package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_thread


class T_main {

    public static void main(String... i_args) {
        Thread.currentThread().setName("TPN_MAIN_THREAD")
        T_tpn_s.x().init_custom(i_args[T_tpn_const.GC_FIRST_INDEX])
        T_s.l().log_info(T_s.s().Welcome_to_Wirecard_Transaction_Push_Notification_Service)
        T_s.l().log_info(T_s.s().Main_thread_started_to_work)
        new T_tpn_thread("GFS", 1, i_args[T_tpn_const.GC_FIRST_INDEX]).start()
        //while (T_tpn_const.GC_TRUE) {
        for (Integer i in [1,2,3,4,5,6,7,8,9]) {
            Thread.sleep(Integer.valueOf(T_tpn_s.c().GC_POLL_INTERVAL_MILLISECONDS))
        }
        //}
    }

}
