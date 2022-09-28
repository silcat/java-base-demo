package 服务器.netty.client;

import com.alibaba.dts.common.domain.remoting.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import 服务器.netty.common.Request;
import 服务器.netty.common.Respone;
import 服务器.netty.handler.*;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Scanner;


public class Client {
    private static String host;
    private static int port;
    static Bootstrap bootstrap;
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }
    static{
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        KoyoSerializer kryoSerializer = new KoyoSerializer();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)throws Exception {
                        // RpcResponse -> ByteBuf
                        ch.pipeline().addLast(new KoyoEncode(kryoSerializer, Request.class));
                        // ByteBuf -> RpcRequest
                        ch.pipeline().addLast(new KoyoDecode(kryoSerializer, Respone.class));
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        bootstrap = b;
    }



    /**
     * 发送消息到服务
     * @return 服务端返回的数据
     */
    public Respone sendMessage(Request msg) {
        try {
            ChannelFuture f = bootstrap.connect(host, port).sync();
            Channel futureChannel = f.channel();

            if (futureChannel != null) {
                futureChannel.writeAndFlush(msg).addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.print("client send message: [{}]" + msg);
                    } else {
                        System.out.print("Send failed:" + msg);
                    }
                });
                //阻塞等待 ，直到Channel关闭
                futureChannel.closeFuture().sync();
                // 将服务端返回的数据也就是RpcResponse对象取出
                AttributeKey<Respone> key = AttributeKey.valueOf("rpcResponse");
                return futureChannel.attr(key).get();
            }
        } catch (InterruptedException e) {
            System.out.print("occur exception when connect server:" + e);
        }
        return null;

    }
    public static void main(String[] args) throws Exception {
   ;
        Client client = new Client("127.0.0.1", 9003);
        Scanner scan = new Scanner(System.in);
        while(scan.hasNext()){
            String str = scan.next();
            Request request = new Request(str);
            Respone respone = client.sendMessage(request);
            System.out.println(respone);
        }

    }
}

