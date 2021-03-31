import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.DecoderException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.curator.shaded.com.google.common.base.Charsets;

import java.nio.charset.Charset;

import static javax.xml.bind.DatatypeConverter.printLong;

public class test {

    public static void main(String[] args)  {

//
//        s = 33;
//        System.out.println(s);
//        System.out.println(integer);
//        long workerId= 32L;
//        long time = System.currentTimeMillis()-1483200000000L << 23;
//        long sequence  = 1024L ;
//        long l = time | workerId | sequence;
////        print(RUNNING);
////        print(CAPACITY);
////        print(0 << 29);
////        print(1 << 29);
////        print(2 << 29);
////        print(3 << 29);
//
//
////        print(RUNNING&CAPACITY);
////        print(c+1);
//        printLong(l );
//        printLong(~(-1L << 12)&4096);
////        print(c+1 & ~CAPACITY);
////        print((c+1)&CAPACITY);
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer();
        writeStr(buf ,"aaa");
        writeStr(buf ,"bb");
        writeStr(buf, JSON.toJSONString(new Object()));
        System.out.println(readStr(buf));
        System.out.println(readStr(buf));
        System.out.println(readStr(buf));

    }
    private static void writeStr(ByteBuf buf, String s) {
        buf.writeInt(s.length());
        buf.writeBytes(s.getBytes(Charset.forName("utf8")));
    }
    private static String readStr(ByteBuf in) {
        int len = in.readInt();
        if (len < 0 || len > (1 << 20)) {
            throw new DecoderException("string too long len=" + len);
        }
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        return new String(bytes, Charset.forName("utf8"));
    }
    private static void print( Integer num){
        System.out.println(num);
        System.out.println(Integer.toBinaryString(num));
    }
    private static void printLong( Long num){
        System.out.println(num);
        System.out.println(Long.toBinaryString(num));
    }
}
