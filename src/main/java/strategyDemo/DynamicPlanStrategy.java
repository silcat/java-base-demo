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

    /**
     *有一个长为n的数组A，求满足0≤a≤b<n的A[b]-A[a]的最大值。
     * 给定数组A及它的大小n，请返回最大差值
     */
    public static int getDis(int[] A, int n){
        int res = 0;
        int currMin = A[0];
        for (int i = 1; i < A.length; i++) {
            res = Math.max(res, A[i] - currMin);
            currMin = Math.min(currMin, A[i]);
        }
        return res;

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

    /**
     * 现在有一个城市销售经理，需要从公司出发，去拜访市内的商家，已知他的位置以及商家的位置，但是由于城市道路交通的原因，他只能在左右中选择一个方向，在上下中选择一个方向，现在问他有多少种方案到达商家地址。
     * 给定一个地图map及它的长宽n和m，其中1代表经理位置，2代表商家位置，-1代表不能经过的地区，0代表可以经过的地区，请返回方案数，保证一定存在合法路径。保证矩阵的长宽都小于等于10。
     */
    //思路：方案增多的可能性在于沿x/y两个路线,所以要考虑两个方向的和
    public int countPath(int[][] map, int n, int m) {
        int startX = 0;             //起点(经理)坐标
        int startY = 0;
        int endX = 0;               //终点(商家)坐标
        int endY = 0;
        int[][] plan = new int[n][m];           //走到当前点的方案数

        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                if(map[i][j] == 1){             //标记起点
                    startX = i;
                    startY = j;
                }
                if(map[i][j] == 2){             //标记终点
                    endX = i;
                    endY = j;
                }
            }
        }

        int dirX = startX > endX ? -1 : 1;       //确定沿X、Y的移动方向!
        int dirY = startY > endY ? -1 : 1;

        for(int x = startX; x != endX + dirX; x += dirX){           //从起点沿着指定方向一直寻找终点,注:x、y各往后走一步,保证走到终点(否则还差终点一步的时候会跳出循环)!
            for(int y = startY; y != endY + dirY; y += dirY){
                if(x == startX && y == startY){         //初始化起点有一种方案
                    plan[x][y] = 1;
                }else if(x == startX){                  //X外边界初始化(X固定,方案等于上次Y方案)
                    plan[x][y] = (map[x][y] == -1) ? 0 : plan[x][y-dirY];
                }else if(y == startY){                  //Y外边界初始化
                    plan[x][y] = (map[x][y] == -1) ? 0 : plan[x-dirX][y];
                }else{
                    //当前可达时,方案数=上一点(注意是-)沿x方向走到当前点的方案数+上一点沿y方向走到当前点的方案数
                    plan[x][y] = (map[x][y] == -1) ? 0 : plan[x-dirX][y] + plan[x][y-dirY];         //等于-1:当前点不可达,方案数为0;否则可达,方案数为之前两个方向方案和
                }
            }
        }
        return plan[endX][endY];
    }
    public static void main(String[] args) {

        int[] a = {10,5,10,7,67,50};
//        mincoin(a,11);
//        zuhe(a,4);
//        count("abcabcbb");
//        mergeCoins(4,a);
        int dis = getDis(a, 1);
        System.out.print( dis);

    }
}
