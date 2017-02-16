package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s
import groovy.sql.Sql

class T_tpn_thread extends Thread {

    String p_source = T_tpn_const.GC_EMPTY_STRING
    String p_endpoint = T_tpn_const.GC_EMPTY_STRING
    Integer p_thread_number = T_tpn_const.GC_ZERO
    String p_conf_file_name = T_tpn_const.GC_EMPTY_STRING
    String p_mode = T_tpn_const.GC_EMPTY_STRING
    Sql p_sql = T_tpn_const.GC_NULL_OBJ_REF

    T_tpn_thread(String i_source, String i_endpoint, Integer i_thread_number, String i_mode, String i_conf_file_name) {
        super(i_source + T_tpn_const.GC_UNDERSCORE + i_thread_number.toString())
        init(i_source, i_endpoint, i_thread_number, i_mode, i_conf_file_name)
    }

    void init(String i_source, String i_endpoint, Integer i_thread_number, String i_mode, String i_conf_file_name) {
        p_source = i_source
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
        p_sql = Sql.newInstance(T_tpn_s.c().GC_MYSQL_CONNECTION_STRING, T_tpn_s.c().GC_MYSQL_USERNAME, T_tpn_s.c().GC_MYSQL_PASSWORD, T_tpn_s.c().GC_MYSQL_DRIVER)
        p_sql.getConnection().setAutoCommit(T_tpn_const.GC_FALSE)
        while (T_tpn_const.GC_TRUE) {
            T_s.l().log_debug(T_s.s().Start_cycle_for_channel_Z1_and_thread_number_Z2_and_mode_Z3, p_endpoint, p_thread_number, p_mode)
            String l_statuses_to_select = T_tpn_const.GC_EMPTY_STRING
            if (p_mode == T_tpn_const.GC_MODE_NORMAL) {
                l_statuses_to_select = "(lower(\"$T_tpn_const.GC_STATUS_NEW\"))"
            } else if (p_mode == T_tpn_const.GC_MODE_RETRY) {
                l_statuses_to_select = "(lower(\"$T_tpn_const.GC_STATUS_NEW\"), lower(\"$T_tpn_const.GC_STATUS_FAILED_NO_CONNECTION\"), lower(\"$T_tpn_const.GC_STATUS_FAILED_RESPONSE\"))"
            } else {
                throw new E_application_exception(T_s.s().Unsupported_mode_Z1, p_mode)
            }
            String l_sql_string = "select * from messages where source=\"$p_source\" and endpoint=\"$p_endpoint\" and lower(ifnull(status, \"$T_tpn_const.GC_STATUS_NEW\")) in $l_statuses_to_select and payload is not null and ifnull(retry_count,0)<= ${T_tpn_s.c().GC_MAX_RETRY_COUNT} for update"
            T_s.l().log_debug(T_s.s().Running_SQL_Z1, l_sql_string)
            p_sql.eachRow(l_sql_string) { l_row ->
                T_tpn_soap_message l_tpn_soap_message = new T_tpn_soap_message(l_row.txn_id, l_row.source, l_row.payload, l_row.endpoint, l_row.status, l_row.retry_count, l_row.post_dt)
                Integer l_response_code = new T_tpn_sender().send_soap(l_tpn_soap_message)
                if (l_response_code == T_tpn_const.GC_HTTP_RESP_CODE_OK) {
                    p_sql.executeUpdate("update messages set status=? where txn_id=\"${l_tpn_soap_message.get_trxn_id()}\"", T_tpn_const.GC_STATUS_DELIVERED)
                } else if (l_response_code == T_tpn_const.GC_RESPONSE_CODE_CONNECTION_REFUSED) {
                    T_s.l().log_warning(T_s.s().Connection_refused, l_response_code)
                    p_sql.executeUpdate("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where txn_id=\"${l_tpn_soap_message.get_trxn_id()}\"", T_tpn_const.GC_STATUS_FAILED_NO_CONNECTION)
                } else if (l_response_code == T_tpn_const.GC_RESPONSE_CODE_INVALID_REQUEST) {
                    T_s.l().log_warning(T_s.s().Connection_refused, l_response_code)
                    p_sql.executeUpdate("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where txn_id=\"${l_tpn_soap_message.get_trxn_id()}\"", T_tpn_const.GC_STATUS_FAILED_INVALID_REQUEST)
                } else if (l_response_code == T_tpn_const.GC_RESPONSE_CODE_INVALID_RESPONSE) {
                    p_sql.executeUpdate("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where txn_id=\"${l_tpn_soap_message.get_trxn_id()}\"", T_tpn_const.GC_STATUS_FAILED_INVALID_RESPONSE)
                } else {
                    T_s.l().log_warning(T_s.s().Unsuccessful_HTTP_Response_Code_Z1, l_response_code)
                    p_sql.executeUpdate("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where txn_id=\"${l_tpn_soap_message.get_trxn_id()}\"", T_tpn_const.GC_STATUS_FAILED_RESPONSE)
                }
            }
            p_sql.commit()
            T_s.l().log_debug(T_s.s().Sleeping_Z1_milliseconds, T_tpn_s.c().GC_POLL_INTERVAL_MILLISECONDS)
            sleep(Integer.valueOf(T_tpn_s.c().GC_POLL_INTERVAL_MILLISECONDS))
        }
    }
}
