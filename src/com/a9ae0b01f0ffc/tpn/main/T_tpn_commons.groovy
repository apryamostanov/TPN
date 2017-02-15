package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.commons.main.T_common_commons


class T_tpn_commons extends T_common_commons{

    String GC_TPN_CLASSES_CONF
    String GC_BLACK_BOX_CONFIG
    String GC_REQUEST_METHOD = "POST"
    String GC_USER_AGENT = "Mozilla/5.0"
    String GC_TPN_URL = "https://localhost:8081/WDNotifyServices/WDNotifyService.svc/basic"
    String GC_ACCEPT_LANGUAGE = "en-US,enq=0.5"
    String GC_CONTENT_TYPE = "application/soap+xml;charset=UTF-8;action=&quot;http://tempuri.org/IWDNotifyService/TranHistNotificationRequest&quot;"
    String GC_HOST_NAME = "localhost"
    String GC_UNSERCURE_TEST_TLS_SSL_MODE = "false"
    String GC_POLL_INTERVAL_MILLISECONDS = 2000

    T_tpn_commons(String i_conf_file_name) {
        super(i_conf_file_name)
        GC_TPN_CLASSES_CONF = GC_CONST_CONF.GC_TPN_CLASSES_CONF(GC_TPN_CLASSES_CONF)
        GC_BLACK_BOX_CONFIG = GC_CONST_CONF.GC_BLACK_BOX_CONFIG(GC_BLACK_BOX_CONFIG)
        GC_REQUEST_METHOD = GC_CONST_CONF.GC_REQUEST_METHOD(GC_REQUEST_METHOD)
        GC_USER_AGENT = GC_CONST_CONF.GC_USER_AGENT(GC_USER_AGENT)
        GC_TPN_URL = GC_CONST_CONF.GC_TPN_URL(GC_TPN_URL)
        GC_ACCEPT_LANGUAGE = GC_CONST_CONF.GC_ACCEPT_LANGUAGE(GC_ACCEPT_LANGUAGE)
        GC_CONTENT_TYPE = GC_CONST_CONF.GC_CONTENT_TYPE(GC_CONTENT_TYPE)
        GC_HOST_NAME = GC_CONST_CONF.GC_HOST_NAME(GC_HOST_NAME)
        GC_UNSERCURE_TEST_TLS_SSL_MODE = GC_CONST_CONF.GC_UNSERCURE_TEST_TLS_SSL_MODE(GC_UNSERCURE_TEST_TLS_SSL_MODE)
        GC_POLL_INTERVAL_MILLISECONDS = GC_CONST_CONF.GC_POLL_INTERVAL_MILLISECONDS(GC_POLL_INTERVAL_MILLISECONDS)
    }

}
