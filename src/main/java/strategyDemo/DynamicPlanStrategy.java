package strategyDemo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.omg.CORBA.MARSHAL;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class DynamicPlanStrategy {
    public static int mincoin(int[]nums,int sum){
        int[] dp = new int[sum+1];
        for (int num =0;num<sum+1;num++){
            dp[num] = sum+1;
        }
        dp[0]=0;
        for (int k =1;k<=sum;k++){
            for (int num =0;num<nums.length;num++){
                if (nums[num]<=k){
                    dp[k] = Math.min(dp[k],(1+dp[k-nums[num]])) ;
                }
            }
        }
        return 0;
    }
    public static int zuhe(int[]nums,int sum){
        Map<Integer,List<List<Integer>>> map = Maps.newHashMap();
        map.put(0,Lists.newArrayList());
        for (int k =1;k<=sum;k++){
            List<List<Integer>> totalList = Lists.newArrayList();
            for (int num =0;num<nums.length;num++){
                if (nums[num]<=k){
                    List<List<Integer>> lists = map.get(k - nums[num]);
                    if ((k - nums[num])==0){
                        List<Integer> newList = Lists.newArrayList();
                        newList.add(nums[num]);
                        totalList.add(newList);
                        continue;
                    }
                    if (CollectionUtils.isEmpty(lists)&& (k - nums[num])!=0){
                        continue;
                    }
                    for (List<Integer> list:lists){
                        List<Integer> newList = Lists.newArrayList();
                        newList.add(nums[num]);
                        newList.addAll(list);
                        totalList.add(newList);
                    }
                }
            }
            map.put(k,totalList);
        }
        return 0;
    }

    public static int count(String s){
        if("".equals(s)){
            return -1;
        }

        char[] chars =  s.toCharArray();
        if(chars.length == 1){
            return 1;
        }
        int left = 0;
        int right = 0;
        int maxNum = 0;
        char[] set = new char[256];
        while(left < chars.length && right < chars.length){

            if(set[chars[right]]!= 0 ){
                set[chars[left]]--;
                left++;
            }else{
                set[chars[right]]++;
                right++;

            }

            maxNum = Math.max(maxNum,right-left);
        }
        return maxNum;

    }
    public static int count(int sum ,int[] coins){
        if(sum <= 0)return 0;
        int[] dp = new int[sum+1];
        dp[0] = 1;
        for(int i = 0; i < coins.length; i++) {
            for (int j = coins[i]; j <= sum; j++) {
                dp[j] = dp[j] + dp[j - coins[i]];
            }
        }
            return dp[sum];

    }
    private static void mergeCoins(int n, int[] demo) {
        Scanner scanner = new Scanner(System.in);
        int[] arr = new int[n];
        int[] sum = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = demo[i];
            sum[i] = i == 0 ? arr[i] : arr[i] + sum[i - 1];
        }
        int[][] dp = new int[n][n];
        for(int dunm= 2;dunm<=n;dunm++){
            for(int begin = 0;begin < n - dunm + 1  ;begin++ ){
                int end = begin + dunm -1;
                dp[begin][end] = Integer.MAX_VALUE;
                if(begin == end){
                    dp[begin][end] = 0;
                }else if(end == begin + 1){
                    if(begin == 0){
                        dp[begin][end] = sum[end];
                    }else{
                        dp[begin][end] = sum[end] - sum[begin - 1];
                    }
                }else{
                    dp[begin][end] = Integer.MAX_VALUE;
                    int sumbd = begin == 0 ? sum[end] : sum[end] - sum[begin - 1];
                    for(int index = begin;index < end;index++) {
                        dp[begin][end] = Math.min(dp[begin][end], dp[begin][index] + dp[index + 1][end] + sumbd);
                    }
                }
            }
        }

        System.out.println(dp[0][n - 1]);
    }
    public static void main(String[] args) {

        int[] a = {1,5,10,20,50,100};
//        mincoin(a,11);
//        zuhe(a,4);
//        count("abcabcbb");
        mergeCoins(4,a);
        System.out.print(  count(8845,a));
    }
}
