package com.zhang.http.request;

import java.util.HashMap;
import java.util.Map;

public class GetRequest {

    private String uri;

    private Map<String,String> params = new HashMap<>();

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
