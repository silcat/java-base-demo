package strategyDemo;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * 自定义规则快排
     */
    static void min(List<String> list ,int start ,int end){
        if (start>= end){
            return;
        }
        int left = start+1;
        int right = end;
        while (left < right){
            while (left<right && ((list.get(left) + list.get(start)).compareTo(list.get(start) + list.get(left)) >= 0)){
                left++;
            }
            while (left<right && ((list.get(right) + list.get(start)).compareTo(list.get(start) + list.get(right)) <= 0)){
                right--;
            }
            String temp = list.get(left);
            list.set(left,list.get(right));
            list.set(right,temp);
        }
        if ((list.get(right) + list.get(start)).compareTo(list.get(start) + list.get(right)) < 0){
            String temp = list.get(start);
            list.set(start,list.get(right));
            list.set(right,temp);
        }else {
            String temp = list.get(start);
            list.set(start,list.get(right-1));
            list.set(right-1,temp);
        }
        min(list,start,left-1);
        min(list,right,end);

    }
    static void min( int[] arr1){
        List<String> list = Lists.newArrayList();
        for (int i = 0;i< arr1.length;i++){
            list.add(String.valueOf(arr1[i]));
        }
        min(list,0,list.size()-1);
        int l =0;
        String left = list.get(0) ;
        while (l+1<list.size()){
            if ((left+list.get(l+1)).compareTo(list.get(l+1)+left)< 0){
                left = left +list.get(l+1);
            }else {
                left=  list.get(l+1)+left;
            }
           l++;
        }
        System.out.println(left);
    }

    public static void main(String[] args) {
//        int[] arr = {5, 6, 5,7,9, 4,5, 3,5, 2, 1};
//        twoSort(arr, 0, arr.length - 1);
//        System.out.println(Arrays.toString(arr));

        int[] arr1 = {6,30,3,21,5};

        min(arr1);

    }
}
