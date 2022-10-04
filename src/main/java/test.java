import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.DecoderException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.curator.shaded.com.google.common.base.Charsets;
import spring.lifeCycle.Person;

import java.nio.charset.Charset;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.printLong;

public class test {

    public static void main(String[] args)  {
        Person person = new Person();
        person.setName("ytf");

        System.out.println( JSON.toJSONString(person) );



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
