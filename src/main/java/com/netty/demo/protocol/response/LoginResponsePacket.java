package com.netty.demo.protocol.response;

import com.netty.demo.protocol.Packet;
import lombok.Data;

import static com.netty.demo.protocol.command.Command.LOGIN_RESPONSE;

@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
