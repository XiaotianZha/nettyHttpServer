package com.zhang.http.model;

public class Router {

    private final Class<?> clazz;

    private final String method;

    private final Object instance;

    public Router(Class<?> clazz, String method, Object instance) {
        this.clazz = clazz;
        this.method = method;
        this.instance=instance;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getMethod() {
        return method;
    }

    public Object getInstanse() {
        return instance;
    }

    @Override
    public String toString() {
        return "Router{" +
                "clazz=" + clazz +
                ", method='" + method + '\'' +
                '}';
    }
}
