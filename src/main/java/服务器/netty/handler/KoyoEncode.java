package 服务器.netty.handler;

import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import 服务器.netty.common.MessageFormatConst;
import 服务器.netty.common.Request;

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
//            // 4B full length（消息长度）. 总长度先空着，后面填。
//            byteBuf.writerIndex(byteBuf.writerIndex() + MessageFormatConst.FULL_LENGTH_LENGTH);
//            // 1B messageType（消息类型）
//            byteBuf.writeByte(rpcMessage.getMessageType());
//            // 1B codec（序列化类型）
//            byteBuf.writeByte(rpcMessage.getSerializeType());
//            // 1B compress（压缩类型）
//            byteBuf.writeByte(rpcMessage.getCompressTye());
//            // 8B requestId（请求的Id）
//            byteBuf.writeLong(rpcMessage.getRequestId());
//            // 写 body，返回 body 长度
//            int bodyLength = writeBody(rpcMessage, byteBuf);

            // 1B magic code（魔数）
            byteBuf.writeByte(MessageFormatConst.MAGIC);
            // 1B version（版本）
            byteBuf.writeByte(MessageFormatConst.VERSION);
            //长度，int,先空下来
            byteBuf.writerIndex(byteBuf.writerIndex()+4);
            byte[] body = serializer.serialize(o);
            //body
            byteBuf.writeBytes(body);
            int current = byteBuf.writerIndex();
            //计算长度
            byteBuf.writerIndex(MessageFormatConst.MAGIC_LENGTH+MessageFormatConst.VERSION_LENGTH);
            int dataLength = body.length;
            byteBuf.writeInt(dataLength);
            //重置长度
            byteBuf.writerIndex(current);

        }
    }
}

