package juc.test;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;


public class LiveLockTest {
    private Lock lock1 = new ReentrantLock(true);
    private Lock lock2 = new ReentrantLock(true);

    public static void main(String[] args) {
        LiveLockTest livelock = new LiveLockTest();
        new Thread(livelock::operation1, "T1").start();
        new Thread(livelock::operation2, "T2").start();
    }

    public void operation1()  {
        while (true) {
            tryLock(lock1, 50);
            print("lock1 acquired, trying to acquire lock2.");
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (tryLock(lock2)) {
                print("lock2 acquired.");
            } else {
                print("cannot acquire lock2, releasing lock1.");
                lock1.unlock();
                continue;
            }

            print("executing first operation.");
            break;
        }
        lock2.unlock();
        lock1.unlock();
    }

    public void operation2() {
        while (true) {
            tryLock(lock2, 50);
            print("lock2 acquired, trying to acquire lock1.");
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (tryLock(lock1)) {
                print("lock1 acquired.");
            } else {
                print("cannot acquire lock1, releasing lock2.");
                lock2.unlock();
                continue;
            }

            print("executing second operation.");
            break;
        }
        lock1.unlock();
        lock2.unlock();
    }
    public boolean tryLock(Lock lock,long second){
        try {
            return lock.tryLock(second,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean tryLock(Lock lock){
        return lock.tryLock();
    }
    public void print(String pri){
       System.out.println(pri);
    }
}
