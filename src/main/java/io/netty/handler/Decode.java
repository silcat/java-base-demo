package io.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 解决半包与粘包
 */
public class Decode extends LengthFieldBasedFrameDecoder {
    public Decode(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            try {
                return decodeFrame(frame);
            } catch (Exception e) {
                throw e;
            } finally {
                frame.release();
            }
        }
        return decoded;
    }

    public Object decodeFrame(ByteBuf frame) {
        //根据协议格式解析内容
        return frame;
    }

}
