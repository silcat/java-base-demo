package strategyDemo;

import java.util.Arrays;

public class HeapSortStrategy {
    /**
     * topK
     */
    public static void maxK(int[] arr, int k, boolean b) {
        int arrayLength = arr.length;
        buildHeap(arr, k-1 ,!b);
        for (int i=k;i<arrayLength;i++){
            if (b){
                int max = arr[0];
                if (max > arr[i]){
                    swap(arr,i,0);
                    buildHeap(arr, k-1 ,!b);
                }
            }else {
                int min = arr[0];
                if (min< arr[i]){
                    swap(arr,i,0);
                    buildHeap(arr, k-1 ,!b);
                }
            }

        }
        System.out.println("TOP K 小/大的值为：" + Arrays.toString(arr));

    }

    /**
     * 堆排序
     */
    public static void sort(int[] arr, boolean b) {
          int arrayLength = arr.length;
//        buildHeap(arr, arrayLength - 1 ,b);
//        //循环建堆
//        for (int i = 0; i < arrayLength-1; i++) {
//            //交换堆顶和最后一个元素
//            swap(arr, 0, arrayLength - 1 - i);
//            if (i < arrayLength-2){
//                down(arr,arrayLength - 2 - i,0,b);
//            }
//            System.out.println("第" + (i + 1) + "轮排序结果：" + Arrays.toString(arr));
//        }
        for (int i = 0; i < arrayLength - 1; i++) {
            //建堆
            buildHeap(arr, arrayLength - 1 - i,b);
            //交换堆顶和最后一个元素
            swap(arr, 0, arrayLength - 1 - i);
            System.out.println("第" + (i + 1) + "轮排序结果：" + Arrays.toString(arr));
        }
    }

    /**
     * 建堆
     */
    private static void buildHeap(int[] data, int lastIndex,boolean isMin) {
        for (int i = parent(lastIndex); i >=0; i--) {
            down(data,lastIndex, i,isMin);
            System.out.println("第" + (i + 1) + "轮排序结果：" + Arrays.toString(data));
        }
    }

    /**
     *上浮
     */
    private static void shifUp(int[] data,  int lastIndex, int index, boolean isMin) {
        while (0< index ){
            int parent = parent(index);
            int parentValue = data[parent];
            int shifUpvalue = data[index];
            if (isMin){
                if (parentValue<shifUpvalue){
                    break;
                }
                swap(data,parent,index);
                index = parent;
            }else {
                if (parentValue>shifUpvalue){
                    break;
                }
                swap(data,parent,index);
                index = parent;
            }
        }

    }

    /**
     * 下沉
     */
    private static void down(int[] data, int lastIndex, int index, boolean isMin) {
        int max = parent(lastIndex);
        while (index <= max){
            int parent = index;
            int rightChildren = leftChildren(index) + 1;
            int leftChildren = leftChildren(index);
            int compareChild = leftChildren;
            if (rightChildren<=lastIndex && leftChildren<=lastIndex){
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
        maxK(arr,3,false);
        maxK(arr,3,true);
        sort(arr,true);
        buildHeap(arr,arr.length-1,true);
    }
}
