package strategyDemo;

import com.google.common.hash.*;

public class BloomFilters {
   static BitMap bitMap;
   static int hashSize = 1;

    public BloomFilters(long n, double p) {
        long bitSize = optimalNumOfBits(n, p);
        hashSize = optimalNumOfHashFunctions(n, bitSize);
        bitMap = new BitMap((int)bitSize,1);
    }

    public static void put(int num) {
        for (int i = 0; i < hashSize; i++) {
            bitMap.put(getHash(num,i));
        }
    }
    public static boolean contain(int num) {
        for (int i = 0; i < hashSize; i++) {
            if (!bitMap.contain(getHash(num,i))){
                return false;
            }
        }
        return true;
    }

    //模拟hash，忽略hash分布是否合理
    public static int getHash(int num ,int time){
        return num + time*10;
    }
    /**
     * 需要的位数
     */
    static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * hash函数个数
     */
    static int optimalNumOfHashFunctions(long n, long m) {
        // (m / n) * log(2), but avoid truncation due to division!
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    public static void main(String[] args) {
        BloomFilters bloomFilters = new BloomFilters(100, 0.0001);
        put(1);
        boolean contain = contain(1);
        boolean contain1 = contain(2);
    }
}
