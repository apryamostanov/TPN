package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.commons.main.T_common_commons


class T_tpn_commons extends T_common_commons{

    String GC_TPN_CLASSES_CONF
    String GC_BLACK_BOX_CONFIG
    String GC_USER_AGENT = "Mozilla/5.0"
    String GC_TPN_URL = "https://localhost:8081/WDNotifyServices/WDNotifyService.svc/basic"
    String GC_ACCEPT_LANGUAGE = "en-US,enq=0.5"
    String GC_CONTENT_TYPE = "application/soap+xml;charset=UTF-8;action=&quot;http://tempuri.org/IWDNotifyService/TranHistNotificationRequest&quot;"

    T_tpn_commons(String i_conf_file_name) {
        super(i_conf_file_name)
        GC_TPN_CLASSES_CONF = GC_CONST_CONF.GC_TPN_CLASSES_CONF(GC_TPN_CLASSES_CONF)
        GC_BLACK_BOX_CONFIG = GC_CONST_CONF.GC_BLACK_BOX_CONFIG(GC_BLACK_BOX_CONFIG)
        GC_USER_AGENT = GC_CONST_CONF.GC_USER_AGENT(GC_USER_AGENT)
        GC_TPN_URL = GC_CONST_CONF.GC_TPN_URL(GC_TPN_URL)
        GC_ACCEPT_LANGUAGE = GC_CONST_CONF.GC_ACCEPT_LANGUAGE(GC_ACCEPT_LANGUAGE)
        GC_CONTENT_TYPE = GC_CONST_CONF.GC_CONTENT_TYPE(GC_CONTENT_TYPE)
    }

}
