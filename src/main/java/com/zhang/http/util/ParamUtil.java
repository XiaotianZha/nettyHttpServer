package com.zhang.http.util;

import java.util.HashMap;
import java.util.Map;

public class ParamUtil {

    public static Map<String, String> processGet(String params) {
        Map<String, String> map = new HashMap<>();
        String[] couples = params.split("&");
        for (String para : couples) {
            String[] kv = para.split("=");
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    public static Map<String, String> processPost(String params) {
        Map<String, String> map = new HashMap<>();
//        @SuppressWarnings("unchecked")
        map=JsonUtil.fromJson(params,Map.class);
        return map;
    }
}
