package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.commons.implementation.round_robin.T_round_robin

import static com.a9ae0b01f0ffc.tpn.main.T_tpn_base_6_util.*

@I_fix_variable_scopes
class T_tpn_channel_master_thread extends Thread {

    String p_channel_name = GC_EMPTY_STRING
    String p_url = GC_EMPTY_STRING
    Integer p_normal_threads_count = GC_ZERO
    Integer p_retry_threads_count = GC_ZERO
    String p_config_file_name = GC_EMPTY_STRING
    String p_worker_config_file_name_normal = GC_EMPTY_STRING
    String p_worker_config_file_name_retry = GC_EMPTY_STRING
    T_round_robin<T_tpn_channel_worker_thread> p_worker_threads_round_robin_normal = GC_NULL_OBJ_REF as T_round_robin<T_tpn_channel_worker_thread>
    T_round_robin<T_tpn_channel_worker_thread> p_worker_threads_round_robin_retry = GC_NULL_OBJ_REF as T_round_robin<T_tpn_channel_worker_thread>
    Integer p_last_processed_tpn_id = GC_ZERO

    @I_black_box("error")
    T_tpn_channel_master_thread(String i_channel_name, String i_url, Integer i_normal_threads_count, Integer i_retry_threads_count, String i_worker_config_file_name_normal, String i_worker_config_file_name_retry, String i_master_config_file_name) {
        this.p_config_file_name = i_master_config_file_name
        this.p_channel_name = i_channel_name
        this.p_url = i_url
        this.p_normal_threads_count = i_normal_threads_count
        this.p_retry_threads_count = i_retry_threads_count
        this.p_worker_config_file_name_normal = i_worker_config_file_name_normal
        this.p_worker_config_file_name_retry = i_worker_config_file_name_retry
        ArrayList<T_tpn_channel_worker_thread> l_channel_worker_threads_normal = new ArrayList<T_tpn_channel_worker_thread>()
        ArrayList<T_tpn_channel_worker_thread> l_channel_worker_threads_retry = new ArrayList<T_tpn_channel_worker_thread>()
        for (Integer l_thread_index in GC_ONE_ONLY..p_normal_threads_count) {
            T_tpn_channel_worker_thread l_thread = new T_tpn_channel_worker_thread(i_channel_name, i_url, l_thread_index, GC_MODE_NORMAL, i_worker_config_file_name_normal)
            l_channel_worker_threads_normal.add(l_thread)
            l_thread.start()
        }
        for (Integer l_thread_index in GC_ONE_ONLY..p_retry_threads_count) {
            T_tpn_channel_worker_thread l_thread = new T_tpn_channel_worker_thread(i_channel_name, i_url, l_thread_index, GC_MODE_RETRY, i_worker_config_file_name_retry)
            l_channel_worker_threads_retry.add(l_thread)
            l_thread.start()
        }
        p_worker_threads_round_robin_normal = new T_round_robin<T_tpn_channel_worker_thread>(l_channel_worker_threads_normal)
        p_worker_threads_round_robin_retry = new T_round_robin<T_tpn_channel_worker_thread>(l_channel_worker_threads_retry)
    }

    @I_black_box("error")
    static Boolean is_not_duplicate(Integer i_trxn_id, Integer i_tpn_internal_unique_id, String i_channel) {
        final String LC_SQL_SELECT_SUCCESSFUL_WITH_SAME_TRXN_ID_AND_DIFFERENT_UNIQUE_ID = """select * from messages where endpoint="$i_channel" and txn_id=$i_trxn_id and tpn_internal_unique_id<>$i_tpn_internal_unique_id and lower(status) in (lower("$GC_STATUS_DELIVERED"), lower("$GC_STATUS_WAITING_FOR_PROCESSING"))"""
        get_sql().eachRow(LC_SQL_SELECT_SUCCESSFUL_WITH_SAME_TRXN_ID_AND_DIFFERENT_UNIQUE_ID) { l_row ->
            l().log_warning(s.Transaction_with_same_TrxnID_Z1_and_different_UniqueID_Z2_already_exists_in_status_Z3_for_new_message_with_UniqueID_Z4_for_channel_Z5, i_trxn_id, l_row.tpn_internal_unique_id, l_row.status, i_tpn_internal_unique_id, i_channel)
            return GC_FALSE
        }
        return GC_TRUE
    }

