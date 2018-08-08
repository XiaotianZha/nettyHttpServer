package com.zhang.http.handler;

import com.zhang.http.exception.ResourcesNotFoundException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class NotFoundHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        if (cause instanceof ResourcesNotFoundException){
            URL url=Thread.currentThread().getContextClassLoader().getResource("404.html");
            url.toURI();
        }
        super.exceptionCaught(ctx, cause);
    }

    public static void main(String[] args) throws Exception {
        URL url=Thread.currentThread().getContextClassLoader().getResource("404.html");
        System.out.println(url.toURI());
        System.out.println(url.toString());
        RandomAccessFile f = new RandomAccessFile(new File(url.toString()),"r");
        f.getChannel();
    }
}
