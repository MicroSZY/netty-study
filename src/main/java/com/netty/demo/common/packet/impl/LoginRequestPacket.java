package com.netty.demo.common.packet.impl;

import com.netty.demo.common.packet.Packet;
import lombok.Data;

import static com.netty.demo.common.packet.Command.LOGIN_REQUEST;

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
