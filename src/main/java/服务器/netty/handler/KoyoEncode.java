package 服务器.netty.handler;

import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class KoyoEncode extends MessageToByteEncoder {
    private final KoyoSerializer serializer ;
    private final Class<?> genericClass ;

    public KoyoEncode(KoyoSerializer serializer, Class<?> genericClass) {
        this.serializer = serializer;
        this.genericClass = genericClass;
    }

    /**
     * 将对象转换为字节码然后写入到 ByteBuf 对象中
     */

    @Override
    protected void encode(io.netty.channel.ChannelHandlerContext channelHandlerContext, Object o, io.netty.buffer.ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(o)) {
            // 1. 将对象转换为byte
            byte[] body = serializer.serialize(o);
            // 2. 读取消息的长度
            int dataLength = body.length;
            // 3.写入消息对应的字节数组长度,writerIndex 加 4
            byteBuf.writeInt(dataLength);
            //4.将字节数组写入 ByteBuf 对象中
            byteBuf.writeBytes(body);
        }
    }
}

