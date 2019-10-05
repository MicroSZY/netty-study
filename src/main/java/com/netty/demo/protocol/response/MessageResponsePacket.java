package com.netty.demo.protocol.response;

import com.netty.demo.protocol.Packet;
import lombok.Data;

import static com.netty.demo.protocol.command.Command.MESSAGE_RESPONSE;

@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
