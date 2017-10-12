package juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by admin on 2017/9/20.
 */
public class SemaphoreTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        ContentPool contentPool = new ContentPool();
        System.out.println("总剩余资源为6，每次只能允许4个线程获得资源，当线程资源释放时，其他线程才可以继续获得资源");
        for (int i = 0; i < 6; i++) {
            executorService.execute(new user(contentPool));
        }
    }
}

class ContentPool {
    private static final int MAX_AVAILABLE = 4;
    private static int count = 6;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    public Object getItem() throws InterruptedException {
        available.acquire();
        --count;
        System.out.println("占用一个资源，剩余资源为：" + count);
        return getNextAvailableItem();
    }

    public void putItem(Object x,Integer count1) {
        if (markAsUnused(x))
            available.release();
        ++count;
        System.out.println("释放资源"+count1+"剩余资源为：" + count);
    }

    protected int[] items = {1, 2, 3, 4,5,6};
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    synchronized Object getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                used[i] = true;
                return items[i];
            }
        }
        return null; // not reached
    }

    synchronized boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if ((int)item == items[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }
}

class user implements Runnable {
    private ContentPool contentPool;
    private int count = (int) (Math.random() * 5 + 1);

    public user(ContentPool contentPool) {
        this.contentPool = contentPool;
    }

    @Override
    public void run() {
        try {
            Object item = contentPool.getItem();
            System.out.println(Thread.currentThread().getName()+"得到资源"+item);
            Thread.sleep(1000);
            contentPool.putItem(count,count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}