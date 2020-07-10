package strategyDemo;

import java.util.Arrays;

public class TwoPartSortStrategy {
    /**
     *  一.归并模板
     * void merge_sort(一个数组) {
     *     if (可以很容易处理) return;
     *     merge_sort(左半个数组);
     *     merge_sort(右半个数组);
     *     merge(左半个数组, 右半个数组);
     *    }
     *  例子：归并排序
     **/
    public static int[]  sort(int[] a, int left, int right) {
        if (right <= left){
            return a;
        }
        int middle = left+ (right-left)/2;
        sort(a, left, middle);
        sort(a, middle+1, right);
        return merge(a,left,middle,right);
    }

    private static int[] merge(int[] a, int left, int middle, int right) {
        int[] temp = new int[right-left+1];
        int i = left;
        int index = 0;
        int j = middle + 1 ;
        while (i<= middle && j<= right){
            if (a[i] > a[j]){
                temp[index++] = a[j++];
            }else {
                temp[index++] = a[i++];
            }
        }
        while (i <= middle){
            temp[index++] = a[i++];
        }
        while (j <= right){
            temp[index++] = a[j++];
        }
        for (int t=0;t<temp.length;t++ ){
            a[left+t] = temp[t];
        }
        return a;
    }

    /**
     *  二分查找
     **/
    public static int search(int[] arr, int data) {

        int min = 0;
        int max = arr.length - 1;
        int mid;
        while (min <= max) {
            // 防止溢出
            mid =  min + (max - min) / 2;
            if (arr[mid] > data) {
                max = mid - 1;
            } else if (arr[mid] < data) {
                min = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = {9, 6, 7, 4, 5, 3, 2, 1};
        sort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
