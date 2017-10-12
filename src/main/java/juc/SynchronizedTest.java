package juc;

/**
 * Created by admin on 2017/9/19.
 */
public class SynchronizedTest {

        public static void main(String[] args) {
            Clerk clerk = new Clerk();
            Clerk clerk1 = new Clerk();

            Productor pro = new Productor(clerk);
            Consumer con = new Consumer(clerk);
            Consumer con1 = new Consumer(clerk1);


            new Thread(con, "消费者 A").start();
            new Thread(con, "消费者 B").start();
            new Thread(con1, "消费者 C").start();
            new Thread(con1, "消费者 D").start();
        }

    }

// 店员
class Clerk {
    private static int product = 5;

    // 进货
    public  synchronized void get() { // 循环：0
        while (product > 5) {
            System.out.println("产品已满！");

            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }

        System.out.println(Thread.currentThread().getName() + " : " + product++);

        this.notifyAll();

    }

    // 卖货
    public synchronized void sale() {// product = 0, 循环： 0
        while (product <= 0) { //为了避免虚假唤醒，wait() 应该总是使用在 循环中
            System.out.println("缺货！");

            try {
                this.wait();// ----
            } catch (InterruptedException e) {
            }
        }

        System.out.println(Thread.currentThread().getName() + " : " + product--);

        this.notifyAll();

    }
}

// 生产者
class Productor implements Runnable {

    private Clerk clerk;

    public Productor(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {


            clerk.get();
        }
    }

}

// 消费者
class Consumer implements Runnable {
    private Clerk clerk;

    public Consumer(Clerk clerk) {
        super();
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clerk.sale();
        }
    }
}


