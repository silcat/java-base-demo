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
    public static void main(String[] args) {
        int[] a = {1,2,3};
        mincoin(a,11);
        zuhe(a,4);
    }
}
