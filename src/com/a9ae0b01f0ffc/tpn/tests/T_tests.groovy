package com.a9ae0b01f0ffc.tpn.tests

import com.a9ae0b01f0ffc.commons.implementation.round_robin.T_round_robin
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_channel_worker_thread
import org.junit.Test

import static com.a9ae0b01f0ffc.commons.implementation.main.T_common_base_1_const.GC_NULL_OBJ_REF

class T_tests {

    @Test
    void test_01() {
        ArrayList<String> l_strings = new ArrayList<String>()
        l_strings.add("1")
        l_strings.add("2")
        l_strings.add("3")
        T_round_robin<String> l_round_robin = new T_round_robin<String>(l_strings)
        while (true) {
            String z = ++l_round_robin.iterator()
            System.out.println(z)
        }
    }

}
