package com.netty.demo.protocol;

import lombok.Data;

/**
 * @author YY
 * @description 获取指令的抽象方法，所有的指令数据包都必须实现这个方法，
 * 这样我们就可以知道某种指令的含义。
 * @date 2019/10/3/003
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     */
    public abstract Byte getCommand();
}
