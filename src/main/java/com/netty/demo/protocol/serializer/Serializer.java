package com.netty.demo.protocol.serializer;

import com.netty.demo.protocol.serializer.impl.JSONSerializer;

/**
 * @description  定义序列化接口
 * @author YY
 * @date 2019/10/3/003
 */
public interface Serializer {
    /**
     * 获取序列化标识
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * json 序列化算法类型
     */
    byte JSON_SERIALIZER = 1;

    /**
     * 默认序列化算法
     */
    Serializer DEFAULT = new JSONSerializer();
}
