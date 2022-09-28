package 服务器.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import 服务器.netty.common.Respone;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            Respone Respone = (Respone) msg;
            System.out.print("client receive msg: [{}]"+Respone.toString());
            // 声明一个 AttributeKey 对象
            AttributeKey<Respone> key = AttributeKey.valueOf("Respone");
            // 将服务端的返回结果保存到 AttributeMap 上，AttributeMap 可以看作是一个Channel的共享数据源
            // AttributeMap的key是AttributeKey，value是Attribute
            ctx.channel().attr(key).set(Respone);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("client caught exception"+cause);
        ctx.close();
    }

}
