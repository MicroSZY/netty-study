package com.netty.demo.server;

import com.netty.demo.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NettyServer {

    public static void main(String[] args) {

        // linkGroup管理链接的线程
        NioEventLoopGroup linkGroup = new NioEventLoopGroup();
        // 处理数据的线程
        NioEventLoopGroup msgGroup = new NioEventLoopGroup();

        // 引导类 ServerBootstrap，这个类将引导我们进行服务端的启动工作
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(linkGroup,msgGroup)
                // 指定io模型
                .channel(NioServerSocketChannel.class)
                // 定义后续每条连接的数据读写，业务处理逻辑（handler()用于指定在服务端启动过程中的一些逻辑）
                /**
                 *   option用于配置服务端本身的一些属性
                 *   childOption用户配置每条链接的一些属性
                 */
                // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG,1024)
                // 开启tcp底层心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                // 表示是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，
                //  如果需要减少发送次数减少网络交互，就开启。
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 业务逻辑
                        ch.pipeline().addLast(new FirstServerHandler());
//                        ch.pipeline().addLast(new ServerHandler());

                        // inBound，处理读数据的逻辑链
//                        ch.pipeline().addLast(new InBoundHandlerA());
//                        ch.pipeline().addLast(new InBoundHandlerB());
//                        ch.pipeline().addLast(new InBoundHandlerC());

                        // outBound，处理写数据的逻辑链
//                        ch.pipeline().addLast(new OutBoundHandlerA());
//                        ch.pipeline().addLast(new OutBoundHandlerB());
//                        ch.pipeline().addLast(new OutBoundHandlerC());

                    }
                });

                // 绑定端口号（bind 方法是异步的）
                bind(serverBootstrap,8000);

    }

    // 绑定端口号的静态方法，初始端口号绑定失败端口号就加一，知道绑定成功
    private static void bind(final ServerBootstrap server, final int port){
        server.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()){
                    System.out.println("端口号：" + port + "，绑定成功");
                }else {
                    System.out.println("端口号：" + port + "，绑定失败");
                    bind(server, port + 1);
                }
            }
        });

    }
}
