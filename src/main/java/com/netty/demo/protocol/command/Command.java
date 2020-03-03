package com.netty.demo.protocol.command;

/**
 * @author YY
 * @description 定义指令(类似于常量池或者枚举类)
 * @date 2019/10/3/003
 */
public interface Command {

    // 登录请求
    Byte LOGIN_REQUEST = 1;

    // 登录响应
    Byte LOGIN_RESPONSE = 2;

    // 发送消息请求
    Byte MESSAGE_REQUEST = 3;

    // 服务端响应的指令
    Byte MESSAGE_RESPONSE = 4;
}

