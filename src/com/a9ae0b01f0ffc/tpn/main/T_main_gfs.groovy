package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_logging_const
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_sender
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_soap_message
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_thread
import groovy.util.slurpersupport.GPathResult

class T_main_gfs {


    static void main(String... i_args) {
        Thread.currentThread().setName("TPN_MAIN_THREAD")
        if (!T_u.method_arguments_present(i_args)) {
            throw new E_application_exception(T_s.s().Missing_command_line_argument_Main_configuration_file_name)
        }
        String l_conf_file_name = i_args[T_tpn_const.GC_FIRST_INDEX]
        T_tpn_s.x().init_custom(l_conf_file_name)
        T_s.l().log_debug(T_s.s().Main_configration_file_name_Z1, l_conf_file_name)
        T_s.l().log_info(T_s.s().Welcome_to_Wirecard_Transaction_Push_Notification_Service)
        T_tpn_soap_message l_tpn_soap_message = new T_tpn_soap_message()
        l_tpn_soap_message.set_endpoint(T_tpn_s.c().GC_DEFAULT_ENDPOINT)
        l_tpn_soap_message.set_payload(new String(new File(T_tpn_s.c().GC_SAMPLE_FILE_NAME).readBytes()))
        new T_tpn_sender().send_soap(l_tpn_soap_message)
    }

}
