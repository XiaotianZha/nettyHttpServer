package com.zhang.http.handler;

import com.zhang.http.model.PostResponse;
import com.zhang.http.request.PostRequest;
import com.zhang.http.util.JsonUtil;
import com.zhang.http.util.ParamUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

public class PostHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("post handler");
        if(msg instanceof PostRequest){
            PostRequest request =(PostRequest)msg;
            System.out.println(request.getRawStr());
            Map<String,String> params= this.processParam(request.getContentType(),request.getRawStr());
            System.out.println("toMap: "+params);
            PostResponse<String> result = new PostResponse<>();
            result.setCode(200);
            result.setContent("success");
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            ctx.write(response);
            ctx.write(Unpooled.copiedBuffer(JsonUtil.toJson(result), CharsetUtil.UTF_8));
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            future.addListener(ChannelFutureListener.CLOSE);
        }else{
            super.channelRead(ctx, msg);
        }
    }

    private Map<String,String> processParam(String contentType,String rawPara){
        Map<String,String> params = new HashMap<>();
        if(contentType.contains("application/x-www-form-urlencoded")){
            params= ParamUtil.processGet(rawPara);
        }
        if (contentType.contains("application/json")){
            params=ParamUtil.processPost(rawPara);
        }
        return params;
    }
}
