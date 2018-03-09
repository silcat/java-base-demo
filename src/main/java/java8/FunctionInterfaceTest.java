package java8;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.*;

/**
 * 函数式接口(Functional Interface)就是一个具有具有唯一的一个抽像方法接口(Object里的抽象方法除外)并用@FunctionalInterface的注解函数式接口。
 * 函数式接口可以被隐式转换为lambda表达式,将方法作为参数传参。
 * 函数式接口里允许定义默认方法: default void foo1(){}
 * 函数式接口里允许定义静态方法: static void foo2(){}
 * 函数式接口里允许定义java.lang.Object里的public方法
 * 函数式接口里允许子接口继承多个父接口，但每个父接口中都只能存在一个抽象方法，且必须的相同的抽象方法.
 * 格式：
        @FunctionalInterface
        public interface InterfaceFuntion<T> {
            //必须有
            void interfaceFuntion();
            //可有可无可与抽象方法共存
             default void foo1(){}
             static void foo2(){}
             boolean equals(Object obj)
        }
 * 文档地址：https://docs.oracle.com/javase/8/docs/api/
 */
public class FunctionInterfaceTest {

    public static void main(String... args) {
        ArrayList<Person> peoples = new ArrayList<>();
        peoples.add(new Person(22,"小明","北京",LocalDate.now()) );
        peoples.add(new Person(23,"小花","上海",LocalDate.now().plusDays(1)) );
        functionTest("test",FunctionInterfaceTest::equal);
        functionTest1("test",FunctionInterfaceTest::equal);
        functionTest1("test","test",FunctionInterfaceTest::equal);
        functionTest2(peoples.get(0),peoples.get(1));
        Predicate<ArrayList<Person>> predicate = (p) -> p.get(0).getDateTime().isBefore(p.get(1).getDateTime());
        functionTest2(peoples,predicate);
        Consumer<String> consumer = System.out::println;
        functionTest3("test",consumer);
        functionTest4(Person::new);
    }


    /**
     * 自定义函数式接口
     */
    @FunctionalInterface
    public interface InterfaceFuntion {
        void testEqual(String string);
    }
    private static void functionTest(String begin,InterfaceFuntion func) {
        func.testEqual(begin);
    }

    /**
     * Function相关函数接口
     * Function<T,R>函数式接口:T 入参类型 R 结果类型
     * 实例如下：
     */
    private static void functionTest1(String begin,Function<String,Boolean> func) {
        func.apply(begin);
    }
    //接收两个参数
    private static void functionTest1(String begin, String end,BiFunction<String,String,Boolean> func) {
        func.apply(begin,end);
    }
    /**
     * 算子Operator相关函数接口
     * 输入输出同类型值
     * 实现默认二元函数min(x,y)和max(x,y)用于二元排序
     */
    //接收两个参数
    private static void functionTest2(Person begin, Person end) {
        BinaryOperator<Person> min=BinaryOperator.minBy((o1,o2)->o1.getAge()-o2.getAge());
        System.out.println(min.apply(begin, end));
    }
    /**
     * 谓语动词:Predicate函数接口
     * 推导真假值存在
     */
    private static void functionTest2(ArrayList<Person> peoples , Predicate<ArrayList<Person>> predicate) {
        System.out.println(predicate.test(peoples));
    }
    /**
     * 消费者Consumer函数接口
     * 没有返回值
     */
    private static void functionTest3(String s,Consumer<String> consumer) {
        consumer.accept(s);
    }
    /**
     * 供应者Supplier函数接口
     * 没有入参，只有返回值
     */
    private static void functionTest4(Supplier<Person> s) {
       System.out.print(s.get());
    }


    //测试方法
    private static Boolean equal(String a){
        System.out.println("test".equals(a));
       return "test".equals(a);
    }
    private static Boolean equal(String a,String b){
        System.out.println(b.equals(a));
        return b.equals(a);
    }

    /**
     * MODAL
     */
    private static class Person {
        private Integer age;
        private String name;
        private String provence;
        private LocalDate dateTime;

        public  Person(Integer age, String name, String provence, LocalDate dateTime) {
            this.age = age;
            this.name = name;
            this.provence = provence;
            this.dateTime = dateTime;
        }

        public Person() {

        }

        public LocalDate getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDate dateTime) {
            this.dateTime = dateTime;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvence() {
            return provence;
        }

        public void setProvence(String provence) {
            this.provence = provence;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    ", provence='" + provence + '\'' +
                    ", dateTime=" + dateTime +
                    '}';
        }


    }

}
