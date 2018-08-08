package com.zhang.http.handler;

import com.zhang.http.request.GetRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class DispatchHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        HttpMethod method = msg.getMethod();
        boolean keepAlive = HttpHeaders.isKeepAlive(msg);
        if (method.equals(HttpMethod.GET)) {
            GetRequest getRequest = new GetRequest();
            String rawUri = msg.getUri();
            System.out.println(rawUri);
            int start = rawUri.indexOf("/");
            int end = rawUri.indexOf("?");
            int tail = end == -1 ? rawUri.length() : end;
            String uri = rawUri.substring(start, tail);
            getRequest.setUri(uri);
            getRequest.setKeepAlive(keepAlive);
            if (end != -1) {
                String params = rawUri.substring(end+1);
                getRequest.setParams(this.processGet(params));
            }
            ctx.fireChannelRead(getRequest);
        } else if (method.equals(HttpMethod.POST)) {

        } else {
//            FullHttpResponse
        }

    }

    private Map<String, String> processGet(String params) {
        Map<String, String> map = new HashMap<>();
        String[] couples = params.split("&");
        for (String para : couples) {
            String[] kv = para.split("=");
            map.put(kv[0], kv[1]);
        }
        return map;
    }
}
