package com.zhang.http.handler;

import com.zhang.http.exception.ResourcesNotFoundException;
import com.zhang.http.request.GetRequest;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;

public class GetHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("get Handler");
        if (msg instanceof GetRequest) {
            GetRequest getRequest = (GetRequest)msg;
            URL location =GetHandler.class.getProtectionDomain()
                    .getCodeSource().getLocation();
            String path =location.toURI()+getRequest.getUri().substring(1);
            path=!path.contains("file:")?path:path.substring(5);
            try {
                RandomAccessFile file = new RandomAccessFile(new File(path),"r");
                HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/html; charset=UTF-8");
                boolean keepAlive = getRequest.isKeepAlive();
                if (keepAlive){
                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,file.length());
                    response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
                }
                ctx.write(response);
                if (ctx.pipeline().get(SslHandler.class)==null){
                    ctx.write(new DefaultFileRegion(file.getChannel(),0,file.length()));
                }else {
                    ctx.write(new ChunkedNioFile(file.getChannel()));
                }
                ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                if (!keepAlive){
                    future.addListener(ChannelFutureListener.CLOSE);
                }
            }catch (IOException e){
                System.out.println("file not found "+path);
                throw new ResourcesNotFoundException(path);
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
