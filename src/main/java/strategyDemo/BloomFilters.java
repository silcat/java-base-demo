package strategyDemo;

import com.alibaba.dts.shade.org.apache.commons.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.math.RoundingMode;

/**
 *  需要的位数：n
 *  误判率：p
 *  实际位数(单位bit):m = -nlnp / ((ln2) ^ 2)
 *  hash函数个数: k = (m / n) * ln2
 *
 */
public class BloomFilters {
    private int k;
    private final long[] data;
    private int length;

    public BloomFilters(long n, double p) {
        long m = optimalNumOfBits(n, p);
        k = optimalNumOfHashFunctions(n, m);
        //long为64位，int数组则除32
        length = Ints.checkedCast(LongMath.divide(m, 64, RoundingMode.CEILING));
        data = new long[length];

    }
    public void put(String value){
        //获取16位128bit的hash，方便一次hash执行多次插入
        byte[] bytes = Hashing.murmur3_128().newHasher().putString(value, Charsets.UTF_8).hash().asBytes();
        long hash1 = Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
        long hash2 = Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
        long combinedHash = hash1;
        for (int i = 0 ;i < k;i++ ){
           long unionHash = combinedHash & Long.MAX_VALUE;
           //获取数组下标
            int index =(int) unionHash % length /64  ;
            //获取对应数组的移位
            Long mod = unionHash % length;
            if ((data[index] & 1L<< mod) == 0L){
                data[index] |= 1L<< mod;
                combinedHash += hash2;
            }
        }

    }

    public boolean  mayGet(String value){
        byte[] bytes = Hashing.murmur3_128().newHasher().putString(value, Charsets.UTF_8).hash().asBytes();
        long hash1 = Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
        long hash2 = Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
        long combinedHash = hash1;
        for (int i = 0 ;i < k;i++ ){
            long unionHash = combinedHash & Long.MAX_VALUE;
            //获取数组下标
            int index =(int) unionHash %  length /64 ;
            Long mod = unionHash % length;
            if ((data[index] & 1L<< mod) == 0L){
              return false;
            }
            combinedHash += hash2;
        }
        return true;
    }
        /**
         * 根据需要n和p确定 实际需要的位数
         * @param n
         * @param p
         * @return
         */
    static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }
    /**
     * 根据需要m和n确定hash次数
     * @param n
     * @return
     */
    static int optimalNumOfHashFunctions(long n, long m) {
        // (m / n) * log(2), but avoid truncation due to division!
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }


    public static void main(String[] args) {
        BloomFilters bloomFilters = new BloomFilters(100, 0.0001);
        bloomFilters.put("111");
        boolean b = bloomFilters.mayGet("110");
    }

}
