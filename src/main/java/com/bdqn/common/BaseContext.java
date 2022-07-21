package com.bdqn.common;

public class BaseContext {

    private static ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        THREAD_LOCAL.set(id);
    }

    public static Long getCurrentId(){
        return THREAD_LOCAL.get();
    }

}
