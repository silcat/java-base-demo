package strategyDemo;

import java.util.Arrays;

public class QuickSortStrategy {
    /**
     *  一.单点快排序
     **/
    public static void sort(int[] a, int start, int end) {
        if (start>= end){
            return;
        }
        int left = start+1;
        int right = end;
        int flag = start;
        while (left < right){
            while (left<right&& a[left]<=a[flag]){
                left++;
            }
            while (left< right&& a[right]>=a[flag]){
                right--;
            }
            swap(a,left,right);
        }
        if (a[flag]>a[right]){
            swap(a,flag,right);
        }else {
            swap(a,flag,right-1);
        }

        sort(a,start,left-1);
        sort(a,left+1,end);
    }

    /**
     *  二.双点快排序
     *  start-> L -> P ->R -> end
     *  L<= x < p：区间等于 基准点
     *  p<= x < end:大于基准点
     *  x<L: 小于基准点
     **/
    public static void twoSort(int[] a, int start, int end) {
        if (start>= end){
            return;
        }
        int left = start;
        int right = end;
        int temp = start+1;
        if (a[start]> a[end]){
            swap(a,start,end);
        }
        int piovdValue = a[start];
        while (temp<right){
            if (a[temp] < piovdValue){
                swap(a,temp,left+1);
                left++;
                temp++;
            }else if (a[temp] > piovdValue){
                while (a[right] > piovdValue&&temp<right){
                    right--;
                }
                swap(a,temp,right);
            }else {
                temp++;
            }
        }
        swap(a,start,left);
        twoSort(a,start,left-1);
        twoSort(a,right,end);
    }

    static void swap(int[] a, int left, int right){
        int temp = a[left];
        a[left] = a[right];
        a[right] = temp;
    }




    public static void main(String[] args) {
        int[] arr = {5, 6, 5,7,9, 4,5, 3,5, 2, 1};
        twoSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
