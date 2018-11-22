package com.zhang.http.request;

import java.util.HashMap;
import java.util.Map;

public class HttPRequest {

    private Map<String,String> params = new HashMap<>();

    public HttPRequest(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
