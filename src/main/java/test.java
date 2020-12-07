

public class test {

    public static void main(String[] args)  {
        int c = -1 << 29;
        int RUNNING = -1 << 29;
        int CAPACITY   = (1 << 29) - 1;
//        print(RUNNING);
//        print(CAPACITY);
//        print(0 << 29);
//        print(1 << 29);
//        print(2 << 29);
//        print(3 << 29);


//        print(RUNNING&CAPACITY);
        print(c+1);
        print(~CAPACITY);
        print(c+1 & ~CAPACITY);
//        print((c+1)&CAPACITY);

    }
    private static void print( Integer num){
        System.out.println(num);
        System.out.println(Integer.toBinaryString(num));
    }
}
