package juc.test;


import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class ClassHeaderTest {

    public static void main(String[] args) {
        D d = new D();
        nolock(d);
//     pxlock(d);
//        qlLock(d);
    }
    public static class D {
    }
    public static void nolock(D d){
        System.out.println("//无锁==========================：" +ClassLayout.parseInstance(d).toPrintable());

    }
    public static void pxlock(D d){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (d) {
            System.out.println("//偏向锁=====================：" + ClassLayout.parseInstance(d).toPrintable());
        }
    }
    public static void qlLock(D d){
        final Object o = new Object();
        Thread thread= new Thread(){
            @Override
            public void run() {
                synchronized (o){
                    System.out.println("//偏向锁=====================："+ClassLayout.parseInstance(o).toPrintable());
                }
            }
        };
        Thread thread1= new Thread(){
            @Override
            public void run() {
                synchronized (o){
                    System.out.println("//偏向锁=====================："+ClassLayout.parseInstance(o).toPrintable());
                }
            }
        };
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (o){
            System.out.println("//轻量锁=====================："+ClassLayout.parseInstance(o).toPrintable()); //轻量锁

        }
    }
}
