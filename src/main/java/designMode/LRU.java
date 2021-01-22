package designMode;

import lombok.*;
import lombok.Builder;
import strategyDemo.TreeStrategy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class LRU {
    /**
     * 自定义
     */
    @NoArgsConstructor
    @Getter
    @Setter
    class Node{
        int key;
        int value;
        int hash;
        Node next;
        Node prev;
        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    class DoubleList {
        private Node head, tail; // 头尾虚节点
        private int size; // 链表元素数
        public DoubleList() {
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
            tail.prev = head;
            size = 0;
        }
        // 在链表头部添加节点 x
        public void addFirst(Node x) {
            x.next = head.next;
            x.next.prev = x;
            head.next = x;
            x.prev = head;
            size++;
        }
        // 删除链表中的 x 节点（x 一定存在）
        public void remove(Node x) {
            x.prev.next = x.next;
            x.next.prev = x.prev;
            size--;
        }

        // 删除链表中最后一个节点，并返回该节点
        public Node removeLast() {
            if (tail.prev == head){
                return null;
            }
            Node last = tail.prev;
            remove(last);
            return last;
        }
        // 返回链表长度
        public int size() { return size; }
    }

    public class LRUZD {
        HashMap<Integer,Node> map;
        DoubleList list;
        int cap = 0;

        public LRUZD(int cap) {
             map = new HashMap(cap);
             list = new DoubleList();
             this.cap = cap;
        }

        public int get(int key){
            if (!map.containsKey(key)){
                return -1;
            }
            Node node = map.get(key);
            put(key,node.value);
            return node.value;
        }

        public void put(int key,int value){
            Node x = new Node(key, value);
            if (map.containsKey(key)){
                Node node = map.get(key);
                list.remove(node);
                list.addFirst(x);
                map.put(key,x);
            }else {
                if (cap == list.size()){
                    list.removeLast();
                }
                // 直接添加到头部
                list.addFirst(x);
                map.put(key, x);
            }
        }
    }

    /**
     * linkedHashMap实现
     */
    class LRUCache extends LinkedHashMap<Integer, Integer> {
        int cap = 0;
        public LRUCache(int capacity) {
            super(capacity, 0.75F, false);
            cap = capacity;
        }

        public int get(int key) {
            int result = super.getOrDefault(key,-1);
            if(result == -1){
                return result;
            }
            super.remove(key);
            super.put(key,result);
            return result;
        }

        public void put(int key, int value) {
            if(super.containsKey(key)){
                super.remove(key);
            }
            super.put(key,value);
        }
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return size() > cap;
        }
    }

    public static void main(String[] args) {
        LRUZD cache = new LRU().new LRUZD(2);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);       // 返回  1
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        cache.get(2);       // 返回 -1 (未找到)
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        cache.get(1);       // 返回 -1 (未找到)
        cache.get(3);       // 返回  3
        cache.get(4);       // 返回  4
    }
}
