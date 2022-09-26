package io.netty.handler;

import com.alibaba.dts.shade.io.netty.buffer.ByteBuf;
import com.alibaba.dts.shade.io.netty.channel.ChannelHandlerContext;
import com.alibaba.dts.shade.io.netty.handler.codec.MessageToByteEncoder;

public class Encode extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //根据协议格式写数据
    }
}
