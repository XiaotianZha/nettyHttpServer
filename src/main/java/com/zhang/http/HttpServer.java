package com.zhang.http;

import com.zhang.http.handler.DispatchHandler;
import com.zhang.http.handler.GetHandler;
import com.zhang.http.handler.PostHandler;
import com.zhang.http.model.Router;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.Map;

public class HttpServer {

    private Map<String, Router> rouers;

    private  EventLoopGroup group = new NioEventLoopGroup();

    public void init(int port) throws Exception{
        ClassPathXMLApplicationContext context = new ClassPathXMLApplicationContext("Package.xml");
        rouers = context.getRouters();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(64*1024));
                            ch.pipeline().addLast(new DispatchHandler());
                            ch.pipeline().addLast(new GetHandler());
                            ch.pipeline().addLast(new PostHandler());

                        }
                    });
            ChannelFuture f= bootstrap.bind().sync();
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        new HttpServer().init(8080);
    }




}
