package juc.test;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class ThreadLocalTest {

    public static ThreadLocal local =  new ThreadLocal();

    public static void main(String[] args) {
        local.set("我是value");
        local = null;
        System.gc();
        while (true){
            Thread thread = currentThread();
            //   {ThreadLocal$ThreadLocalMap$Entry@502}
            //        discovered = null
            //        next = {ThreadLocal$ThreadLocalMap$Entry@502}
            //        queue = {ReferenceQueue$Null@507}
            //        referent = null 内存泄漏
            //        value = "我是value"
            try {
                sleep(5000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            local.remove();//Exception in thread "main" java.lang.NullPointerExceptionat juc.test.ThreadLocalTest.main(ThreadLocalTest.java:33)
        }

    }

}
