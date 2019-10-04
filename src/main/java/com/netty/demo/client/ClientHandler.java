package com.netty.demo.client;

import com.netty.demo.common.packet.PacketCodec;
import com.netty.demo.common.packet.impl.LoginRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.UUID;

@Log4j2
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        log.info(new Date() + "客户端开始登录");

        // 创建登录对象
        LoginRequestPacket lrp = new LoginRequestPacket();
        lrp.setUserId(UUID.randomUUID().toString());
        lrp.setUsername("szy");
        lrp.setPassword("123456");

        // 编码
        ByteBuf byteBuf = PacketCodec.INSTANCE.encode(ctx.alloc(),lrp);

        // 写入数据
        ctx.channel().writeAndFlush(byteBuf);
        //ctx.writeAndFlush(byteBuf);
    }
}
