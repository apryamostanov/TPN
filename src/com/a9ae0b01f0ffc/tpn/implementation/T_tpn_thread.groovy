package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.commons.implementation.exceptions.E_application_exception
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_message
import com.a9ae0b01f0ffc.middleware.implementation.T_middleware_sender
import com.a9ae0b01f0ffc.middleware.main.T_middleware_base_6_util
import groovy.transform.CompileStatic

import static com.a9ae0b01f0ffc.tpn.main.T_tpn_base_6_util.*

@CompileStatic
class T_tpn_thread extends Thread {
    
    static final String PC_HEADER_NAME_USER_AGENT = "User-Agent"
    static final String PC_HEADER_NAME_ACCEPT_LANGUAGE = "Accept-Language"
    static final String PC_HEADER_NAME_ACCEPT_CONTENT_TYPE = "Content-Type"
    String p_channel_name = GC_EMPTY_STRING
    String p_endpoint = GC_EMPTY_STRING
    Integer p_thread_number = GC_ZERO
    String p_conf_file_name = GC_EMPTY_STRING
    String p_mode = GC_EMPTY_STRING
    String p_payload_type = T_middleware_base_6_util.GC_PAYLOAD_TYPE_XML

    T_tpn_thread(String i_channel_name, String i_endpoint, Integer i_thread_number, String i_mode, String i_conf_file_name, String i_payload_type = T_middleware_base_6_util.GC_PAYLOAD_TYPE_XML) {
        super(i_channel_name + GC_UNDERSCORE + i_thread_number.toString())
        init(i_channel_name, i_endpoint, i_thread_number, i_mode, i_conf_file_name, i_payload_type)
    }

    void init(String i_channel_name, String i_endpoint, Integer i_thread_number, String i_mode, String i_conf_file_name, String i_payload_type) {
        p_channel_name = i_channel_name
        p_endpoint = i_endpoint
        p_thread_number = i_thread_number
        p_mode = i_mode
        p_conf_file_name = i_conf_file_name
        p_payload_type = i_payload_type
    }

    @Override
    void run() {
        init_custom(p_conf_file_name)
        work()
    }

