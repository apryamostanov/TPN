package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.commons.static_string.T_static_string_builder

class T_tpn_s {

    static T_tpn_context x() {
        return (T_tpn_context)T_tpn_context.get_context()
    }

    static T_tpn_commons c() {
        return x().get_commons()
    }

    static final T_static_string_builder s() {
        return T_tpn_const.GC_STATIC_STRING_BUILDER
    }



}
