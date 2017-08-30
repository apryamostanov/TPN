package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_message
import com.a9ae0b01f0ffc.middleware.implementation.T_http_sender
import com.a9ae0b01f0ffc.middleware.implementation.T_soap2rest_automated_converter
import com.a9ae0b01f0ffc.middleware.main.T_middleware_base_4_const
import com.a9ae0b01f0ffc.middleware.main.T_middleware_base_6_util
import groovy.time.TimeCategory
import org.json.JSONException

import java.text.SimpleDateFormat
import java.util.concurrent.LinkedBlockingQueue

import static com.a9ae0b01f0ffc.tpn.main.T_tpn_base_6_util.*

@I_fix_variable_scopes
class T_tpn_channel_worker_thread extends Thread {

    static final String PC_SQL_UPDATE_SENDING = "update messages set status=?, thread_number=?, send_time=now() where tpn_internal_unique_id=?"
    static final String PC_SQL_UPDATE_SUCCESS = "update messages set status=?, response_payload=?, receive_time=now() where tpn_internal_unique_id=?"
    static final String PC_SQL_UPDATE_FAIL = "update messages set status=?, retry_count=ifnull(retry_count, 0) + 1, response_payload=?, receive_time=now() where tpn_internal_unique_id=?"
    String p_channel_name = GC_EMPTY_STRING
    String p_endpoint = GC_EMPTY_STRING
    Integer p_thread_number = GC_ZERO
    String p_worker_conf_file_name = GC_EMPTY_STRING
    String p_mode = GC_EMPTY_STRING
    LinkedBlockingQueue<T_tpn_http_message> p_tpn_http_message_queue = new LinkedBlockingQueue<T_tpn_http_message>()

    @I_black_box("error")
    T_tpn_channel_worker_thread(String i_channel_name, String i_endpoint, Integer i_thread_number, String i_mode, String i_worker_conf_file_name) {
        super.setName(GC_WORKER_TPN_THREAD_NAME_PREFIX + GC_UNDERSCORE + i_channel_name + GC_UNDERSCORE + i_thread_number.toString())
        init(i_channel_name, i_endpoint, i_thread_number, i_mode, i_worker_conf_file_name)
    }

    @I_black_box("error")
    void init(String i_channel_name, String i_endpoint, Integer i_thread_number, String i_mode, String i_worker_conf_file_name) {
        p_channel_name = i_channel_name
        p_endpoint = i_endpoint
        p_thread_number = i_thread_number
        p_mode = i_mode
        p_worker_conf_file_name = i_worker_conf_file_name
        JSONException l_json_exception = new JSONException("null")
    }

    @I_black_box("error")
    void run_with_logging() {
        while (GC_TRUE) {
            while (not(p_tpn_http_message_queue.isEmpty())) {
                process_message(p_tpn_http_message_queue.poll())
            }
            synchronized (this) {
                wait()
            }
        }
    }

    @Override
    void run() {
        init_custom(p_worker_conf_file_name)
        run_with_logging()
    }

    @I_black_box
    Boolean is_obsolete(T_tpn_standard_message_format i_tpn_standard_message_format) {
        Boolean l_is_obsolete = GC_FALSE
        if (is_not_null(c().GC_TPN_MAX_MESSAGE_AGE)) {
            Date l_posting_date = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").parse(i_tpn_standard_message_format.transactionPostingDate + GC_SPACE + i_tpn_standard_message_format.transactionPostingTime)
            Date l_current_date = new Date()
            Date l_min_acceptable_date = l_posting_date
            use(TimeCategory) { l_min_acceptable_date = l_current_date - (new Integer(c().GC_TPN_MAX_MESSAGE_AGE)).seconds }
            if (l_min_acceptable_date > l_posting_date) {
                l_is_obsolete = GC_TRUE
            }
        }
        return l_is_obsolete
    }

