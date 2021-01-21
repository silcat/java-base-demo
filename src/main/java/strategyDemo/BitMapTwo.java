package strategyDemo;


public class BitMapTwo {
    private static int[] bitsArray;

    public static void init(int n) {
        bitsArray = new int[1+n/32];
    }

    public static void push(int n){
        int bits = getBits(n);
        int offset = (int) n & 31;
        //0x00000001等于1
        bits = bits | 0x00000001 << offset;
        int index = (int) n>>5;
        bitsArray[index] = bits;

    }

    public static boolean isExist(int n){
        int bits = getBits(n);
        int offset = (int) n & 31;
        //0x00000001等于1
        int i = bits & 0x00000001 << offset;
        return i==0?false:true;
    }

    public static int getBits(int n){
        int index = (int) n>>5;
        return bitsArray[index] ;
    }

    public static int[] sort(int[] arr){
        int[] sort = new int[arr.length];
        init(arr.length);
        for (int i =0 ;i< arr.length;i++){
            push(arr[i]);
        }
        int index = 0;
        for (int i =0 ;i< bitsArray.length;i++){
            for (int j =0 ;j< 32;j++){
                if (isExist(j)){
                    sort[index] = 32*i+j;
                    index ++;
                }
            }

        }
        return sort;
    }

    public static void main(String[] args) {
        int[] arr = {9, 6, 7, 4, 5, 3, 2, 1};
        int[] sort = sort(arr);
    }
}
