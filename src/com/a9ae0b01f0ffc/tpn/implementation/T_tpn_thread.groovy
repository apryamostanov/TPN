package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s
import groovy.transform.CompileStatic

class T_tpn_thread extends Thread {

    String p_channel_name = T_tpn_const.GC_EMPTY_STRING
    String p_endpoint = T_tpn_const.GC_EMPTY_STRING
    Integer p_thread_number = T_tpn_const.GC_ZERO
    String p_conf_file_name = T_tpn_const.GC_EMPTY_STRING
    String p_mode = T_tpn_const.GC_EMPTY_STRING

    T_tpn_thread(String i_channel_name, String i_endpoint, Integer i_thread_number, String i_mode, String i_conf_file_name) {
        super(i_channel_name + T_tpn_const.GC_UNDERSCORE + i_thread_number.toString())
        init(i_channel_name, i_endpoint, i_thread_number, i_mode, i_conf_file_name)
    }

    void init(String i_channel_name, String i_endpoint, Integer i_thread_number, String i_mode, String i_conf_file_name) {
        p_channel_name = i_channel_name
        p_endpoint = i_endpoint
        p_thread_number = i_thread_number
        p_mode = i_mode
        p_conf_file_name = i_conf_file_name
    }

    @Override
    void run() {
        T_tpn_s.x().init_custom(p_conf_file_name)
        work()
    }

    @I_black_box
    void work() {
        while (T_tpn_const.GC_TRUE) {
            T_s.l().log_debug(T_s.s().Start_cycle_for_channel_Z1_and_thread_number_Z2_and_mode_Z3, p_endpoint, p_thread_number, p_mode)
            String l_statuses_to_select = T_tpn_const.GC_EMPTY_STRING
            String l_thread_affinity = T_tpn_const.GC_EMPTY_STRING
            if (p_mode == T_tpn_const.GC_MODE_NORMAL) {
                l_statuses_to_select = "(lower(\"$T_tpn_const.GC_STATUS_NEW\"))"
                l_thread_affinity = " and thread_number=$p_thread_number"
            } else if (p_mode == T_tpn_const.GC_MODE_RETRY) {
                l_statuses_to_select = "(lower(\"$T_tpn_const.GC_STATUS_FAILED_NO_CONNECTION\"), lower(\"$T_tpn_const.GC_STATUS_FAILED_RESPONSE\"))"
            } else {
                throw new E_application_exception(T_s.s().Unsupported_mode_Z1, p_mode)
            }
            String l_sql_string = "select * from messages where endpoint=\"$p_endpoint\" " +
                    "and lower(ifnull(status, \"$T_tpn_const.GC_STATUS_NEW\")) in $l_statuses_to_select " +
                    "and payload is not null and ifnull(retry_count,0)< ${T_tpn_s.c().GC_MAX_RETRY_COUNT} " +
                    "$l_thread_affinity limit ${T_tpn_s.c().GC_POLL_LIMIT} for update"
            T_s.l().log_sql("eachRow", l_sql_string)
            T_tpn_s.x().get_sql().eachRow(l_sql_string) { l_row ->
                T_tpn_soap_message l_tpn_soap_message = new T_tpn_soap_message(l_row.txn_id, l_row.source, l_row.payload, l_row.endpoint, l_row.status, l_row.retry_count, l_row.post_dt, l_row.tpn_internal_unique_id)
                try {
                    Integer l_response_code = new T_tpn_sender().send_soap(l_tpn_soap_message)
                    if (l_response_code == T_tpn_const.GC_HTTP_RESP_CODE_OK) {
                        T_tpn_s.x().sql_update("update messages set status=? where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", T_tpn_const.GC_STATUS_DELIVERED)
                    } else if (l_response_code == T_tpn_const.GC_RESPONSE_CODE_CONNECTION_REFUSED) {
                        T_s.l().log_warning(T_s.s().Connection_refused_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_row.tpn_internal_unique_id, l_row.txn_id)
                        T_tpn_s.x().sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", T_tpn_const.GC_STATUS_FAILED_NO_CONNECTION)
                    } else if (l_response_code == T_tpn_const.GC_RESPONSE_CODE_INVALID_REQUEST) {
                        T_s.l().log_warning(T_s.s().Invalid_Request_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_row.tpn_internal_unique_id, l_row.txn_id)
                        T_tpn_s.x().sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", T_tpn_const.GC_STATUS_FAILED_INVALID_REQUEST)
                    } else if (l_response_code == T_tpn_const.GC_RESPONSE_CODE_INVALID_RESPONSE) {
                        T_s.l().log_warning(T_s.s().Invalid_Response_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_row.tpn_internal_unique_id, l_row.txn_id)
                        T_tpn_s.x().sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", T_tpn_const.GC_STATUS_FAILED_INVALID_RESPONSE)
                    } else {
                        T_s.l().log_warning(T_s.s().Unsuccessful_HTTP_Response_Code_Z1_for_message_TPN_ID_Z2_CoreCard_ID_Z3, l_response_code, l_row.tpn_internal_unique_id, l_row.txn_id)
                        T_tpn_s.x().sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", T_tpn_const.GC_STATUS_FAILED_RESPONSE)
                    }
                } catch (Throwable e_others) {
                    T_s.l().log_warning(T_s.s().Exception_Z1_for_message_TPN_ID_Z2_CoreCard_ID_Z3, e_others, l_row.tpn_internal_unique_id, l_row.txn_id)
                    T_tpn_s.x().sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", T_tpn_const.GC_STATUS_EXCEPTION)
                }
            }
            T_tpn_s.x().get_sql().commit()
            T_s.l().log_debug(T_s.s().Sleeping_Z1_milliseconds, T_tpn_s.c().GC_CYCLE_INTERVAL_MILLISECONDS)
            sleep(Integer.valueOf(T_tpn_s.c().GC_CYCLE_INTERVAL_MILLISECONDS))
        }
    }

    @I_black_box("error")
    String get_channel_name() {
        return p_channel_name
    }

    @I_black_box("error")
    String get_endpoint() {
        return p_endpoint
    }

    @I_black_box("error")
    Integer get_thread_number() {
        return p_thread_number
    }

    @I_black_box("error")
    String get_conf_file_name() {
        return p_conf_file_name
    }

    @I_black_box("error")
    String get_mode() {
        return p_mode
    }

}
