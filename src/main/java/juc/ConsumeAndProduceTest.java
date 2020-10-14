package juc;


/**
 * Created by admin on 2017/9/19.
 */
public class ConsumeAndProduceTest {
    public static void main(String[] args) throws InterruptedException {
        ShareDate shareDate = new ShareDate();
        Thread thread_consume_1 = new Thread(() -> shareDate.consume(), "Thread_consume_1");
        Thread thread_consume_2 = new Thread(() -> shareDate.consume(), "Thread_consume_2");
        Thread thread_produce_1 = new Thread(() -> shareDate.produce(), "Thread_produce_1");
        Thread thread_produce_2 = new Thread(() -> shareDate.produce(), "Thread_produce_2");
        Thread thread_produce_3 = new Thread(() -> shareDate.produce(), "Thread_produce_3");
        thread_consume_1.start();
        thread_consume_2.start();
        thread_produce_1.start();
        thread_produce_2.start();
        thread_produce_3.start();
        Thread.sleep(10000);
        shareDate.stop();

    }
    static class ShareDate{
            private volatile boolean flag = true;
            public  int MAX_SIZE  = 10;
            public  int count = 0;
            public  final Object lock = new Object();

            private void produce() {
               while (flag){
                   try {
                       Thread.sleep(100);
                   } catch (InterruptedException e) {
                   }
                   synchronized (lock){
                       while (count == MAX_SIZE){
                           try {
                               System.out.println(Thread.currentThread().getName()+" ：pool is full, wating...");
                               lock.wait();
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                       }
                       System.out.println(Thread.currentThread().getName()+"：pool add count is " + ++count);
                       lock.notifyAll();
                   }


               }
            }

            private void consume() {
                while (flag){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    synchronized (lock){
                        while (count == 0){
                            try {
                                System.out.println(Thread.currentThread().getName()+ "：pool is empty, wating...");
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println(Thread.currentThread().getName()+"：pool remove count is " + --count);
                        lock.notifyAll();
                    }


                }
            }

            private void stop(){
                flag = false;
                System.out.println(Thread.currentThread().getName()+"：stop ");
            }
        }

}
