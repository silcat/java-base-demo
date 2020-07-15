package strategyDemo;

import io.netty.util.HashedWheelTimer;
import io.netty.util.internal.PlatformDependent;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * hash轮
 */
public class HashedWheel {
    //hash环
    private Bucket[] wheel;
    private final int mask ;
    private final Queue<HashedWheel.Node> nodes;
    private final long tickDuration;
    //选择次数
    private int tick;
    public HashedWheel(long tickDuration, int ticksPerWheel) {
        this.nodes = PlatformDependent.newMpscQueue();
        this.wheel = createWheel(ticksPerWheel);
        this.mask = this.wheel.length - 1;
        this.tickDuration = tickDuration;
    }

    //双向链表
    private static final class Bucket {
        private HashedWheel.Node head;
        private HashedWheel.Node tail;

        private Bucket() {
        }

        public void add(HashedWheel.Node node) {
            node.bucket = this;
            if (this.head == null){
                this.head = this.tail = node;
            }else {
                tail.next = node;
                node.pre = this.tail;
                this.tail = node;
            }

        }

        public HashedWheel.Node remove(HashedWheel.Node node) {
            Node next = node.next;
            if (node.pre != null){
                node.pre.next = next;
            }
            if (node.next !=null){
                node.next.pre = node.pre;
            }
            if (node == this.head){
                if (node == this.tail){
                    this.tail = null;
                    this.head = null;
                }else {
                    this.head = next;
                }
            }else if (node == this.tail){
                this.tail = node.pre;
            }
            node.next = null;
            node.pre = null;
            node.bucket = null;
            return next;
        }

        public HashedWheel.Node pollFirst() {
            Node head = this.head;
            remove(this.head);
            return head;
        }

        public void excute() {
            HashedWheel.Node next;
            for(HashedWheel.Node node = this.head; node != null; node = next) {
                next = node.next;
                if (node.remainRounds <= 0){
                    node.excute();
                }else {
                    --node.remainRounds;
                }
            }
        }
    }
    //链表节点
    private static final class Node {
        private HashedWheel.Node pre;
        private HashedWheel.Node next;
        HashedWheel.Bucket bucket;
        private long deadTime;
        private int remainRounds;
        private Node() {
        }

        public void excute() {
            System.out.print("执行任务节点");
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

    private void addWheel(Node node){
        long calculated = node.deadTime / HashedWheel.this.tickDuration;
        long ticks = Math.max(calculated, this.tick);
        node.remainRounds  = (int) (calculated - this.tick) / this.wheel.length;
        int stopIndex = (int)(ticks & (long)HashedWheel.this.mask);
        this.wheel[stopIndex].add(node);
    }
    private Bucket getBucket(long deadTime){
        long calculated = deadTime / HashedWheel.this.tickDuration;
        long ticks = Math.max(calculated, this.tick);
        int stopIndex = (int)(ticks & (long)HashedWheel.this.mask);
        return wheel[stopIndex];

    }
    private void excute(long deadTime){
        Bucket bucket = getBucket(deadTime);
        bucket.excute();
        ++this.tick;
    }
}
