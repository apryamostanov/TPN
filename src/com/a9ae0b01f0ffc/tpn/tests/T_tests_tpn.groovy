package com.a9ae0b01f0ffc.tpn.tests

import com.a9ae0b01f0ffc.tpn.implementation.T_tpn
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s
import org.junit.Test

class T_tests_tpn {

    final String PC_CONF_FILE_NAME = "./src/com/a9ae0b01f0ffc/tpn/conf/commons.conf"

    @Test
    void test_001() {
        T_tpn_s.x().init_custom(PC_CONF_FILE_NAME)
        new T_tpn().send()
    }

}
