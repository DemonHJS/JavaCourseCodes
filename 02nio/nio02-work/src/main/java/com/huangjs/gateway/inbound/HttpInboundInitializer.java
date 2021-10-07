package com.huangjs.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

/**
 * @Author: HuangFu
 * @Date: 2021/9/8 8:34
 * @Description:
 */
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private List<String> proxyServer;

    public HttpInboundInitializer(List<String> proxyServer) {
        this.proxyServer = proxyServer;
    }

    /**
     * 初始话
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpServerCodec());
        //p.addLast(new HttpServerExpectContinueHandler());
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        p.addLast(new HttpInboundHandler(this.proxyServer));
    }
}
