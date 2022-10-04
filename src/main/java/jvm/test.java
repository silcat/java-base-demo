package jvm;

import lombok.Data;

@Data
public class test {
    public static String a;
    public static String c = "c";
    public  String b;
    static {
        a = "a";
        System.out.println("loada");
    }
    {
        b = "b";
        System.out.println("loadb");
    }

    public test() {
        System.out.println("init" + a);
        a= "a2";
        System.out.println("init");
    }

    public static String getA() {
        return a;
    }

    public static void setA(String a) {
        test.a = a;
    }
    public  String getB() {
        return b;
    }

    public void setB(String a) {
        b = a;
    }
}
