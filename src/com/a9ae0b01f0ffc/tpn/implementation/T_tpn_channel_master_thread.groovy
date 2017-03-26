package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes

import static com.a9ae0b01f0ffc.tpn.main.T_tpn_base_6_util.*

@I_fix_variable_scopes
class T_tpn_channel_master_thread extends Thread {

    String p_channel_name = GC_EMPTY_STRING
    String p_url = GC_EMPTY_STRING
    Integer p_normal_threads_count = GC_ZERO
    Integer p_retry_threads_count = GC_ZERO
    String p_normal_config_file_name = GC_EMPTY_STRING
    String retry_config_file_name = GC_EMPTY_STRING
    T_round_robin<T_tpn_channel_worker_thread> p_worker_threads_round_robin_normal = GC_NULL_OBJ_REF as T_round_robin<T_tpn_channel_worker_thread>
    T_round_robin<T_tpn_channel_worker_thread> p_worker_threads_round_robin_retry = GC_NULL_OBJ_REF as T_round_robin<T_tpn_channel_worker_thread>

    @I_black_box
    T_tpn_channel_master_thread(String i_channel_name, String i_url, Integer i_normal_threads_count, Integer i_retry_threads_count, String i_normal_config_file_name, String i_retry_config_file_name) {
        this.p_channel_name = i_channel_name
        this.p_url = i_url
        this.p_normal_threads_count = i_normal_threads_count
        this.p_retry_threads_count = i_retry_threads_count
        this.p_normal_config_file_name = i_normal_config_file_name
        this.retry_config_file_name = i_retry_config_file_name
        ArrayList<T_tpn_channel_worker_thread> l_channel_worker_threads_normal = new ArrayList<T_tpn_channel_worker_thread>()
        ArrayList<T_tpn_channel_worker_thread> l_channel_worker_threads_retry = new ArrayList<T_tpn_channel_worker_thread>()
        for (Integer l_thread_index in GC_ONE_ONLY..p_normal_threads_count) {
            T_tpn_channel_worker_thread l_thread = new T_tpn_channel_worker_thread(i_channel_name, i_url, l_thread_index, GC_MODE_NORMAL, i_normal_config_file_name)
            l_channel_worker_threads_normal.add(l_thread)
            l_thread.start()
        }
        for (Integer l_thread_index in GC_ONE_ONLY..p_retry_threads_count) {
            T_tpn_channel_worker_thread l_thread = new T_tpn_channel_worker_thread(i_channel_name, i_url, l_thread_index, GC_MODE_RETRY, i_retry_config_file_name)
            l_channel_worker_threads_retry.add(l_thread)
            l_thread.start()
        }
        p_worker_threads_round_robin_normal = new T_round_robin<T_tpn_channel_worker_thread>(l_channel_worker_threads_normal)
        p_worker_threads_round_robin_retry = new T_round_robin<T_tpn_channel_worker_thread>(l_channel_worker_threads_retry)
    }

    @I_black_box
    void run_with_logging() {
        final String LC_SQL_SELECT_MAIN_QUERY = """select * from messages where lower(status) in (lower("$GC_STATUS_NEW"), lower("$GC_STATUS_FAILED_NO_CONNECTION"), lower("$GC_STATUS_FAILED_RESPONSE")) and endpoint="$p_channel_name" """
        final String LC_SQL_UPDATE_STATUS = """update messages set status=? where tpn_internal_unique_id=?"""
        l().log_info(s.Master_thread_for_channel_Z1, p_channel_name)
        l().log_send_sql(LC_SQL_SELECT_MAIN_QUERY)
        get_sql().eachRow(LC_SQL_SELECT_MAIN_QUERY) { l_row ->
            l().log_receive_sql(l_row)
            sql_update(LC_SQL_UPDATE_STATUS, GC_STATUS_UNKNOWN, l_row.tpn_internal_unique_id)
            commit()
            T_tpn_http_message l_message = new T_tpn_http_message(l_row, p_url)
            T_tpn_channel_worker_thread l_next_thread
            if (l_message.get_state() == GC_STATUS_NEW) {
                l_next_thread = ++p_worker_threads_round_robin_normal.iterator()
            } else {
                l_next_thread = ++p_worker_threads_round_robin_retry.iterator()
            }
            l_next_thread.p_tpn_http_message_queue.put(l_message)
            synchronized (l_next_thread) {
                l_next_thread.notify()
            }
        }
    }

    @Override
    void run() {
        setName(GC_MASTER_TPN_THREAD_NAME_PREFIX + p_channel_name)
        init_custom(c().GC_THREAD_CONFIG_FILE_NAME)
        run_with_logging()
    }

}
