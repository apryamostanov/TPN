package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_message
import com.a9ae0b01f0ffc.tpn.main.T_tpn_base_6_util
import groovy.transform.ToString
import groovy.util.slurpersupport.GPathResult

@I_fix_variable_scopes
@ToString(includeNames = true, includeFields = true, includeSuper = false)
class T_tpn_conversion_module extends T_tpn_base_6_util {

    @I_black_box("error")
    static I_http_message convert_http_message(T_tpn_http_message i_http_message) {
        T_tpn_standard_message_format l_tpn_standard_message_format = i_http_message.get_tpn_standard_message_format()
        l_tpn_standard_message_format.UserName = c().GC_TPN_EXTERNAL_USERNAME
        l_tpn_standard_message_format.Password = c().GC_TPN_EXTERNAL_PASSWORD
        l_tpn_standard_message_format.UniqueID = i_http_message.get_tpn_internal_unique_id()
        if (nvl(i_http_message.get_retry_count(), GC_ZERO) > GC_ONE_ONLY) {
            l_tpn_standard_message_format.UniqueIDflag = GC_UNIQUE_ID_FLAG_RETRY
        } else {
            l_tpn_standard_message_format.UniqueIDflag = GC_UNIQUE_ID_FLAG_NORMAL
        }
        Map l_template_variable_map = [
                "std_format": l_tpn_standard_message_format
        ]
        if (is_null(l_tpn_standard_message_format.networkResponseCode)) {
            i_http_message.set_payload(get_template_bank().make(l_template_variable_map).toString())
        } else {
            i_http_message.set_payload(get_template_scheme().make(l_template_variable_map).toString())
        }
        return i_http_message
    }

}
