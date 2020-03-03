package com.netty.demo.client.handler;

import com.netty.demo.protocol.Packet;
import com.netty.demo.protocol.PacketCodec;
import com.netty.demo.protocol.request.LoginRequestPacket;
import com.netty.demo.protocol.response.LoginResponsePacket;
import com.netty.demo.protocol.response.MessageResponsePacket;
import com.netty.demo.utils.LoginUtil;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.UUID;

@Log4j2
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + "->客户端开始登录");

        // 创建登录对象
        LoginRequestPacket lrp = new LoginRequestPacket();
        lrp.setUserId(UUID.randomUUID().toString());
        lrp.setUsername("szy");
        lrp.setPassword("123456");

        // 编码
        ByteBuf byteBuf = PacketCodec.INSTANCE.encode(ctx.alloc(), lrp);

        // 写入数据
        ctx.channel().writeAndFlush(byteBuf);
        //ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf responseByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodec.INSTANCE.decode(responseByteBuf);

        if (packet instanceof LoginResponsePacket) {
            // 强转
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (packet instanceof LoginResponsePacket) {
                LoginUtil.markAsLogin(ctx.channel());
                System.out.println(new Date() + "->客户端登录成功！！！");
            } else {
                System.out.println(new Date() + "->登录失败，原因：" + loginResponsePacket.getReason());
            }

        } else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(messageResponsePacket.getMessage());

        }
    }
}
