package com.zhang.http.model;

import java.util.Map;

public class ApplicationContext {
    private Map<String, Router> routers;

    public ApplicationContext(Map<String, Router> routers) {
        this.routers = routers;
    }

    public Map<String, Router> getRouters() {
        return routers;
    }
}
