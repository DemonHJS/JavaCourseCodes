package com.huangjs.gateway.outbound.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author: HuangFu
 * @Date: 2021/9/27 8:15
 * @Description:
 */
public class NettyHttpClient {


    public void connect(String host,Integer port) throws InterruptedException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.group(workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            p.addLast(new HttpServerCodec());
                            //p.addLast(new HttpServerExpectContinueHandler());
                            p.addLast(new HttpObjectAggregator(1024 * 1024));
                            p.addLast(new NettyHttpClientInboundHandler());
                        }
                    });
            Channel ch = b.bind(host,port).sync().channel();
            ch.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }




}
