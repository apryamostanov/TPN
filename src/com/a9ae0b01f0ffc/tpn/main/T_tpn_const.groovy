package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.commons.main.T_common_const

class T_tpn_const extends T_common_const{

    final static String GC_MODE_NORMAL = "normal"
    final static String GC_MODE_RETRY = "retry"
    final static String GC_STATUS_NEW = "new"
    final static String GC_STATUS_DELIVERED = "delivered"
    final static String GC_STATUS_FAILED_NO_CONNECTION = "no_connection"
    final static String GC_STATUS_FAILED_INVALID_RESPONSE = "invalid_response"
    final static String GC_STATUS_FAILED_INVALID_REQUEST = "invalid_request"
    final static String GC_STATUS_FAILED_RESPONSE = "failed_response"
    final static String GC_STATUS_EXCEPTION = "error"
    final static Integer GC_RESPONSE_CODE_INVALID_RESPONSE = -1
    final static Integer GC_RESPONSE_CODE_INVALID_REQUEST = -2
    final static Integer GC_RESPONSE_CODE_CONNECTION_REFUSED = -3

}