    @I_black_box("error")
    void process_message(T_tpn_http_message i_tpn_standard_xml_http_message) {
        try {
            T_http_sender.set_soft(GC_TRUE)
            T_tpn_http_message l_tpn_http_message_to_send = i_tpn_standard_xml_http_message
            i_tpn_standard_xml_http_message.set_payload_type(T_middleware_base_4_const.GC_PAYLOAD_TYPE_XML)
            if (T_middleware_base_6_util.validate_xml(l_tpn_http_message_to_send.get_payload(), GC_TRUE)) {
                i_tpn_standard_xml_http_message.set_service_name(c().GC_TPN_SERVICE_NAME)
                inject_header(l_tpn_http_message_to_send)
                i_tpn_standard_xml_http_message.set_tpn_standard_message_format(parse_payload(i_tpn_standard_xml_http_message))
                if (is_obsolete(i_tpn_standard_xml_http_message.get_tpn_standard_message_format())) {
                    l().log_warning(s.Obsolete_message_TPN_ID_Z1_CoreCard_ID_Z2, i_tpn_standard_xml_http_message.p_tpn_internal_unique_id, i_tpn_standard_xml_http_message.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_OBSOLETE, GC_EMPTY_STRING, i_tpn_standard_xml_http_message.p_tpn_internal_unique_id)
                    return
                }
                if (c().GC_USE_CONVERSION_TEMPLATES == GC_TRUE_STRING) {
                    l_tpn_http_message_to_send = new T_tpn_conversion_module().convert_http_message(l_tpn_http_message_to_send) as T_tpn_http_message
                } else if (c().GC_PAYLOAD_TYPE == T_middleware_base_4_const.GC_PAYLOAD_TYPE_JSON) {
                    HashMap<String, I_http_message> l_messages_map = new HashMap<String, I_http_message>()
                    l_messages_map.put(c().GC_TPN_SERVICE_NAME, i_tpn_standard_xml_http_message)
                    l_tpn_http_message_to_send.set_payload(new T_soap2rest_automated_converter().convert_http_messages(l_messages_map, c().GC_TPN_SERVICE_NAME).get_payload())
                }
                sql_update(PC_SQL_UPDATE_SENDING, GC_STATUS_SENDING, p_thread_number, l_tpn_http_message_to_send.p_tpn_internal_unique_id)
                l().log_info(s.Sending_message_Z1, l_tpn_http_message_to_send.get_tpn_internal_unique_id())
                l_tpn_http_message_to_send.set_payload_type(c().GC_PAYLOAD_TYPE)
                I_http_message l_http_response = T_http_sender.send_http_request(l_tpn_http_message_to_send)
                Integer l_response_code = l_http_response.get_status()
                if (l_response_code == GC_HTTP_RESP_CODE_OK) {
                    sql_update(PC_SQL_UPDATE_SUCCESS, GC_STATUS_DELIVERED, serialize_for_db(l_http_response), l_tpn_http_message_to_send.p_tpn_internal_unique_id)
                } else if (l_response_code == GC_RESPONSE_CODE_CONNECTION_REFUSED) {
                    l().log_warning(s.Connection_refused_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_tpn_http_message_to_send.p_tpn_internal_unique_id, l_tpn_http_message_to_send.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_NO_CONNECTION, serialize_for_db(l_http_response), l_tpn_http_message_to_send.p_tpn_internal_unique_id)
                } else if (l_response_code == GC_RESPONSE_CODE_INVALID_REQUEST) {
                    l().log_warning(s.Invalid_Request_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_tpn_http_message_to_send.p_tpn_internal_unique_id, l_tpn_http_message_to_send.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_INVALID_REQUEST, serialize_for_db(l_http_response), l_tpn_http_message_to_send.p_tpn_internal_unique_id)
                } else if (l_response_code == GC_RESPONSE_CODE_INVALID_RESPONSE) {
                    l().log_warning(s.Invalid_Response_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_tpn_http_message_to_send.p_tpn_internal_unique_id, l_tpn_http_message_to_send.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_INVALID_RESPONSE, serialize_for_db(l_http_response), l_tpn_http_message_to_send.p_tpn_internal_unique_id)
                } else {
                    l().log_warning(s.Unsuccessful_HTTP_Response_Code_Z1_for_message_TPN_ID_Z2_CoreCard_ID_Z3, l_response_code, l_tpn_http_message_to_send.p_tpn_internal_unique_id, l_tpn_http_message_to_send.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_RESPONSE, serialize_for_db(l_http_response), l_tpn_http_message_to_send.p_tpn_internal_unique_id)
                }
            } else {
                l().log_warning(s.Invalid_Request_for_message_TPN_ID_Z1_CoreCard_ID_Z2, i_tpn_standard_xml_http_message.p_tpn_internal_unique_id, i_tpn_standard_xml_http_message.p_trxn_id)
                sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_INVALID_REQUEST, GC_EMPTY_STRING, i_tpn_standard_xml_http_message.p_tpn_internal_unique_id)
            }
        } catch (Throwable e_others) {
            l().log_warning(s.Exception_Z1_for_message_TPN_ID_Z2_CoreCard_ID_Z3, e_others, i_tpn_standard_xml_http_message.p_tpn_internal_unique_id, i_tpn_standard_xml_http_message.p_trxn_id)
            sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_EXCEPTION, GC_EMPTY_STRING, i_tpn_standard_xml_http_message.p_tpn_internal_unique_id)
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
        return p_worker_conf_file_name
    }

    @I_black_box("error")
    String get_mode() {
        return p_mode
    }

}
