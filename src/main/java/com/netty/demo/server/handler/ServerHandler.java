package com.netty.demo.server.handler;

import com.netty.demo.protocol.Packet;
import com.netty.demo.protocol.PacketCodec;
import com.netty.demo.protocol.request.LoginRequestPacket;
import com.netty.demo.protocol.request.MessageRequestPacket;
import com.netty.demo.protocol.response.LoginResponsePacket;
import com.netty.demo.protocol.response.MessageResponsePacket;
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
        // 登录响应
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        // 编码
        ByteBuf byteBuf;

        if (packet instanceof LoginRequestPacket){

            LoginRequestPacket lrp = (LoginRequestPacket) packet;
            if (login(lrp) == true){
                System.out.println(new Date() + "->登录成功");
                loginResponsePacket.setSuccess(true);
            }else{
                System.out.println(new Date() + "->登录失败");
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setReason("账号或密码错误！！！");
            }

            byteBuf = PacketCodec.INSTANCE.encode(ctx.alloc(),loginResponsePacket);

        }else if (packet instanceof MessageRequestPacket){

            // 输出接受到的消息
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
            System.out.println("客户端收到来自用户端的消息：" + messageRequestPacket.getMessage());

            // 向用户端响应
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端收到消息：" + messageRequestPacket.getMessage());

            // 编码
            byteBuf = PacketCodec.INSTANCE.encode(ctx.alloc(),messageResponsePacket);
        }else{
            throw new RuntimeException("未声明的指令类型！！！");
        }
        
        // 返回登录响应
        ctx.channel().writeAndFlush(byteBuf);
    }

    private boolean login(LoginRequestPacket lrp){
        if ("szy".equals(lrp.getUsername()) && "123456".equals(lrp.getPassword())){
            return true;
        }else {
            return false;
        }
    }
}
