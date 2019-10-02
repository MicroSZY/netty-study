package com.netty.demo.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufTest {

    public static void main(String[] args) {

        /**
         * 1.本质上它的原理就是，它引用了一段内存，这段内存可以是堆内也可以是堆外的，
         *   然后用引用计数来控制这段内存是否需要被释放，使用读写指针来控制对 ByteBuf 的读写，
         *   可以理解为是外观模式的一种使用
         *
         * 2.基于读写指针和容量、最大可扩容容量，衍生出一系列的读写方法，
         *   要注意 read/write 与 get/set 的区别
         *
         * 3.多个 ByteBuf 可以引用同一段内存，通过引用计数来控制内存的释放，
         *   遵循 谁retain() 谁release() 的原则
         */

        // 初始化一个ByteBuf
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9,100);
        print("allocate ByteBuf(9, 100)", buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        buffer.writeBytes(new byte[]{1,2,3,4});
        print("writeBytes(1,2,3,4)",buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写, 写完 int 类型之后，写指针增加4（int占用4字节）
        buffer.writeInt(12);
        print("writeInt(12)",buffer);

        // write 方法改变写指针, 写完之后写指针等于 capacity 的时候，buffer 不可写
        buffer.writeBytes(new byte[]{5});
        print("writeBytes(5)",buffer);

        // write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 随即改变
        buffer.writeBytes(new byte[]{7,8});
        print("writeBytes(7,8)",buffer);

        // get 方法不改变读写指针
        System.out.println("getByte(3) return: " + buffer.getByte(3));
        System.out.println("getShort(3) return: " + buffer.getShort(3));
        System.out.println("getInt(3) return: " + buffer.getInt(3));
        print("getByte()", buffer);

        // set 方法不改变读写指针
        buffer.setByte(buffer.readableBytes() + 1,0);
        print("setByte(7)",buffer);

    }

    public static void print(String action, ByteBuf buffer){
        System.out.println("after ===========" + action + "============");
        System.out.println("capacity(): " + buffer.capacity());                 // 初始化容量
        System.out.println("maxCapacity(): " + buffer.maxCapacity());           // 最大容量
        System.out.println("readerIndex(): " + buffer.readerIndex());           // 读指针的位置,默认是0
        System.out.println("readableBytes(): " + buffer.readableBytes());       // 可读字节数
        System.out.println("isReadable(): " + buffer.isReadable());             // 是否可读:取决于readableBytes的大小,readableBytes大于0可读,反之不可读
        System.out.println("writerIndex(): " + buffer.writerIndex());           // 写指针的位置(可读字节数-1)
        System.out.println("writableBytes(): " + buffer.writableBytes());       // 可写字节数(容量 - 可读字节数)
        System.out.println("isWritable(): " + buffer.isWritable());             // 是否可写(取决于可写字节数)
        System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes()); // 最大可写字节数(最大容量 - 可读字节数)
        System.out.println();
    }
}
