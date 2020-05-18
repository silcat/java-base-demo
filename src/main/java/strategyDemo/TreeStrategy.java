package strategyDemo;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static strategyDemo.TwoPartSortStrategy.sort;

/**
 * 树
 */
public class TreeStrategy {
    static List<Integer> res = new LinkedList<>();
    static List<List<Integer>> levelRes = new LinkedList<>();
    /**
     * 二叉查找树
     */
    public static Node intSearchTree(int[] nums,int start,int end ,Node root){
        if (start > end){
            return null;
        }else {
            int m=start+ (end-start)/2;
            root=new Node(nums[m]);
            root.left=intSearchTree(nums,start,m-1,root.left);
            root.right=intSearchTree(nums,m+1,end,root.right);
        }
        return root;
    }

    /**
     * 前序遍历
     */
    public static void preOrder(Node root){
        if (root == null){
            return;
        }
        res.add(root.getData());
        preOrder(root.getLeft());
        preOrder(root.getRight());

    }
    public static void preOrderTwo(Node root){
        Stack<Node> stack = new Stack();
        if (root == null){
            return;
        }
        stack.push(root);
        while (!stack.empty()){
            Node node = stack.pop();
            res.add(node.getData());
            if (node.getRight() != null){
                stack.push(node.getRight());
            }
            if (node.getLeft() != null){
                stack.push(node.getLeft());
            }
        }
    }

    class LRUCache extends LinkedHashMap<Integer, Integer> {
        LinkedHashMap<Integer, Integer> hashMap;
        int cap = 0;


        public LRUCache(int capacity) {
            hashMap =  new LinkedHashMap<Integer,Integer>(2);
            cap = capacity;
        }

        public int get(int key) {
            Integer value = hashMap.get(key);
            if(value == null){
                return -1;
            }
            return value;

        }

        public void put(int key, int value) {
            hashMap.put(key,value);
        }
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            if(cap + 1 == hashMap.size()){
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * 中序遍历
     */
    public static void middlerOrder(Node root){
        if (root == null){
            return;
        }
        middlerOrder(root.getLeft());
        res.add(root.getData());
        middlerOrder(root.getRight());

    }

    /**
     * 后序遍历
     */
    public static void afterOrder(Node root){
        if (root == null){
            return;
        }
        afterOrder(root.getLeft());
        afterOrder(root.getRight());
        res.add(root.getData());

    }
    /**
     * 层序遍历
     */
    public static void levelOrder(Node root){
        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue();
        queue.offer(root);
        while (!queue.isEmpty()){
            int size = queue.size();
            List<Integer> list = new ArrayList();
            for (int i = 0;i<size;i++){
                Node poll = queue.poll();
                list.add(poll.getData());
                if (poll.getLeft()!= null){
                    queue.offer(poll.getLeft());
                }
                if (poll.getRight()!= null){
                    queue.offer(poll.getRight());
                }
            }
            levelRes.add(list);
        }
    }




    public static void main(String[] args) {
        int[] arr = {1,3,5,6,7,8,9,11};
        sort(arr, 0, arr.length - 1);
        Node node = intSearchTree(arr, 0, arr.length - 1, new Node());
        preOrder(node);
        System.out.println("前序遍历结果："+res.toString());
        res.clear();
        preOrderTwo(node);
        System.out.println("前序遍历结果："+res.toString());
        res.clear();
        middlerOrder(node);
        System.out.println("中序遍历结果："+res.toString());
        res.clear();
        afterOrder(node);
        System.out.println("后序遍历结果："+res.toString());
        levelOrder(node);
        System.out.println("层序遍历结果："+levelRes.toString());


    }

    private static class Node {
        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        Integer data;
        Node left;
        Node right;

        Node(int data) {
            this.data = data;
        }
        Node() {
        }
    }
}
