package com.netty.demo.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 收消息的逻辑
        ByteBuf in = (ByteBuf) msg;
        System.out.println(new Date() + " -> 服务端收到消息：" + in.toString(Charset.forName("utf-8")));

        // 回复客户端的逻辑
        System.out.println("服务端开始回复消息");
        ByteBuf out = getByteBuf(ctx);
        ctx.writeAndFlush(out);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] bytes = "我是服务端".getBytes(Charset.forName("utf-8"));
        buffer.writeBytes(bytes);
        return buffer;
    }
}
