package juc.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by admin on 2018/2/2.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe=(Unsafe)f.get(null);


    }

}
