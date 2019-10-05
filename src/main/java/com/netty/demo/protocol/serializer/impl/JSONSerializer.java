package com.netty.demo.protocol.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.netty.demo.protocol.serializer.Serializer;
import com.netty.demo.protocol.serializer.SerializerAlgorithm;

/**
 * @description  序列化实现类
 * @author YY
 * @date 2019/10/3/003
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
