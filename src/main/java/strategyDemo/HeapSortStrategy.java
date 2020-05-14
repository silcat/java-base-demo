package strategyDemo;

import java.util.Arrays;
import java.util.PriorityQueue;


public class HeapSortStrategy {

    private static void buildHeap(int[] data, int index,boolean isMin) {
        //循环建堆

        for (int i = parent(index); i >=0; i--) {
            down(data,i,isMin);
            System.out.println("第" + (i + 1) + "轮排序结果：" + Arrays.toString(data));
        }

    }
    private static void shifUp(int[] data, int index) {

    }
    private static void down(int[] data, int index,boolean isMin) {
        int length = data.length;
        int max = parent(length - 1);
        while (index <= max){
            int parent = index;
            int rightChildren = leftChildren(index) + 1;
            int leftChildren = leftChildren(index);
            int compareChild = leftChildren;
            if (rightChildren<=length-1 && leftChildren<=length-1){
                if (isMin){
                    compareChild = data[leftChildren] < data[rightChildren]  ? leftChildren : rightChildren;
                }else {
                    compareChild = data[leftChildren] > data[rightChildren] && !isMin ? leftChildren : rightChildren;
                }

            }
            if (data[compareChild]>data[parent]){
                if (isMin){
                    break;
                }else {
                    swap(data,parent,compareChild);
                    index = compareChild;
                }
            }else {
                if (isMin){
                    swap(data,parent,compareChild);
                    index = compareChild;
                }else {
                    break;
                }
            }
        }


    }
    private static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }
    private static int parent(int index){
        return (index - 1)/2;
    }
    private static int leftChildren(int index){
        return index*2+1;
    }

    public static void main(String[] args) {
        int[] arr = {9, 6, 7, 4, 5, 3, 2, 1};

        buildHeap(arr,arr.length-1,false);
    }
}
