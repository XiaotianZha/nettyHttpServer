package com.zhang.http;

import com.zhang.http.model.ApplicationContext;

public class ContextHolder {

    private static ApplicationContext context = null;

    public ContextHolder(ApplicationContext applicationContext) {
        System.out.println("setContext");
        context=applicationContext;
    }

    public static ApplicationContext getContext(){
        return  context;
    }
}
