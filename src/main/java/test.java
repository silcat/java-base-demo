import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
