package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s
import com.a9ae0b01f0ffc.tpn.main.T_u
import com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl
import groovy.xml.XmlUtil
import sun.net.www.protocol.https.HttpsURLConnectionImpl

import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.KeyManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.X509Certificate


class T_tpn {

    static final String PC_HEADER_NAME_USER_AGENT = "User-Agent"
    static final String PC_HEADER_NAME_ACCEPT_LANGUAGE = "Accept-Language"
    static final String PC_HEADER_NAME_ACCEPT_CONTENT_TYPE = "Content-Type"

    @I_black_box
    void validate_tpn_soap_message_body(T_tpn_soap_message_body i_tpn_soap_message_body) {
        try {
            String l_serialized_xml = XmlUtil.serialize(i_tpn_soap_message_body.get_xml_string())
        } catch (Exception e_others) {
            throw new E_application_exception(T_s.s().Invalid_XML, e_others)
        }
    }

    @I_black_box
    void send_soap(T_tpn_soap_message_body i_tpn_soap_message_body) throws Exception {
        validate_tpn_soap_message_body(i_tpn_soap_message_body)
        if (T_tpn_s.c().GC_UNSERCURE_TEST_TLS_SSL_MODE == T_tpn_const.GC_TRUE_STRING) {
            T_s.l().log_warning(T_s.s().UNSECURE_TEST_TLS_MODE_IS_USED)
            T_s.l().log_warning(T_s.s().DO_NOT_USE_ON_PRODUCTION)
            SSLContext l_ssl_context = SSLContext.getInstance("TLS")
            T_test_trust_manager[] l_test_trust_managers = new T_test_trust_manager()
            l_ssl_context.init(T_tpn_const.GC_NULL_OBJ_REF as KeyManager[], l_test_trust_managers, T_tpn_const.GC_NULL_OBJ_REF as SecureRandom)
            HttpsURLConnection.setDefaultSSLSocketFactory(l_ssl_context.getSocketFactory())
        }
        URL l_url = new URL(T_tpn_s.c().GC_TPN_URL)
        HttpsURLConnection l_https_url_connection = (HttpsURLConnection) l_url.openConnection()
        if (T_tpn_s.c().GC_UNSERCURE_TEST_TLS_SSL_MODE == T_tpn_const.GC_TRUE_STRING) {
            l_https_url_connection.setHostnameVerifier(new T_host_name_verifier())
        }
        l_https_url_connection.setRequestMethod(T_tpn_s.c().GC_REQUEST_METHOD)
        l_https_url_connection.setRequestProperty(PC_HEADER_NAME_USER_AGENT, T_tpn_s.c().GC_USER_AGENT)
        l_https_url_connection.setRequestProperty(PC_HEADER_NAME_ACCEPT_LANGUAGE, T_tpn_s.c().GC_ACCEPT_LANGUAGE)
        l_https_url_connection.setRequestProperty(PC_HEADER_NAME_ACCEPT_CONTENT_TYPE, T_tpn_s.c().GC_CONTENT_TYPE)
        l_https_url_connection.setDoOutput(T_tpn_const.GC_TRUE)
        DataOutputStream l_data_output_stream = new DataOutputStream(l_https_url_connection.getOutputStream())
        l_data_output_stream.writeBytes(i_tpn_soap_message_body.get_xml_string())
        l_data_output_stream.flush()
        l_data_output_stream.close()
        T_s.l().log_send(i_tpn_soap_message_body.get_xml_string())
        Integer l_response_code = l_https_url_connection.getResponseCode()
        T_s.l().log_debug(T_s.s().Response_code_Z1, l_response_code)
        InputStream l_input_stream = get_input_stream(l_https_url_connection)
        BufferedReader l_buffered_reader = new BufferedReader(new InputStreamReader(l_input_stream))
        String l_buffered_reader_line
        StringBuffer l_response = new StringBuffer()
        while ((l_buffered_reader_line = l_buffered_reader.readLine()) != T_tpn_const.GC_NULL_OBJ_REF) {
            l_response.append(l_buffered_reader_line)
        }
        l_buffered_reader.close()
        T_s.l().log_receive(l_response)
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
