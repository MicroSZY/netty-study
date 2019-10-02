package com.netty.demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private static int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup msgGroup = new NioEventLoopGroup();

        // 服务端的引导类是serverBootStrap
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(msgGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 指定连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
                // 开启tcp底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 表示是否开始 Nagle 算法，true 表示关闭，false 表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就设置为true关闭，如果需要减少发送次数减少网络交互，就设置为false开启
                .option(ChannelOption.TCP_NODELAY, true)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });
        // 建立连接
        connect(bootstrap,"127.0.0.1",8000,MAX_RETRY);

    }

    // 重连（connect方法是异步的）
    private static void connect(Bootstrap bootstrap, String host, int port, int reTry){
        bootstrap.connect(host,port).addListener(future -> {
          if (future.isSuccess()) {
              System.out.println("连接成功");
          }else if (reTry == 0){
              System.out.println("重连超时，放弃连接！！");
          }else {
              // 第几次重连
              int order = (MAX_RETRY - reTry) + 1;
              // 本次重连间隔
              int delay = 1 << order;
              System.out.println(new Date() + "连接失败，正在第" + order + "次重新连接....");
              // 连接失败，递归调用自身，实现重连
              bootstrap.config().group().schedule(() ->connect(bootstrap, host, port,reTry - 1), delay, TimeUnit.SECONDS);
          }
        });
    }
}
