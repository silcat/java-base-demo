package java8.Lamda;


import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
public class LambdaTest {

    public static void main(String... args) {
        new Thread(() -> System.out.println("Hello from thread")).start();
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        list.forEach(n -> System.out.println(n));
    }
}
