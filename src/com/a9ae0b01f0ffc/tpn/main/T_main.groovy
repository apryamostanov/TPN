package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_logging_const
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.exceptions.E_application_exception
import com.a9ae0b01f0ffc.commons.implementation.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_thread
import groovy.util.slurpersupport.GPathResult

class T_main extends T_tpn_base_6_util{

    private static HashMap<String, HashMap<Integer, T_tpn_thread>> p_threads_by_source_by_number = new HashMap<String, HashMap<Integer, T_tpn_thread>>()

    @I_black_box
    private static void spawn_thread(GPathResult i_thread_xml) {
        T_tpn_thread l_tpn_thread = new T_tpn_thread(i_thread_xml.@channel_name.text(), i_thread_xml.@endpoint.text(), Integer.parseInt(i_thread_xml.@thread_number.text()), i_thread_xml.@mode.text(), i_thread_xml.@thread_config_file_name.text())
        if (l_tpn_thread.get_mode() != GC_MODE_RETRY) {
            if (p_threads_by_source_by_number.containsKey(l_tpn_thread.get_channel_name())) {
                p_threads_by_source_by_number.get(l_tpn_thread.get_channel_name()).put(l_tpn_thread.get_thread_number(), l_tpn_thread)
            } else {
                HashMap<Integer, T_tpn_thread> l_threads_by_number = new HashMap<Integer, T_tpn_thread>()
                l_threads_by_number.put(l_tpn_thread.get_thread_number(), l_tpn_thread)
                p_threads_by_source_by_number.put(l_tpn_thread.get_channel_name(), l_threads_by_number)
            }
        }
        l_tpn_thread.start()
    }

    static void main(String... i_args) {
        Thread.currentThread().setName("TPN_MAIN_THREAD")
        if (!method_arguments_present(i_args)) {
            throw new E_application_exception(s.Missing_command_line_argument_Main_configuration_file_name)
        }
        String l_conf_file_name = i_args[GC_FIRST_INDEX]
        init_custom(l_conf_file_name)
        l().log_debug(s.Main_configration_file_name_Z1, l_conf_file_name)
        l().log_info(s.Welcome_to_Wirecard_Transaction_Push_Notification_Service)
        l().log_info(s.Main_thread_started_to_work)
        l().log_debug(s.Clearing_previous_thread_affinity)
        sql_update("update messages set thread_number=null where thread_number is not null and lower(status) in (lower(\"$GC_STATUS_NEW\"), lower(\"$GC_STATUS_FAILED_NO_CONNECTION\"), lower(\"$GC_STATUS_FAILED_RESPONSE\"))")
        commit()
        GPathResult p_conf = (GPathResult) new XmlSlurper().parse(c().GC_THREAD_CONFIG_FILE_NAME)
        for (l_thread_xml in p_conf.children()) {
            spawn_thread(l_thread_xml as GPathResult)
        }
        while (GC_TRUE) {
            l().log_debug(s.Dispatching_new_messages_to_worker_threads)
            //TODO: add parameter to control aging of pending messages
            for (String l_source_to_update in p_threads_by_source_by_number.keySet()) {
                sql_update("update messages set thread_number=FLOOR( 1 + RAND( ) * ${p_threads_by_source_by_number.get(l_source_to_update).keySet().size()} ) where thread_number is null")
                commit()
            }
            Thread.sleep(Integer.valueOf(c().GC_CYCLE_INTERVAL_MILLISECONDS))
        }
    }

}
