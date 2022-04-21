package juc.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2017/9/19.
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(3);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i=0;i<3;i++){
           executorService.execute(new Worker(startSignal, doneSignal));
        }
        System.out.println("准备完毕");
        startSignal.countDown();
        System.out.println("开始执行");
        doneSignal.await();
        System.out.println("执行完毕");

    }
     static class Worker implements Runnable {
        private  static Integer count=1;
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;
        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }
        public void run() {
            try {
                startSignal.await();
                doWork(count);
                count++;
                doneSignal.countDown();
            } catch (InterruptedException ex) {} // return;
        }

        void doWork(Integer count) {
            System.out.println(Thread.currentThread().getName()+"-"+count);
        }
    }

}
