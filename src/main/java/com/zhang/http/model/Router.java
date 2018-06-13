package com.zhang.http.model;

public class Router {

    private final Class<?> clazz;

    private final String method;

    public Router(Class<?> clazz, String method) {
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public String toString() {
        return "Router{" +
                "clazz=" + clazz +
                ", method='" + method + '\'' +
                '}';
    }
}
