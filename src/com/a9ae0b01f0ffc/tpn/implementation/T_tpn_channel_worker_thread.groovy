package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_message
import com.a9ae0b01f0ffc.middleware.implementation.T_http_sender
import com.a9ae0b01f0ffc.middleware.main.T_middleware_base_6_util
import com.a9ae0b01f0ffc.tpn.main.T_tpn_base_6_util

import java.util.concurrent.LinkedBlockingQueue

import static com.a9ae0b01f0ffc.tpn.main.T_tpn_base_6_util.*

@I_fix_variable_scopes
class T_tpn_channel_worker_thread extends Thread {
    
    static final String PC_SQL_UPDATE_SUCCESS = "update messages set status=?, response_payload=?, thread_number=?, send_time=now() where tpn_internal_unique_id=?"
    static final String PC_SQL_UPDATE_FAIL = "update messages set status=?, retry_count=ifnull(retry_count, 0) + 1, response_payload=?, thread_number=?, send_time=now() where tpn_internal_unique_id=?"
    String p_channel_name = GC_EMPTY_STRING
    String p_endpoint = GC_EMPTY_STRING
    Integer p_thread_number = GC_ZERO
    String p_worker_conf_file_name = GC_EMPTY_STRING
    String p_mode = GC_EMPTY_STRING
    LinkedBlockingQueue<T_tpn_http_message> p_tpn_http_message_queue = new LinkedBlockingQueue<T_tpn_http_message>()

    @I_black_box("error")//orig=
    T_tpn_channel_worker_thread(String i_channel_name, String i_endpoint, Integer i_thread_number, String i_mode, String i_worker_conf_file_name) {
        super.setName(GC_WORKER_TPN_THREAD_NAME_PREFIX + GC_UNDERSCORE + i_channel_name + GC_UNDERSCORE + i_thread_number.toString())
        init(i_channel_name, i_endpoint, i_thread_number, i_mode, i_worker_conf_file_name)
    }

    @I_black_box("error")//orig=
    void init(String i_channel_name, String i_endpoint, Integer i_thread_number, String i_mode, String i_worker_conf_file_name) {
        p_channel_name = i_channel_name
        p_endpoint = i_endpoint
        p_thread_number = i_thread_number
        p_mode = i_mode
        p_worker_conf_file_name = i_worker_conf_file_name
    }

    @I_black_box("error")//orig=
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

    @I_black_box("error")
    void process_message(T_tpn_http_message i_tpn_http_message) {
        try {
            i_tpn_http_message.set_tpn_standard_message_format(parse_payload(i_tpn_http_message.get_payload()))
            T_http_sender.set_soft(GC_TRUE)
            T_tpn_http_message l_tpn_http_message = i_tpn_http_message
            if (c().GC_USE_CONVERSION_TEMPLATES == GC_TRUE_STRING) {
                l_tpn_http_message = new T_tpn_conversion_module().convert_http_message(l_tpn_http_message) as T_tpn_http_message
            }
            if (T_middleware_base_6_util.validate_xml(l_tpn_http_message.get_payload(), GC_TRUE)) {
                I_http_message l_http_response = T_http_sender.send_http_request(l_tpn_http_message)
                Integer l_response_code = l_http_response.get_status()
                if (l_response_code == GC_HTTP_RESP_CODE_OK) {
                    sql_update(PC_SQL_UPDATE_SUCCESS, GC_STATUS_DELIVERED, substr(l_http_response.toString(), GC_FIRST_CHAR, GC_MYSQL_VARCHAR_LIMIT), p_thread_number, l_tpn_http_message.p_tpn_internal_unique_id)
                } else if (l_response_code == GC_RESPONSE_CODE_CONNECTION_REFUSED) {
                    l().log_warning(s.Connection_refused_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_tpn_http_message.p_tpn_internal_unique_id, l_tpn_http_message.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_NO_CONNECTION, substr(l_http_response.toString(), GC_FIRST_CHAR, GC_MYSQL_VARCHAR_LIMIT), p_thread_number, l_tpn_http_message.p_tpn_internal_unique_id)
                } else if (l_response_code == GC_RESPONSE_CODE_INVALID_REQUEST) {
                    l().log_warning(s.Invalid_Request_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_tpn_http_message.p_tpn_internal_unique_id, l_tpn_http_message.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_INVALID_REQUEST, substr(l_http_response.toString(), GC_FIRST_CHAR, GC_MYSQL_VARCHAR_LIMIT), p_thread_number, l_tpn_http_message.p_tpn_internal_unique_id)
                } else if (l_response_code == GC_RESPONSE_CODE_INVALID_RESPONSE) {
                    l().log_warning(s.Invalid_Response_for_message_TPN_ID_Z1_CoreCard_ID_Z2, l_tpn_http_message.p_tpn_internal_unique_id, l_tpn_http_message.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_INVALID_RESPONSE, substr(l_http_response.toString(), GC_FIRST_CHAR, GC_MYSQL_VARCHAR_LIMIT), p_thread_number, l_tpn_http_message.p_tpn_internal_unique_id)
                } else {
                    l().log_warning(s.Unsuccessful_HTTP_Response_Code_Z1_for_message_TPN_ID_Z2_CoreCard_ID_Z3, l_response_code, l_tpn_http_message.p_tpn_internal_unique_id, l_tpn_http_message.p_trxn_id)
                    sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_RESPONSE/*todo: change to "unknown"*/, substr(l_http_response.toString(), GC_FIRST_CHAR, GC_MYSQL_VARCHAR_LIMIT), p_thread_number, l_tpn_http_message.p_tpn_internal_unique_id)
                }
            } else {
                l().log_warning(s.Invalid_Request_for_message_TPN_ID_Z1_CoreCard_ID_Z2, i_tpn_http_message.p_tpn_internal_unique_id, i_tpn_http_message.p_trxn_id)
                sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_FAILED_INVALID_REQUEST, GC_EMPTY_STRING, p_thread_number, i_tpn_http_message.p_tpn_internal_unique_id)
            }
        } catch (Throwable e_others) {
            l().log_warning(s.Exception_Z1_for_message_TPN_ID_Z2_CoreCard_ID_Z3, e_others, i_tpn_http_message.p_tpn_internal_unique_id, i_tpn_http_message.p_trxn_id)
            sql_update(PC_SQL_UPDATE_FAIL, GC_STATUS_EXCEPTION, GC_EMPTY_STRING, p_thread_number, i_tpn_http_message.p_tpn_internal_unique_id)
        }
        commit()
    }

    @I_black_box("error")//orig=("error")
    String get_channel_name() {
        return p_channel_name
    }

    @I_black_box("error")//orig=("error")
    Integer get_thread_number() {
        return p_thread_number
    }

    @I_black_box("error")//orig=("error")
    String get_conf_file_name() {
        return p_worker_conf_file_name
    }

    @I_black_box("error")//orig=("error")
    String get_mode() {
        return p_mode
    }

}
