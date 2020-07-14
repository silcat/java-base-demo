package strategyDemo;


import io.netty.util.HashedWheelTimer;
import io.netty.util.internal.PlatformDependent;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * hash轮
 */
public class HashedWheel {
    private Bucket[] wheel;
    private final long tickDuration ;
    private final int mask ;
    private final Queue<HashedWheel.Node> nodes;

    public HashedWheel(TimeUnit unit, long duration,  int ticksPerWheel) {
        this.nodes = PlatformDependent.newMpscQueue();
        this.wheel = createWheel(ticksPerWheel);
        this.tickDuration = duration;
        this.mask = this.wheel.length - 1;

    }
    private static final class Bucket {
        private HashedWheel.Node head;
        private HashedWheel.Node tail;

        private Bucket() {
        }

        public void add(HashedWheel.Node Node) {


        }

        public void remove(HashedWheel.Node Node) {

        }


    }
    private static final class Node {
        private HashedWheel.Node pre;
        private HashedWheel.Node next;
        HashedWheel.Bucket bucket;
        private String value;
        private Node() {
        }

        public void add(HashedWheel.Node node) {


        }

        public void excute() {


        }

        public void remove(HashedWheel.Node node) {

        }


    }
    private static HashedWheel.Bucket[] createWheel(int ticksPerWheel) {
        HashedWheel.Bucket[] wheel = new HashedWheel.Bucket[ticksPerWheel];
        //hash轮的数量要满足2的倍数
        int idx =1;
        while (idx<ticksPerWheel){
            idx<<= 1;
        }
        for(int i = 0; i < idx; ++i) {
            wheel[i] = new HashedWheel.Bucket();
        }
        return wheel;

    }


}
