package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by admin on 2017/9/19.
 */
public class ReentrantLockTest {
    public static void main(String[] args) {
        final AlternateDemo ad = new AlternateDemo();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 20; i++) {
                    ad.loopA(i);
                }
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 20; i++) {
                    ad.loopB(i);
                }
            }
        }, "B").start();

    }

}

class AlternateDemo {
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private int number = 1; //用于确定当前执行线程的标记

    public void loopA(int totalLoop) {
        lock.lock();
        try {
            //判断
            if (number != 1) {
                try {
                    condition1.await();
                } catch (InterruptedException e) {
                }
            }
            //打印
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }
            //唤醒
            number = 2;
            condition2.signal();
        } finally {
            lock.unlock();
        }
    }

    public void loopB(int totalLoop) {
        lock.lock();
        try {
            //判断
            if (number != 2) {
                try {
                    condition2.await();
                } catch (InterruptedException e) {
                }
            }
            //打印
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }
            //唤醒
            number = 1;
            condition1.signal();
        } finally {
            lock.unlock();
        }
    }
}