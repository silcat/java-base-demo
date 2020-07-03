package strategyDemo;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static strategyDemo.TwoPartSortStrategy.sort;

/**
 * 树
 */
public class LinkListStrategy {

   public static Node reverseList(Node head) {
        if (head == null || head.next == null){
            return head;
        }
        Node newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
   }


    public static void main(String[] args) {
        Node head = new Node();
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        head.next = a;
        a.next = b;
        b.next = c;
        c.next =null;
        Node node = reverseList(head);
    }

    private static class Node {
       private Integer value;
       private Node next;

        public Node() {
        }

        public Node(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}
