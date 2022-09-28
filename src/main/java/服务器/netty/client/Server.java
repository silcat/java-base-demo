package 服务器.netty.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import 服务器.netty.common.Request;
import 服务器.netty.common.Respone;
import 服务器.netty.handler.*;

import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws Exception {
        new Server(9003).start();
    }

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final ServerHandler serverHandler = new ServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        KoyoSerializer kryoSerializer = new KoyoSerializer();
        //linix 2.6支持epoll
//        EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // RpcResponse -> ByteBuf
                            ch.pipeline().addLast(new KoyoEncode(kryoSerializer, Respone.class));
                            // ByteBuf -> RpcRequest
                            ch.pipeline().addLast(new KoyoDecode(kryoSerializer, Request.class));
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }


}
