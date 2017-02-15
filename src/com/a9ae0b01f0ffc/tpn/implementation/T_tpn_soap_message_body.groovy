package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.implementation.T_object_with_guid
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const

class T_tpn_soap_message_body extends T_object_with_guid {

    String p_xml_string = T_tpn_const.GC_EMPTY_STRING

    T_tpn_soap_message_body(String i_xml_string) {
        p_xml_string = i_xml_string
    }

    @I_black_box("error")
    String get_xml_string() {
        return p_xml_string
    }

}
