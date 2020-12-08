package zookeeper.distributedLock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;
import zookeeper.ZKclient;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Slf4j
public class ZkLockTester {

    int count = 0;

    @Test
    public void testLock() throws InterruptedException {
        for (int i = 0; i < 10; i++) {

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
