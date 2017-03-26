package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.commons.implementation.exceptions.E_application_exception
import com.a9ae0b01f0ffc.tpn.main.T_tpn_base_6_util

@I_fix_variable_scopes
class T_round_robin<T_type> extends T_tpn_base_6_util implements Iterable<T_type> {

    private List<T_type> p_list

    @I_black_box
    T_round_robin(List<T_type> i_list) {
        p_list = i_list
    }

    @Override
    @I_black_box
    Iterator<T_type> iterator() {
        return new Iterator<T_type>() {

            private Integer lp_index = GC_ZERO

            @Override
            boolean hasNext() {
                return GC_TRUE
            }

            @Override
            T_type next() {
                T_type l_result = p_list.get(lp_index)
                lp_index = (lp_index + GC_ONE_ONLY) % p_list.size()
                return l_result
            }

            @Override
            void remove() {
                throw new E_application_exception(s.Unable_to_remove_from_Round_Robin)
            }

        }
    }

}