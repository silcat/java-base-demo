package juc;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by admin on 2017/9/19.
 */
public class ThreadNotifyTest {
    public static void main(String[] args) throws InterruptedException {
        ShareDate shareDate = new ShareDate(100);
        Thread thread_consume_1 = new Thread(() -> shareDate.parseJishu(), "奇数线程");
        Thread thread_produce_1 = new Thread(() -> shareDate.parseOushu(), "偶数线程1");
//        Thread thread_produce_2 = new Thread(() -> shareDate.parseOushu(), "偶数线程2");
        thread_consume_1.start();
        thread_produce_1.start();
//        thread_produce_2.start();

    }
    static class ShareDate{
        private volatile boolean flag = true;
        private int count ;

        final ReentrantLock lock;

        private final Condition oushu;

        private final Condition jishu;

        public ShareDate(int count) {
            this.lock = new ReentrantLock(true);
            this.oushu = lock.newCondition();
            this.jishu = lock.newCondition();
            this.count =  count;
        }

        private void parseJishu() {
            while (flag){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                lock.lock();
                while (count%2==0){
                    try {
                        System.out.println(Thread.currentThread().getName()+"  wating...");
                        jishu.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName()+ ":"+count);
                count--;
                if (count <= 0){
                    flag = false;
                }
                oushu.signalAll();
                lock.unlock();
            }
        }
        private void parseOushu() {
            while (flag){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                lock.lock();
                while (count%2!=0){
                    try {
                        System.out.println(Thread.currentThread().getName()+"  wating...");
                        oushu.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName()+ ":"+count);
                count--;
                if (count <= 0){
                    flag = false;
                }
                jishu.signalAll();
                lock.unlock();
            }
        }
    }


}
