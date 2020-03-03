package com.netty.demo.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * @return void
     * @description 发消息的方法
     * @param: ctx
     * @author YY
     * @date 2019/10/1/001
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            // 1.获取数据
            ByteBuf buffer = getByteBuf(ctx);
            // 2.写数据
            ctx.channel().writeAndFlush(buffer);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println(new Date() + " : 用户端收到服务端的消息 ->" + in.toString(Charset.forName("utf-8")));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        System.out.println(new Date() + "：客户端输出数据");

        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 伪造数据，指定字符串的字符集为 utf-8
        byte[] bytes = "大家好，我是yy，是一名新手java开发者，希望多多指教！！！".getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }

}
