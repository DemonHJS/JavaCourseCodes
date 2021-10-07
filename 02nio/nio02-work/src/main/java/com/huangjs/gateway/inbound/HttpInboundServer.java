package com.huangjs.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;

import java.util.List;

@Data
public class HttpInboundServer {

    private int port;
    
    private List<String> proxyServers;

    public HttpInboundServer(int port, List<String> proxyServers) {
        this.port=port;
        this.proxyServers = proxyServers;
    }

    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(16);

        try {
            ServerBootstrap b = new ServerBootstrap();

            //option 设置bossGroup参数;childOption 设置workerGroup参数;
            //SO_BACKLOG请求连接队列
            b.option(ChannelOption.SO_BACKLOG, 128)
                    //TCP_NODELAY 是否开启数据较小片段及时发送
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //socket 心跳
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //地址重用,等待关闭中得端口被投入使用
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    //接收buf大小
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    //发送buf大小
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    // 地址端口,等待关闭中得端口被投入使用
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //设置buf pool，默认直接使用堆外内存,也可以设计堆内内存
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new HttpInboundInitializer(this.proxyServers));

            Channel ch = b.bind(port).sync().channel();
            System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
