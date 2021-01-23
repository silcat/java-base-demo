package strategyDemo;

/**
 * 差分数组：假如范围i到j的数组值加c，正常遍历o(n),差分数组通过b(i)+c与b(j+1)-c o(1)完成赋值;
 * 原数组 a[]
 * 差分数组 c[i] = a[i] - a[i -1]
 * 前缀和数组b(i) = a[0] + ...a[i] = b[i-1]+a[i];
 * 范围求和: b(i,j) =b(j) - b(i-1)
 */
public class ChafenShuzuStrategy {

    /**
     * 路由器 k 为接收最少距离
     */
    public static int max(int[] a, int n,int k){
        //最大接收数组
        int[] aCep = new int[n];
        int[] c = new int[n+1];
        for(int i=0; i < n; i++) {
            c[Math.max(i-a[i], 0)] ++;
            c[Math.min(i+a[i]+1, n)] --;
        }
        int res = 0;
        // 通过C数组重构aCep数组
        for(int i=0; i < n; i++) {
            if(i > 0){
                aCep[i] = c[i] + aCep[i-1];
            } else{
                aCep[0] = c[0];
            }

            if(aCep[i] >= k) {
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

    /**
     * 最大站台
     */
    public static void mState( int[] c, int start,int end){
            c[start]++;
            c[end]--;
    }
    /**
     * 最大站台
     */
    public static void mState( ){
        int[] c = new int[255];
        int[] a = new int[255];
        mState(c,4,7);
        mState(c,2,6);
        mState(c,2,5);
        mState(c,1,2);
        // 通过C数组重构aCep数组
        int max = -1;
        for(int i=0; i < c.length; i++) {
            if(i > 0){
                a[i] = c[i] + a[i-1];
            } else{
                a[0] = c[0];
            }
             max = Math.max(max, a[i]);
        }
        System.out.print(max);
    }


    public static void main(String[] args) {
        int[] a ={1,1,1,1};
        max(a,a.length,3);
        mState( );

    }


}
