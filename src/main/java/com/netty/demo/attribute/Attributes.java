package com.netty.demo.attribute;

import io.netty.util.AttributeKey;

public interface Attributes {
    // 登录成功的标识位
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
