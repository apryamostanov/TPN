package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_logging_const
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_thread
import groovy.sql.Sql
import groovy.util.slurpersupport.GPathResult


class T_main {

    private static HashMap<String, HashMap<Integer, T_tpn_thread>> p_threads_by_source_by_number = new HashMap<String, HashMap<Integer, T_tpn_thread>>()

    @I_black_box
    private static void spawn_thread(GPathResult i_thread_xml) {
        T_tpn_thread l_tpn_thread = new T_tpn_thread(i_thread_xml.@source.text(), i_thread_xml.@endpoint.text(), Integer.parseInt(i_thread_xml.@thread_number.text()), i_thread_xml.@mode.text(), i_thread_xml.@thread_config_file_name.text())
        if (l_tpn_thread.get_mode() != T_tpn_const.GC_MODE_RETRY) {
            if (p_threads_by_source_by_number.containsKey(l_tpn_thread.get_source())) {
                p_threads_by_source_by_number.get(l_tpn_thread.get_source()).put(l_tpn_thread.get_thread_number(), l_tpn_thread)
            } else {
                HashMap<Integer, T_tpn_thread> l_threads_by_number = new HashMap<Integer, T_tpn_thread>()
                l_threads_by_number.put(l_tpn_thread.get_thread_number(), l_tpn_thread)
                p_threads_by_source_by_number.put(l_tpn_thread.get_source(), l_threads_by_number)
            }
        }
        l_tpn_thread.start()
    }

    static void main(String... i_args) {
        Thread.currentThread().setName("TPN_MAIN_THREAD")
        if (!T_u.method_arguments_present(i_args)) {
            throw new E_application_exception(T_s.s().Missing_command_line_argument_Main_configuration_file_name)
        }
        String l_conf_file_name = i_args[T_tpn_const.GC_FIRST_INDEX]
        T_tpn_s.x().init_custom(l_conf_file_name)
        T_s.l().log_debug(T_s.s().Main_configration_file_name_Z1, l_conf_file_name)
        T_s.l().log_info(T_s.s().Welcome_to_Wirecard_Transaction_Push_Notification_Service)
        T_s.l().log_info(T_s.s().Main_thread_started_to_work)
        Sql p_sql = Sql.newInstance(T_tpn_s.c().GC_MYSQL_CONNECTION_STRING, T_tpn_s.c().GC_MYSQL_USERNAME, T_tpn_s.c().GC_MYSQL_PASSWORD, T_tpn_s.c().GC_MYSQL_DRIVER)
        T_s.l().log_debug(T_s.s().Clearing_previous_thread_affinity)
        p_sql.executeUpdate("update messages set thread_number=null where thread_number is not null")
        GPathResult p_conf = T_logging_const.GC_NULL_OBJ_REF as GPathResult
        p_conf = (GPathResult) new XmlSlurper().parse(T_tpn_s.c().GC_THREAD_CONFIG_FILE_NAME)
        for (l_thread_xml in p_conf.children()) {
            spawn_thread(l_thread_xml as GPathResult)
        }
        while (T_tpn_const.GC_TRUE) {
            T_s.l().log_debug(T_s.s().Dispatching_new_messages_to_worker_threads)
            //TODO: add parameter to control aging of pending messages
            for (String l_source_to_update in p_threads_by_source_by_number.keySet()) {
                p_sql.executeUpdate("update messages set thread_number=FLOOR( 1 + RAND( ) * ${p_threads_by_source_by_number.get(l_source_to_update).keySet().size()} ) where thread_number is null")
            }
            Thread.sleep(Integer.valueOf(T_tpn_s.c().GC_CYCLE_INTERVAL_MILLISECONDS))
        }
    }

}
