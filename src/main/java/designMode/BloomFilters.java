package designMode;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class BloomFilters {



    public static void main(String[] args) {
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(),1000,0.001);
        bloomFilter.put(1);
        boolean contain = bloomFilter.mightContain(1);
        boolean put = bloomFilter.put(2);
        boolean b = bloomFilter.mightContain(3);

    }
}
