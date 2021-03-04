package bigdata;


public class BitMap {
    private static int[] bitsArray;
    private static int length;
    //占用位数
    private static int bitSize;

    public BitMap(int n ,int bit) {
        bitSize = bit;
        length = n /( 32 / bitSize);
        bitsArray = new int[length];
    }

    public  void push(int n){
        int count = getNum(n)+1;
        if (count > (1<<bitSize)-1){
            return;
        }
        //将位数清零
        print(~((((1<<bitSize) -1) << bitSize * getOffese(n)) & (int)4294967295L));
        bitsArray[getIndex(n)] &= ~((((1<<bitSize) -1) << bitSize * getOffese(n)) & (int)4294967295L);
        print(bitsArray[getIndex(n)]);
        print(((count & ((1<<bitSize) -1)) << bitSize * getOffese(n))& (int)4294967295L);
        //重新赋值
        bitsArray[getIndex(n)] |=(((count & ((1<<bitSize) -1)) << bitSize * getOffese(n))& (int)4294967295L);
        print(bitsArray[getIndex(n)]);

    }
    private  void print( Integer num){
//        System.out.println(num);
//        System.out.println(Integer.toBinaryString(num));
    }
    public  int getNum(int num){
        return (bitsArray[getIndex(num)] &(((1<<bitSize) -1) << bitSize * getOffese(num)))>> bitSize * getOffese(num);
    }
    public boolean contain(int num){
        return  (bitsArray[getIndex(num)] & ((1<<bitSize) -1) << bitSize * getOffese(num)) != 0;
    }
    private static int getOffese(int n){
        return n % (32/ bitSize) ;
    }
    private static int getBits(int n){
        return bitsArray[getIndex(n)] ;
    }
    private static int getIndex(int n){
        return n / (32/ bitSize);
    }

    public static void main(String[] args) {
        int n =10000;
        BitMap bitMap = new BitMap(n, 2);
        for (int i=3;i<n;i++){
            bitMap.push(i);
        }
        boolean contain = bitMap.contain(2);
        int num = bitMap.getNum(2);
    }

}
