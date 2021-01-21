package strategyDemo;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class HeapStrategy {

    /**
     * 双堆维护中位数
     */
    public static int[] getMiddle(int[] arr) {
        int[] res = new int[arr.length];
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        };
        PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(arr.length, comparator);
        PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>(arr.length);
        for (int i = 0;i<arr.length;i++){
            int value = arr[i];
            if (i%2==0){
                if (!maxHeap.isEmpty()&&value<maxHeap.peek()){
                    minHeap.offer(maxHeap.poll());
                    maxHeap.offer(value);
                }else {
                    minHeap.offer(value);
                }
                res[i] = minHeap.peek();
            }else {
                if (!minHeap.isEmpty()&&value>minHeap.peek()){
                    maxHeap.offer(minHeap.poll());
                    minHeap.offer(value);
                }else {
                    maxHeap.offer(value);
                }
                res[i] = maxHeap.peek();
            }

        }
        return res;
    }

    /**
     * K路归并，合并多个已经排序数组

     */

    public static ArrayList kMerge(int[][] arr){
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i=0; i< arr.length;i++){
             Arrays.sort(arr[i]);
        }
        PriorityQueue<NewInteger> queue = new PriorityQueue<>(arr.length, ((o1, o2) -> {
            return o1.value < o2.value ? -1 : 1;
        }));

        for (int i=0; i< arr.length;i++){
            queue.offer(new NewInteger(arr[i][0],i,0));
        }
        while (!queue.isEmpty()){
            NewInteger poll = queue.poll();
            list.add(poll.value);
            if (poll.col < arr[poll.row].length-1){
                queue.offer(new NewInteger(arr[poll.row][poll.col+1],poll.row,poll.col+1));
            }
        }
        return list;
    }
    public static class NewInteger {
        int value, row, col;
        public NewInteger(int value, int row, int col) {
            this.value = value;
            this.row = row;
            this.col = col;
        }
    }
    public static void main(String[] args) {
        int[] arr = {9, 6, 7, 4, 5, 3, 2, 1};
        int[][] arrs ={{3, 1, 5, 7, 0, 11, 13},{2, 4, 6, 8, 10}};
        int[] middle = getMiddle(arr);
        ArrayList arrayList = kMerge(arrs);

    }
}
