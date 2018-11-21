package com.zhang.http.handler;

import com.zhang.http.request.GetRequest;
import com.zhang.http.request.PostRequest;
import com.zhang.http.util.ParamUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DispatchHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static Logger logger = LoggerFactory.getLogger(DispatchHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        HttpMethod method = msg.getMethod();
        String rawUri = msg.getUri();
        int start = rawUri.indexOf("/");
        int end = rawUri.indexOf("?");
        int tail = end == -1 ? rawUri.length() : end;
        String uri = rawUri.substring(start, tail);
        boolean keepAlive = HttpHeaders.isKeepAlive(msg);
        if (method.equals(HttpMethod.GET)) {
            GetRequest getRequest = new GetRequest();

            logger.debug(rawUri);

            getRequest.setUri(uri);
            getRequest.setKeepAlive(keepAlive);
            if (end != -1) {
                String params = rawUri.substring(end+1);
                getRequest.setParams(ParamUtil.processGet(params));
            }
            ctx.fireChannelRead(getRequest);
        } else if (method.equals(HttpMethod.POST)) {
            ByteBuf jsonBuf = msg.content();
            String contentType=msg.headers().get(HttpHeaders.Names.CONTENT_TYPE);

            logger.debug("content-type: "+contentType);
            String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
            PostRequest post = new PostRequest();
            post.setContentType(contentType);
            post.setKeepAlive(keepAlive);
            post.setUri(uri);
            post.setRawStr(jsonStr);
            ctx.fireChannelRead(post);
        } else {
//            FullHttpResponse
        }

    }


}
