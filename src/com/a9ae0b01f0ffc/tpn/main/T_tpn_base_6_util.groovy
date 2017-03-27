package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.black_box.implementation.T_logger
import com.a9ae0b01f0ffc.black_box.main.T_logging_base_5_context
import groovy.sql.Sql

@I_fix_variable_scopes
class T_tpn_base_6_util extends T_tpn_base_5_context{

    @I_black_box
    static void sql_update(String i_sql_string, Object... i_bind_variables = GC_SKIPPED_ARGS) {
        l().log_send_sql("update", i_sql_string, i_bind_variables)
        if (method_arguments_present(i_bind_variables)) {
            get_sql().executeUpdate(i_sql_string, i_bind_variables)
        } else {
            get_sql().executeUpdate(i_sql_string)
        }
        l().log_receive_sql("updated", get_sql().getUpdateCount())
    }

    @I_black_box
    static void commit() {
        l().log_send_sql("commit")
        get_sql().commit()
        l().log_receive_sql("commit_finished")
    }

    @I_black_box
    static Sql get_sql() {
        return get_context().p_sql
    }

    @I_black_box("error")
    static T_logger l() {
        return T_logging_base_5_context.l()
    }

}
