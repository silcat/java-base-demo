package strategyDemo;


public class ChafenShuzuStrategy {

    /**
     * 路由器 k 为接收最少距离
     */
    public static int max(int[] a, int n,int k){
        //前缀和数组
        int[] b = new int[n];
        // 差分数组
        int[] c = new int[n];
        for(int i=0; i < n; i++) {
            int l = Math.max(i-a[i], 0);
            int r = Math.min(i+a[i], n-1);
            c[l] += 1;
            if(r+1 < n) {
                c[r+1] -= 1;
            }
        }
        int res = 0;
        // 通过C数组重构B数组
        for(int i=0; i < n; i++) {
            if(i > 0){
                b[i] = c[i] + b[i-1];
            } else{
                b[0] = c[0];
            }

            if(b[i] >= k) {
                res++;
            }

        }
        System.out.println(res);
        return res;
    }

    /**
     * 数组求和
     */
    public static int sum(int[] nums, int start,int end){
        //前缀和数组
        int[] sum=new int[nums.length+1];
        for(int i=0;i<nums.length;i++){
            sum[i+1]=sum[i]+nums[i];
        }
        return sum[end+1]-sum[start];
    }


    public static void main(String[] args) {
        int[] a ={2,3,4,5};
        max(a,a.length,3);

    }


}
