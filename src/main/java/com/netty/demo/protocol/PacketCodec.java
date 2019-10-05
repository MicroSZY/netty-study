package com.netty.demo.protocol;

import com.netty.demo.protocol.request.LoginRequestPacket;
import com.netty.demo.protocol.request.MessageRequestPacket;
import com.netty.demo.protocol.response.LoginResponsePacket;
import com.netty.demo.protocol.response.MessageResponsePacket;
import com.netty.demo.protocol.serializer.impl.JSONSerializer;
import com.netty.demo.protocol.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.netty.demo.protocol.command.Command.*;

/**
 * @description  实际编码，解码过程
 * @author YY
 * @date 2019/10/3/003
 */
public class PacketCodec {
    /**
     * 定义魔数（4字节）
     */
    private static final int MAGIC_NUMBER = 0x12345678;

    private static final Map<Byte,Class<? extends Packet>> packetTypeMap;

    private static final Map<Byte,Serializer> serializerMap;

    public static final PacketCodec INSTANCE = new PacketCodec();

    static{
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(),serializer);
    }

    /**
     * 编码方法（java对象转换成二进制流）
     */
    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet){

        /**
         * 1.首先，我们需要创建一个 ByteBuf，这里我们调用 Netty 的 ByteBuf 分配器来创建，
         *   ioBuffer() 方法会返回适配 io 读写相关的内存，它会尽可能创建一个直接内存，
         *   直接内存可以理解为不受 jvm 堆管理的内存空间，写到 IO 缓冲区的效果更高。
         *
         * 2.接下来，我们将 Java 对象序列化成二进制数据包。
         *
         * 3.最后，我们对照本小节开头协议的设计以及上一小节 ByteBuf 的 API，逐个往 ByteBuf 写入字段，
         *   即实现了编码过程，到此，编码过程结束。
         */

        // 1.创建ByteBuf对象
        ByteBuf byteBuf = byteBufAllocator.DEFAULT.ioBuffer();
        // 2.序列化java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3.实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);// 魔数
        byteBuf.writeByte(packet.getVersion());// 版本
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm()); // 序列化算法类型
        byteBuf.writeByte(packet.getCommand()); // 指令类型
        byteBuf.writeInt(bytes.length);  // 消息长度
        byteBuf.writeBytes(bytes);       // 消息内容

        return byteBuf;
    }

    /**
     * 解码方法（二进制流转换成java对象）
     */
    public Packet decode(ByteBuf byteBuf){
        /**
         * 1.我们假定 decode 方法传递进来的 ByteBuf 已经是合法的，即首四个字节是我们前面定义的魔数 0x12345678，
         *   这里我们调用 skipBytes 跳过这四个字节。这里，我们暂时不关注协议版本，通常我们在没有遇到协议升级的时候，
         *   这个字段暂时不处理，因为，你会发现，绝大多数情况下，这个字段几乎用不着，但我们仍然需要暂时留着。
         *
         * 2.接下来，我们调用 ByteBuf 的 API 分别拿到序列化算法标识、指令、数据包的长度。
         *
         * 3.最后，我们根据拿到的数据包的长度取出数据，通过指令拿到该数据包对应的 Java 对象的类型，根据序列化算法标识拿到序列化对象，
         *   将字节数组转换为 Java 对象，至此，解码过程结束。
         */

        // 跳过魔数
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length =  byteBuf.readInt();

        // 实际数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        // 获取指令类型
        Class<? extends Packet> packetType = getPacketType(command);
        // 获取序列化类型
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (packetType != null && serializer != null){
            // 反序列化得到java对象
            return serializer.deserialize(packetType,bytes);
        }else {
            return null;
        }
    }

    /**
     * 获取请求类型的方法
     */
    public Class<? extends Packet> getPacketType(byte command){
        return packetTypeMap.get(command);
    }

    /**
     * 获取序列化方式的方法
     */
    public Serializer getSerializer(byte serializeAlgorithm){
        return serializerMap.get(serializeAlgorithm);
    }

}
