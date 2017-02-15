package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s

class T_tpn_thread extends Thread {

    String p_channel = T_tpn_const.GC_EMPTY_STRING
    Integer p_thread_number = T_tpn_const.GC_ZERO
    String p_conf_file_name = T_tpn_const.GC_EMPTY_STRING

    T_tpn_thread(String i_channel, Integer i_thread_number, String i_conf_file_name) {
        super(i_channel + T_tpn_const.GC_UNDERSCORE + i_thread_number.toString())
        init(i_channel, i_thread_number, i_conf_file_name)
    }

    void init(String i_channel, Integer i_thread_number, String i_conf_file_name) {
        p_channel = i_channel
        p_thread_number = i_thread_number
        p_conf_file_name = i_conf_file_name
    }

    @Override
    void run() {
        T_tpn_s.x().init_custom(p_conf_file_name)
        work()
    }

    @I_black_box
    void work() {
        //while (T_tpn_const.GC_TRUE) {
        for (Integer i in [1,2,3,4,5,6,7,8,9]) {
            T_s.l().log_debug(T_s.s().Start_cycle)
            sleep(Integer.valueOf(T_tpn_s.c().GC_POLL_INTERVAL_MILLISECONDS))
            T_s.l().log_debug(T_s.s().Finished_cycle)
        }
    }
}
