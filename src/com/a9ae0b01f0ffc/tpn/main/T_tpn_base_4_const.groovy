package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.commons.implementation.main.T_common_base_3_utils

class T_tpn_base_4_const extends T_common_base_3_utils{

    public final static String GC_MODE_NORMAL = "normal"
    public final static String GC_MODE_RETRY = "retry"
    public final static String GC_STATUS_NEW = "new"
    public final static String GC_STATUS_DELIVERED = "delivered"
    public final static String GC_STATUS_FAILED_NO_CONNECTION = "no_connection"
    public final static String GC_STATUS_FAILED_INVALID_RESPONSE = "invalid_response"
    public final static String GC_STATUS_FAILED_INVALID_REQUEST = "invalid_request"
    public final static String GC_STATUS_FAILED_RESPONSE = "failed_response"
    public final static String GC_STATUS_EXCEPTION = "error"
    public final static String GC_STATUS_WAITING_FOR_PROCESSING = "waiting"
    public final static String GC_STATUS_DUPLICATE = "duplicate"
    public final static String GC_STATUS_RENEWED = "renewed"
    public final static Integer GC_RESPONSE_CODE_INVALID_RESPONSE = -1
    public final static Integer GC_RESPONSE_CODE_INVALID_REQUEST = -2
    public final static Integer GC_RESPONSE_CODE_CONNECTION_REFUSED = -3
    public final static String GC_MAIN_TPN_THREAD_NAME = "TPN_MAIN"
    public final static String GC_MASTER_TPN_THREAD_NAME_PREFIX = "TPN_MASTER_"
    public final static String GC_WORKER_TPN_THREAD_NAME_PREFIX = "TPN_WORKER_"
    public final static Integer GC_MYSQL_VARCHAR_LIMIT = 3999
    public final static String GC_UNIQUE_ID_FLAG_RETRY = "1"
    public final static String GC_UNIQUE_ID_FLAG_NORMAL = "0"

}
