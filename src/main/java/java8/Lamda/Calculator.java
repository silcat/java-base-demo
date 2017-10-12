package java8.Lamda;

/**
 * Created by admin on 2017/9/20.
 */
public class Calculator {
    interface IntegerMath {
        int operation(int a, int b);
    }

    public int operateBinary(int a, int b, IntegerMath op) {
        return op.operation(a, b);
    }


}
