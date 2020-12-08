package zookeeper.distributedLock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;
import zookeeper.ZKclient;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Slf4j
public class ZkLockTester {

    int count = 0;

    @Test
    public void testLock() throws InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            if (i == 0){
                //测试可重入锁
                threadPool.submit(()->{
                    ZkLock lock = new ZkLock();
                    lock.lock();
                    lock.lock();
                    for (int j = 0; j < 10; j++) {
                        count++;
                    }

                    log.info("count = " + count);
                    lock.unlock();
                    lock.unlock();
                });
            }else {
                threadPool.submit(()->{
                    ZkLock lock = new ZkLock();
                    lock.lock();

                    for (int j = 0; j < 10; j++) {
                        count++;
                    }

                    log.info("count = " + count);
                    lock.unlock();
                });
            }


        }

        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void testzkMutex() throws InterruptedException {

        CuratorFramework client = ZKclient.instance.getClient();
        final InterProcessMutex zkMutex =
                new InterProcessMutex(client, "/mutex");
        ;
        for (int i = 0; i < 10; i++) {

        }

        Thread.sleep(Integer.MAX_VALUE);
    }


}
