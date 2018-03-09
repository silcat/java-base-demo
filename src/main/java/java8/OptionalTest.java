package java8;

import java.util.Optional;

/**
 * Created by admin on 2018/3/6.
 */
public class OptionalTest {
    public static void main(String... args) {
        Optional<String> optional = Optional.ofNullable("");
        //若不为空返回true，否则返回false
        boolean notNull = optional.isPresent();
        System.out.println(notNull);
        if (notNull) {
            String value = optional.get();
            System.out.println(value);
            //断言过滤，若正确返回value，否则返回Optional.empty
            Optional<String> predicate = optional.filter(test -> {
                if (test.equals("test")) {
                    return true;
                }
                return false;
            });
            System.out.println(predicate.get());
            //lambda表达式返回值会包装为Optional实例。
            Optional<String> upperName = optional.map((value1) -> value1.toUpperCase());
            System.out.println(upperName.orElse("为null返回默认值"));
        } else {
            //若不为空返回value，否则返回other
            String value = optional.orElse("为null返回默认值");
            System.out.println(value);

        }

    }
}
