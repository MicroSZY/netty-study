package com.netty.demo.protocol.request;

import com.netty.demo.protocol.Packet;
import lombok.Data;

import static com.netty.demo.protocol.command.Command.LOGIN_REQUEST;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
