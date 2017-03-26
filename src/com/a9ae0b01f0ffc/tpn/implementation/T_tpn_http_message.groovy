package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.middleware.implementation.T_http_message
import groovy.transform.ToString

@I_fix_variable_scopes
@ToString(includeNames = true, includeFields = true, includeSuper = true)
class T_tpn_http_message extends T_http_message {

    String p_trxn_id = GC_EMPTY_STRING
    String p_source = GC_EMPTY_STRING
    String p_channel_name = GC_EMPTY_STRING
    Integer p_retry_count = GC_ZERO
    Date p_post_date = GC_NULL_OBJ_REF as Date
    Integer p_tpn_internal_unique_id = GC_NULL_OBJ_REF as Integer
    String p_payload_type = GC_PAYLOAD_TYPE_XML
    String p_state = GC_EMPTY_STRING

    T_tpn_http_message() {}

    T_tpn_http_message(String i_xml) {
        p_payload = i_xml
    }

    @I_black_box
    String get_state() {
        return p_state
    }

    @I_black_box
    void set_state(String i_state) {
        p_state = i_state
    }

    @I_black_box
    String get_trxn_id() {
        return p_trxn_id
    }

    @I_black_box
    void set_trxn_id(Integer i_trxn_id) {
        p_trxn_id = i_trxn_id
    }

    @I_black_box
    String get_source() {
        return p_source
    }

    @I_black_box
    void set_source(String i_source) {
        p_source = i_source
    }

    @I_black_box
    String get_channel_name() {
        return p_channel_name
    }

    @I_black_box
    void set_channel_name(String i_channel_name) {
        p_channel_name = i_channel_name
    }

    @I_black_box
    Integer get_retry_count() {
        return p_retry_count
    }

    @I_black_box
    Integer set_retry_count(Integer i_retry_count) {
        p_retry_count = i_retry_count
    }

    @I_black_box
    Date get_post_date() {
        return p_post_date
    }

    @I_black_box
    void set_post_date(Date i_post_date) {
        p_post_date = i_post_date
    }

    @I_black_box
    Integer get_tpn_internal_unique_id() {
        return p_tpn_internal_unique_id
    }

    @I_black_box
    void set_tpn_internal_unique_id(Integer i_tpn_internal_unique_id) {
        p_tpn_internal_unique_id = i_tpn_internal_unique_id
    }
}
