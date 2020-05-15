package strategyDemo;

import java.util.Arrays;

/**
 * 桶排序(Bucket Sort)的原理很简单，它是将数组分到有限数量的桶子里。
 * 假设待排序的数组a中共有N个整数，并且已知数组a中数据的范围[0, MAX)。在桶排序时，创建容量为MAX的桶数组r，并将桶数组元素都初始化为0；将容量为MAX的桶数组中的每一个单元都看作一个"桶"。
 * 时间复杂度为O(n+k),k为取值范围
 */
public class BucketSortStrategy {
    public static void bucketSort(int[] a){
        int[] buckets;
        if(a==null){
            return;
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for(int i=0;i<a.length;i++){
            min = min > a[i] ? a[i] : min;
            max = max < a[i] ? a[i] : max;
        }

        if (min == max){ return; }

        buckets = new int[max - min + 1];

        //计数
        for(int i=0;i<a.length;i++){
            buckets[a[i]-min]++;
        }
        //排序
        for(int i=0,j=0;i<max;i++){
            while((buckets[i]--)>0){
                a[j++] = i + min;
            }
        }
    }
    public static void main(String[] args) {
        int[] arr = {9, 6, 7, 4, 5, 3, 2,2,3 ,1};
        bucketSort(arr);
        System.out.println(Arrays.toString(arr));

    }
}
