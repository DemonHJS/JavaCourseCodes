package com.huangjs.gateway.outbound.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * @Author: HuangFu
 * @Date: 2021/9/27 8:24
 * @Description:
 */
public class NettyHttpClientInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        String uri = fullRequest.uri();
        fullRequest.content();

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(uri.getBytes(StandardCharsets.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.write(response);
        ctx.flush();
    }

}
