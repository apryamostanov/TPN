package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.main.T_common_context
import groovy.sql.Sql

class T_tpn_context {

    protected static ThreadLocal<T_tpn_context> p_context_thread_local = new ThreadLocal<T_tpn_context>()
    private Sql p_sql = T_tpn_const.GC_NULL_OBJ_REF as Sql
    private T_tpn_commons p_commons = T_tpn_const.GC_NULL_OBJ_REF as T_tpn_commons

    private static void check_init() {
        if (p_context_thread_local.get() == T_tpn_const.GC_NULL_OBJ_REF) {
            p_context_thread_local.set(new T_tpn_context())
        }
    }

    void init_custom(String i_commons_conf_file_name) {
            check_init()
            p_context_thread_local.get().p_commons = new T_tpn_commons(i_commons_conf_file_name)
            T_common_context.get_context().init_custom(i_commons_conf_file_name)
            T_s.x().init_custom(T_tpn_s.c().GC_BLACK_BOX_CONFIG)
    }

    private static void check_init_sql() {
        if (get_context().p_sql == T_tpn_const.GC_NULL_OBJ_REF) {
            get_context().p_sql = Sql.newInstance(T_tpn_s.c().GC_MYSQL_CONNECTION_STRING, T_tpn_s.c().GC_MYSQL_USERNAME, T_tpn_s.c().GC_MYSQL_PASSWORD, T_tpn_s.c().GC_MYSQL_DRIVER)
            get_context().p_sql.getConnection().setAutoCommit(T_tpn_const.GC_FALSE)
        }
    }

    T_tpn_commons get_commons() {
        check_init()
        return p_context_thread_local.get().p_commons
    }

    static T_tpn_context get_context() {
        check_init()
        return p_context_thread_local.get()
    }

    @I_black_box
    void sql_update(String i_sql_string, String... i_bind_variables = T_tpn_const.GC_SKIPPED_ARGS) {
        check_init_sql()
        T_s.l().log_sql("update", i_sql_string, i_bind_variables)
        if (T_u.method_arguments_present(i_bind_variables)) {
            p_sql.executeUpdate(i_sql_string, i_bind_variables)
        } else {
            p_sql.executeUpdate(i_sql_string)
        }
    }

    Sql get_sql() {
        check_init_sql()
        return p_sql
    }

}
