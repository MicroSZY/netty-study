package com.netty.demo.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * @description  发消息的方法
     * @param: ctx
     * @return void
     * @author YY
     * @date 2019/10/1/001
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
        // 1.获取数据
        ByteBuf buffer = getByteBuf(ctx);
        // 2.写数据
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf in = (ByteBuf) msg;
        System.out.println(new Date() + " : 用户端收到服务端的消息 ->" + in.toString(Charset.forName("utf-8")));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx){
        System.out.println(new Date() + "：客户端输出数据");

        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 伪造数据，指定字符串的字符集为 utf-8
        byte[] bytes = "吗卖批".getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }

}
