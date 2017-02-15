package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s
import sun.net.www.protocol.https.DefaultHostnameVerifier

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


class T_host_name_verifier implements HostnameVerifier {

    @Override
    @I_black_box
    boolean verify(String l_host_name, SSLSession l_ssl_session) {
        if (T_tpn_s.c().GC_HOST_NAME == l_host_name) {
            return T_tpn_const.GC_TRUE
        } else {
            return T_tpn_const.GC_FALSE
        }
    }
}
