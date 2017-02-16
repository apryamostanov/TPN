package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.implementation.T_object_with_guid
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import groovy.transform.ToString

class T_tpn_soap_message extends T_object_with_guid {

    String p_trxn_id = T_tpn_const.GC_EMPTY_STRING
    String p_source = T_tpn_const.GC_EMPTY_STRING
    String p_payload = T_tpn_const.GC_EMPTY_STRING
    String p_endpoint = T_tpn_const.GC_EMPTY_STRING
    String p_status = T_tpn_const.GC_EMPTY_STRING
    Integer p_retry_count = T_tpn_const.GC_ZERO
    Date p_post_date = T_tpn_const.GC_NULL_OBJ_REF as Date

    T_tpn_soap_message(String i_trxn_id, String i_source, String i_payload, String i_endpoint, String i_status, Integer i_retry_count, Date i_post_date) {
        T_s.l().log_debug(T_s.s().T_tpn_soap_message_creation_Trxn_Id_Z1_Source_Z2_Endpoint_Z4_Status_Z5_Retry_count_Z6_Post_date_Z7, i_trxn_id, i_source, i_payload, i_endpoint, i_status, i_retry_count, i_post_date)
        this.p_trxn_id = i_trxn_id
        this.p_source = i_source
        this.p_payload = i_payload
        this.p_endpoint = i_endpoint
        this.p_status = i_status
        this.p_retry_count = i_retry_count
        this.p_post_date = i_post_date
    }

    T_tpn_soap_message(String i_xml_string) {
        p_payload = i_xml_string
    }

    @I_black_box("error")
    String get_payload() {
        return p_payload
    }

    String get_trxn_id() {
        return p_trxn_id
    }

    String get_source() {
        return p_source
    }

    String get_endpoint() {
        return p_endpoint
    }

    String get_status() {
        return p_status
    }

    Integer get_retry_count() {
        return p_retry_count
    }

    Date get_post_date() {
        return p_post_date
    }


    @Override
    public String toString() {
        return "T_tpn_soap_message{" +
                "p_trxn_id='" + p_trxn_id + '\'' +
                ", p_source='" + p_source + '\'' +
                ", p_endpoint='" + p_endpoint + '\'' +
                ", p_status='" + p_status + '\'' +
                ", p_retry_count=" + p_retry_count +
                ", p_post_date=" + p_post_date +
                '}';
    }
}
