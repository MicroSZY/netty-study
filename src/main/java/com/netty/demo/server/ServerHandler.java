package com.netty.demo.server;

import com.netty.demo.common.packet.Packet;
import com.netty.demo.common.packet.PacketCodec;
import com.netty.demo.common.packet.impl.LoginRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

@Log4j2
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf requestByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodec.INSTANCE.decode(requestByteBuf);

        if ( packet instanceof LoginRequestPacket){
            LoginRequestPacket lrp = (LoginRequestPacket) packet;
            if (login(lrp) == true){
                log.info(new Date() + "登录成功");
            }else{
                log.info(new Date() + "登录失败");
            }
        }
    }

    private boolean login(LoginRequestPacket lrp){
        if ("szy".equals(lrp.getUsername()) && "123456".equals(lrp.getPassword())){
            return true;
        }else {
            return false;
        }
    }
}
