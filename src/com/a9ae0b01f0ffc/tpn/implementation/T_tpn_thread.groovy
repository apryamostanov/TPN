package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s
import groovy.sql.Sql

class T_tpn_thread extends Thread {

    String p_channel = T_tpn_const.GC_EMPTY_STRING
    Integer p_thread_number = T_tpn_const.GC_ZERO
    String p_conf_file_name = T_tpn_const.GC_EMPTY_STRING
    String p_mode = T_tpn_const.GC_EMPTY_STRING
    Sql p_sql = T_tpn_const.GC_NULL_OBJ_REF

    T_tpn_thread(String i_channel, Integer i_thread_number, String i_mode, String i_conf_file_name) {
        super(i_channel + T_tpn_const.GC_UNDERSCORE + i_thread_number.toString())
        init(i_channel, i_thread_number, i_mode, i_conf_file_name)
    }

    void init(String i_channel, Integer i_thread_number, String i_mode, String i_conf_file_name) {
        p_channel = i_channel
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
        //while (T_tpn_const.GC_TRUE) {
        for (Integer i in [1,2,3,4,5,6,7,8,9]) {
            T_s.l().log_debug(T_s.s().Start_cycle_for_channel_Z1_and_thread_number_Z2_and_mode_Z3, p_channel, p_thread_number, p_mode)
            T_s.l().log_debug(T_s.s().Scanning_for_messages_to_be_sent)
            String l_statuses_to_select = T_tpn_const.GC_EMPTY_STRING
            if (p_mode == T_tpn_const.GC_MODE_NORMAL) {
                l_statuses_to_select = "(lower(\"$T_tpn_const.GC_STATUS_NEW\"))"
            } else if (p_mode == T_tpn_const.GC_MODE_RETRY) {
                l_statuses_to_select = "(lower(\"$T_tpn_const.GC_STATUS_NEW\"), lower(\"$T_tpn_const.GC_STATUS_FAILED_NO_CONNECTION\"), lower(\"$T_tpn_const.GC_STATUS_FAILED_RESPONSE\"))"
            } else {
                throw new E_application_exception(T_s.s().Unsupported_mode_Z1, p_mode)
            }
            String l_sql_string = "select * from messages where endpoint=\"$p_channel\" and lower(nvl(status, \"$T_tpn_const.GC_STATUS_NEW\")) in $l_statuses_to_select for update"
            T_s.l().log_debug(T_s.s().Running_SQL_Z1, l_sql_string)
            p_sql.eachRow(l_sql_string) { l_print_row ->
                T_s.l().log_debug(T_s.s().Transaction_id_Z1_source_Z2, l_print_row.TXN_ID, l_print_row.SOURCE)
            }
            p_sql.rollback() //change to commit
            T_s.l().log_debug(T_s.s().Committing)
            T_s.l().log_debug(T_s.s().Finished_cycle)
            T_s.l().log_debug(T_s.s().Sleeping_Z1_milliseconds, T_tpn_s.c().GC_POLL_INTERVAL_MILLISECONDS)
            sleep(Integer.valueOf(T_tpn_s.c().GC_POLL_INTERVAL_MILLISECONDS))
        }
    }
}