    @I_black_box("error")
    void run_with_logging() {
        String p_sql_select_main_query
        l().log_info(s.Starting_Master_thread_for_channel_Z1_with_retry_count_Z2, p_channel_name, c().GC_MAX_RETRY_COUNT)
        if (GC_ZERO == Integer.parseInt(c().GC_MAX_RETRY_COUNT)) {
            final String LC_SQL_SELECT_LAST_ID = """select max(tpn_internal_unique_id) as last_id from messages where endpoint="$p_channel_name" """
            l().log_send_sql(LC_SQL_SELECT_LAST_ID, p_channel_name)
            Object l_row = get_sql().firstRow(LC_SQL_SELECT_LAST_ID)
            p_last_processed_tpn_id = l_row.last_id
            l().log_receive_sql(p_last_processed_tpn_id)
        }
        while (GC_TRUE) {
            final String LC_SQL_UPDATE_STATUS = """update messages set status=? where tpn_internal_unique_id=?"""
            if (GC_ZERO == Integer.parseInt(c().GC_MAX_RETRY_COUNT)) {
                p_sql_select_main_query = """select * from messages where
                                            endpoint="$p_channel_name" and tpn_internal_unique_id > $p_last_processed_tpn_id and
                                            lower(status) in ("$GC_STATUS_NEW", "$GC_STATUS_RENEWED", "$GC_STATUS_FAILED_NO_CONNECTION", "$GC_STATUS_FAILED_RESPONSE")
                                            order by tpn_internal_unique_id asc"""
            } else {
                p_sql_select_main_query = """select * from messages where endpoint="$p_channel_name" and (lower(status) in ("$GC_STATUS_NEW", "$GC_STATUS_RENEWED") or
                                            (lower(status) in ("$GC_STATUS_FAILED_NO_CONNECTION", "$GC_STATUS_FAILED_RESPONSE") and
                                            ifnull(retry_count,0)<= ${c().GC_MAX_RETRY_COUNT} and
                                            ifnull(send_time,now()) <= date_sub(now(), INTERVAL ${c().GC_RESEND_INTERVAL_SECONDS} SECOND)))
                                            order by tpn_internal_unique_id asc"""
            }
            get_sql().eachRow(p_sql_select_main_query) { l_row ->
                l().log_receive_sql(l_row)
                if (is_not_duplicate(Integer.parseInt(l_row.txn_id), l_row.tpn_internal_unique_id, p_channel_name)) {
                    sql_update(LC_SQL_UPDATE_STATUS, GC_STATUS_WAITING_FOR_PROCESSING, l_row.tpn_internal_unique_id)
                    T_tpn_http_message l_message = new T_tpn_http_message(l_row, p_url)
                    if (GC_ZERO == Integer.parseInt(c().GC_MAX_RETRY_COUNT)) {
                        p_last_processed_tpn_id = l_row.tpn_internal_unique_id
                    }
                    T_tpn_channel_worker_thread l_next_thread
                    if ([GC_STATUS_NEW, GC_STATUS_RENEWED].contains(l_message.get_state())) {
                        l_next_thread = ++p_worker_threads_round_robin_normal.iterator()
                    } else {
                        l_next_thread = ++p_worker_threads_round_robin_retry.iterator()
                    }
                    l_next_thread.p_tpn_http_message_queue.put(l_message)
                    commit()//release row-level lock prior to passing message to thread
                    synchronized (l_next_thread) {
                        l_next_thread.notify()
                    }
                } else {
                    sql_update(LC_SQL_UPDATE_STATUS, GC_STATUS_DUPLICATE, l_row.tpn_internal_unique_id)
                    commit()
                }
                //here is no wait and it causes CPU-intensive upload of messages into queues of worker threads.
                //This is normal behavior in case when there are many messages accumulated in DB - they get assigned to threads and loaded into RAM (Java heap)
            }
            sleep(new Long(c().GC_CYCLE_INTERVAL_MILLISECONDS))
        }
    }

    @Override
    void run() {
        setName(GC_MASTER_TPN_THREAD_NAME_PREFIX + p_channel_name)
        init_custom(p_config_file_name)
        run_with_logging()
    }

}
