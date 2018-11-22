package com.zhang.http;

import com.zhang.http.request.HttPRequest;

public class HttpRequestHolder {

    private static ThreadLocal<HttPRequest> holder = new ThreadLocal<>();

    public static void setHolder(HttPRequest request){
        holder.set(request);
    }

    public static HttPRequest getHttpRequest(){
        return  holder.get();
    }

    public static void clear(){
        holder.remove();
    }
}
