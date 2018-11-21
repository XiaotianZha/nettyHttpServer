package com.zhang.http.request;

import java.util.HashMap;
import java.util.Map;

public class PostRequest {

    private String uri;

    private Map<String,String> params = new HashMap<>();

    private boolean isKeepAlive;

    private String rawStr;

    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

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

    public boolean isKeepAlive() {
        return isKeepAlive;
    }

    public String getRawStr() {
        return rawStr;
    }

    public void setRawStr(String rawStr) {
        this.rawStr = rawStr;
    }

    public void setKeepAlive(boolean keepAlive) {
        isKeepAlive = keepAlive;
    }
}