    @I_black_box
    void work() {
        while (GC_TRUE) {
            l().log_debug(s.Start_cycle_for_channel_Z1_and_thread_number_Z2_and_mode_Z3, p_channel_name, p_thread_number, p_mode)
            String l_statuses_to_select
            String l_thread_affinity = GC_EMPTY_STRING
            if (p_mode == GC_MODE_NORMAL) {
                l_statuses_to_select = "(lower(\"$GC_STATUS_NEW\"))"
                l_thread_affinity = " and thread_number=$p_thread_number"
            } else if (p_mode == GC_MODE_RETRY) {
                l_statuses_to_select = "(lower(\"$GC_STATUS_FAILED_NO_CONNECTION\"), lower(\"$GC_STATUS_FAILED_RESPONSE\"))"
            } else {
                throw new E_application_exception(s.Unsupported_mode_Z1, p_mode)
            }
            String l_sql_string = "select * from messages where endpoint=\"$p_channel_name\" " +
                    "and lower(ifnull(status, \"$GC_STATUS_NEW\")) in $l_statuses_to_select " +
                    "and payload is not null and ifnull(retry_count,0)< ${c().GC_MAX_RETRY_COUNT} " +
                    "$l_thread_affinity limit ${c().GC_POLL_LIMIT} for update"
            l().log_send_sql("eachRow", l_sql_string)
            get_sql().eachRow(l_sql_string) { l_row ->
                l().log_receive_sql(l_row)
                T_tpn_http_message l_tpn_soap_message = new T_tpn_http_message()
                l_tpn_soap_message.set_trxn_id(l_row.txn_id)
                l_tpn_soap_message.set_source(l_row.source)
                l_tpn_soap_message.set_payload(l_row.payload)
                l_tpn_soap_message.set_payload_type(p_payload_type)
                l_tpn_soap_message.set_channel_name(l_row.endpoint)
                l_tpn_soap_message.set_uri(p_endpoint)
                l_tpn_soap_message.set_retry_count(l_row.retry_count)
                l_tpn_soap_message.set_state(l_row.status)
                l_tpn_soap_message.set_post_date(l_row.post_dt)
                l_tpn_soap_message.set_tpn_internal_unique_id(l_row.tpn_internal_unique_id)
                l_tpn_soap_message.set_method(c().GC_REQUEST_METHOD)
                l_tpn_soap_message.set_header(PC_HEADER_NAME_USER_AGENT, c().GC_USER_AGENT)
                l_tpn_soap_message.set_header(PC_HEADER_NAME_ACCEPT_LANGUAGE, c().GC_ACCEPT_LANGUAGE)
                l_tpn_soap_message.set_header(PC_HEADER_NAME_ACCEPT_CONTENT_TYPE, c().GC_CONTENT_TYPE)
                try {
                    T_middleware_sender.set_soft(GC_TRUE)
                    I_http_message l_http_response = T_middleware_sender.send_http_request(l_tpn_soap_message)
                    Integer l_response_code = l_http_response.get_status()
                    if (l_response_code == GC_HTTP_RESP_CODE_OK) {
                        sql_update("update messages set status=?, response_payload=? where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", GC_STATUS_DELIVERED, l_http_response.toString())
                    } else if (l_response_code == GC_RESPONSE_CODE_CONNECTION_REFUSED) {
                        l().log_warning(s.Connection_refused_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_row.tpn_internal_unique_id, l_row.txn_id)
                        sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1, response_payload=? where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", GC_STATUS_FAILED_NO_CONNECTION, l_http_response.toString())
                    } else if (l_response_code == GC_RESPONSE_CODE_INVALID_REQUEST) {
                        l().log_warning(s.Invalid_Request_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_row.tpn_internal_unique_id, l_row.txn_id)
                        sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1, response_payload=? where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", GC_STATUS_FAILED_INVALID_REQUEST, l_http_response.toString())
                    } else if (l_response_code == GC_RESPONSE_CODE_INVALID_RESPONSE) {
                        l().log_warning(s.Invalid_Response_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_row.tpn_internal_unique_id, l_row.txn_id)
                        sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1, response_payload=? where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", GC_STATUS_FAILED_INVALID_RESPONSE, l_http_response.toString())
                    } else {
                        l().log_warning(s.Unsuccessful_HTTP_Response_Code_Z1_for_message_TPN_ID_Z2_CoreCard_ID_Z3, l_response_code, l_row.tpn_internal_unique_id, l_row.txn_id)
                        sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1, response_payload=? where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", GC_STATUS_FAILED_RESPONSE, l_http_response.toString())
                    }
                } catch (Throwable e_others) {
                    l().log_warning(s.Exception_Z1_for_message_TPN_ID_Z2_CoreCard_ID_Z3, e_others, l_row.tpn_internal_unique_id, l_row.txn_id)
                    sql_update("update messages set status=?, retry_count=ifnull(retry_count, 0) + 1 where tpn_internal_unique_id=\"${l_tpn_soap_message.get_tpn_internal_unique_id()}\"", GC_STATUS_EXCEPTION)
                }
            }
            commit()
            l().log_debug(s.Sleeping_Z1_milliseconds, c().GC_CYCLE_INTERVAL_MILLISECONDS)
            sleep(Integer.valueOf(c().GC_CYCLE_INTERVAL_MILLISECONDS))
        }
    }

    @I_black_box("error")
    String get_channel_name() {
        return p_channel_name
    }

    @I_black_box("error")
    Integer get_thread_number() {
        return p_thread_number
    }

    @I_black_box("error")
    String get_conf_file_name() {
        return p_conf_file_name
    }

    @I_black_box("error")
    String get_mode() {
        return p_mode
    }

}
