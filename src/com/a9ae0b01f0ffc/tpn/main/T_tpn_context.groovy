package com.a9ae0b01f0ffc.tpn.main


import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.commons.ioc.T_class_loader
import com.a9ae0b01f0ffc.commons.main.T_common_context

class T_tpn_context {

    protected static ThreadLocal<T_tpn_context> p_context_thread_local = new ThreadLocal<T_tpn_context>()
    private T_class_loader p_ioc = T_tpn_const.GC_NULL_OBJ_REF as T_class_loader
    private T_tpn_commons p_commons = T_tpn_const.GC_NULL_OBJ_REF as T_tpn_commons

    static {
        p_context_thread_local.set(new T_tpn_context())
    }

    void init_custom(String i_commons_conf_file_name) {
        p_context_thread_local.get().p_commons = new T_tpn_commons(i_commons_conf_file_name)
        p_context_thread_local.get().p_ioc = new T_class_loader(T_tpn_s.c().GC_TPN_CLASSES_CONF)
        T_common_context.get_context().init_custom(i_commons_conf_file_name)
        T_s.x().init_custom(T_tpn_s.c().GC_BLACK_BOX_CONFIG)
    }

    T_class_loader get_ioc() {
        return p_context_thread_local.get().p_ioc
    }

    T_tpn_commons get_commons() {
        return ((T_tpn_context) p_context_thread_local.get()).p_commons
    }

    static T_tpn_context get_context() {
        return p_context_thread_local.get()
    }
    
}
