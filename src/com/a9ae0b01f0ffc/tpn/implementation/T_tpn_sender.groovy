package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s

import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.KeyManager
import javax.net.ssl.SSLContext
import java.security.SecureRandom

class T_tpn_sender {

    static final String PC_HEADER_NAME_USER_AGENT = "User-Agent"
    static final String PC_HEADER_NAME_ACCEPT_LANGUAGE = "Accept-Language"
    static final String PC_HEADER_NAME_ACCEPT_CONTENT_TYPE = "Content-Type"

    @I_black_box("error")
    Boolean validate_xml(String i_xml) {
        try {
            String l_formatted_payload = i_xml
            Node l_node = T_tpn_const.GC_NULL_OBJ_REF as Node
            l_node = new XmlParser().parseText(l_formatted_payload)
            StringWriter l_string_writer = new StringWriter()
            XmlNodePrinter l_xml_node_printer = new XmlNodePrinter(new PrintWriter(l_string_writer))
            l_xml_node_printer.print(l_node)
            l_formatted_payload = l_string_writer.toString()
            return T_tpn_const.GC_TRUE
        } catch (Exception e_others) {
            T_s.l().log_warning(T_s.s().Potentially_invalid_XML, T_s.r(i_xml, "problematic_payload"))
            return T_tpn_const.GC_FALSE
        }
    }

    @I_black_box
    Integer send_soap(T_tpn_soap_message i_tpn_soap_message_body) throws Exception {
        if (!validate_xml(i_tpn_soap_message_body.get_payload())) {
            return T_tpn_const.GC_RESPONSE_CODE_INVALID_REQUEST
        }
        if (T_tpn_s.c().GC_UNSERCURE_TEST_TLS_SSL_MODE == T_tpn_const.GC_TRUE_STRING) {
            T_s.l().log_warning(T_s.s().UNSECURE_TEST_TLS_MODE_IS_USED)
            T_s.l().log_warning(T_s.s().DO_NOT_USE_ON_PRODUCTION)
            SSLContext l_ssl_context = SSLContext.getInstance("TLS")
            T_test_trust_manager[] l_test_trust_managers = new T_test_trust_manager()
            l_ssl_context.init(T_tpn_const.GC_NULL_OBJ_REF as KeyManager[], l_test_trust_managers, T_tpn_const.GC_NULL_OBJ_REF as SecureRandom)
            HttpsURLConnection.setDefaultSSLSocketFactory(l_ssl_context.getSocketFactory())
        }
        URL l_url = new URL(i_tpn_soap_message_body.get_endpoint())
        HttpsURLConnection l_https_url_connection = (HttpsURLConnection) l_url.openConnection()
        if (T_tpn_s.c().GC_UNSERCURE_TEST_TLS_SSL_MODE == T_tpn_const.GC_TRUE_STRING) {
            l_https_url_connection.setHostnameVerifier(new T_host_name_verifier())
        }
        l_https_url_connection.setRequestMethod(T_tpn_s.c().GC_REQUEST_METHOD)
        l_https_url_connection.setRequestProperty(PC_HEADER_NAME_USER_AGENT, T_tpn_s.c().GC_USER_AGENT)
        l_https_url_connection.setRequestProperty(PC_HEADER_NAME_ACCEPT_LANGUAGE, T_tpn_s.c().GC_ACCEPT_LANGUAGE)
        l_https_url_connection.setRequestProperty(PC_HEADER_NAME_ACCEPT_CONTENT_TYPE, T_tpn_s.c().GC_CONTENT_TYPE)
        l_https_url_connection.setDoOutput(T_tpn_const.GC_TRUE)
        DataOutputStream l_data_output_stream
        try {
            l_data_output_stream = new DataOutputStream(l_https_url_connection.getOutputStream())
        } catch (ConnectException e_connection_refused) {
            return T_tpn_const.GC_RESPONSE_CODE_CONNECTION_REFUSED
        }
        l_data_output_stream.writeBytes(i_tpn_soap_message_body.get_payload())
        l_data_output_stream.flush()
        l_data_output_stream.close()
        T_s.l().log_send(i_tpn_soap_message_body.get_payload())
        Integer l_response_code = l_https_url_connection.getResponseCode()
        InputStream l_input_stream = get_input_stream(l_https_url_connection)
        BufferedReader l_buffered_reader = new BufferedReader(new InputStreamReader(l_input_stream))
        String l_buffered_reader_line
        StringBuffer l_response = new StringBuffer()
        while ((l_buffered_reader_line = l_buffered_reader.readLine()) != T_tpn_const.GC_NULL_OBJ_REF) {
            l_response.append(l_buffered_reader_line)
        }
        l_buffered_reader.close()
        T_s.l().log_receive(l_response)
        if (!validate_xml(l_response.toString())) {
            return T_tpn_const.GC_RESPONSE_CODE_INVALID_RESPONSE
        }
        return l_response_code
    }

    @I_black_box
    InputStream get_input_stream(HttpURLConnection i_http_url_connection) {
        InputStream l_input_stream = T_tpn_const.GC_NULL_OBJ_REF as InputStream
        if (i_http_url_connection.getErrorStream() == T_tpn_const.GC_NULL_OBJ_REF) {
            if (i_http_url_connection.getResponseCode() == T_tpn_const.GC_HTTP_RESP_CODE_OK) {
                l_input_stream = i_http_url_connection.getInputStream()
            }
        } else {
            l_input_stream = i_http_url_connection.getErrorStream()
        }
        return l_input_stream
    }

}
