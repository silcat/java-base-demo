package strategyDemo;

import java.util.Arrays;

/**
 * 桶排序(Bucket Sort)的原理很简单，它是将数组分到有限数量的桶子里。
 */
public class BucketSortStrategy {
    /**
     * 桶排序
     * @param a
     */
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

    /**
     * 排序后最大相邻数差值
     * @param nums
     * @return
     */
    public static int maxGap(int[] nums){
        // write code here
        if (nums == null || nums.length < 2) {
            return 0;
        }
        int len = nums.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            min = Math.min(min, nums[i]);
            max = Math.max(max, nums[i]);
        }
        if (min == max) {
            return 0;
        }
        //每个桶里是否有值
        boolean[] hasNum = new boolean[len + 1];
        //每个桶里的最大值
        int[] maxs = new int[len + 1];
        //每个桶里的最小值
        int[] mins = new int[len + 1];
        int bid = 0;
        for (int i = 0; i < len; i++) {
            bid = bucket(nums[i], len, min, max);
            mins[bid] = hasNum[bid] ? Math.min(mins[bid], nums[i]) : nums[i];
            maxs[bid] = hasNum[bid] ? Math.max(maxs[bid], nums[i]) : nums[i];
            hasNum[bid] = true;
        }
        int res = 0;
        int lastMax = maxs[0];
        int i = 1;
        for (; i <= len; i++) {
            if (hasNum[i]) {
                res = Math.max(res, mins[i] - lastMax);
                lastMax = maxs[i];
            }
        }
        return res;
    }
    public static int bucket(long num, long len, long min, long max) {
        return (int) ((num - min) * len / (max - min));
    }
    public static void main(String[] args) {
        int[] arr = {1,6,1,2,4,7};
        bucketSort(arr);
        maxGap(arr);
        bucketSort(arr);
        System.out.println(Arrays.toString(arr));

    }
}
