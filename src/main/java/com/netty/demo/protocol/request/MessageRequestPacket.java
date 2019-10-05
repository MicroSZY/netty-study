package com.netty.demo.protocol.request;

import com.netty.demo.protocol.Packet;
import lombok.Data;

import static com.netty.demo.protocol.command.Command.MESSAGE_REQUEST;

@Data
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
