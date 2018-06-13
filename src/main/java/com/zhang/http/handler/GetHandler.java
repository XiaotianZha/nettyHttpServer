package com.zhang.http.handler;

import com.zhang.http.request.GetRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GetHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof GetRequest) {

        } else {
            super.channelRead(ctx, msg);
        }
    }
}
