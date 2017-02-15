package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const

import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

class T_test_trust_manager implements X509TrustManager {

    @Override
    @I_black_box
    public X509Certificate[] getAcceptedIssuers() {
        return T_tpn_const.GC_NULL_OBJ_REF as X509Certificate[]
    }

    @Override
    @I_black_box
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    @Override
    @I_black_box
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }

}
