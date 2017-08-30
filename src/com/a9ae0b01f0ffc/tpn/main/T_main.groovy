package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.commons.implementation.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_channel_master_thread
import groovy.util.slurpersupport.GPathResult

@I_fix_variable_scopes
class T_main extends T_tpn_base_6_util {

    @I_black_box("error")
    private static void spawn_thread(GPathResult i_thread_xml) {
        T_tpn_channel_master_thread l_tpn_thread = new T_tpn_channel_master_thread(
                i_thread_xml.@channel_name.text()
                , i_thread_xml.@url.text()
                , Integer.parseInt(i_thread_xml.@normal_threads_count.text())
                , Integer.parseInt(i_thread_xml.@retry_threads_count.text())
                , i_thread_xml.@worker_config_file_name_normal.text()
                , i_thread_xml.@worker_config_file_name_retry.text()
                , i_thread_xml.@master_config_file_name.text()
        )
        l_tpn_thread.start()
    }

    static void main(String... i_args) {
        final String LC_SQL_UPDATE_STATUS = """update messages set status=? where status=? """
        Thread.currentThread().setName(GC_MAIN_TPN_THREAD_NAME)
        if (!method_arguments_present(i_args)) {
            throw new E_application_exception(s.Missing_command_line_argument_Main_configuration_file_name)
        }
        String l_conf_file_name = i_args[GC_FIRST_INDEX]
        init_custom(l_conf_file_name)
        l().log_debug(s.Main_configration_file_name_Z1, l_conf_file_name)
        l().log_info(s.Welcome_to_Wirecard_Transaction_Push_Notification_Service)
        l().log_info(s.Version_Z1, GC_TPN_VERSION)
        l().log_debug(s.Clearing_unprocessed_notifications_from_previous_run)
        sql_update(LC_SQL_UPDATE_STATUS, GC_STATUS_RENEWED, GC_STATUS_WAITING_FOR_PROCESSING)
        GPathResult p_conf = (GPathResult) new XmlSlurper().parse(c().GC_THREAD_CONFIG_FILE_NAME)
        for (l_thread_xml in p_conf.children()) {
            spawn_thread(l_thread_xml as GPathResult)
        }
    }

}
