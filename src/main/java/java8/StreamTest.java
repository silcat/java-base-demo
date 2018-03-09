package java8;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  Java.util.Stream 流
 *  中间操作(只对操作做记录，不消费流)
 *         无状态(不依赖上个值) ：filter(过滤)，map(重新计算赋值),flatMap(和map一致，流进行扁平化)，peek(遍历操作)
 *         有状态(依赖所有值) ：distinct(去重)，sorted（排序），limit(返回前面n个元素)，skip（扔掉前 n 个元素）
 *  结束操作(对流进行计算,消费流）:
 *         短路操作(不依赖上个值) ：foreach(遍历操作)，reduce(元素组合)，toArray（将流转换为其他数据结构），collect(s收集器)，count(总数计算),min(最小值)，max(最大值)
 *         非短路操作(依赖所有值) ：allMatch：Stream 中全部元素符合传入的 predicate，返回 true
                                    anyMatch：Stream 中只要有一个元素符合传入的 predicate，返回 true
 *                                  noneMatch：Stream 中没有一个元素符合传入的 predicate，返回 true
 */
public class StreamTest {
    public static void main(String... args) {
        /**
         * 初始化参数与方法
         */
        ArrayList<Person> peoples = new ArrayList<>();
        peoples.add(new Person(22,"小明","北京", LocalDate.now()) );
        peoples.add(new Person(23,"小花","上海",LocalDate.now().plusDays(1)) );
        Consumer<Person> out = System.out::println;
        Consumer<Map> out1 = System.out::println;
        Consumer<String> out2 = System.out::println;
        Comparator<Person> comparator = (o1, o2) -> {
            if (o1.getDateTime().isBefore(o2.getDateTime())) {
                return 1;
            } else if (o1.getDateTime().isAfter(o2.getDateTime())) {
                return -1;
            } else {
                return 0;
            }
        };

        /**
         * 流的构造
         */
        //并行流:多线程
        Stream<Person> parallelStream = peoples.parallelStream();
        //串行流：单线程
        Stream<Person> stream0 = peoples.stream();
        Stream<List<Person>> stream9=Stream.of(peoples);
        //自定义流:实现Supplier<T> 接口
        Stream.generate(new Person()).limit(2).forEach(System.out::println);

        /**
         * 过滤
         */
        stream0 = peoples.stream();
        stream0.filter(person -> person.getAge()<24).forEach(out);

        Stream<Person> stream1 = peoples.stream();
        stream1.limit(1).forEach(out);;

        Stream<Person> stream2 = peoples.stream();
        stream2.skip(1).forEach(out);;

        /**
         * 收集器collect
         */
        //结果变为list
        Stream<Person> stream3 = peoples.stream();
        stream3.collect(Collectors.toList()).forEach(out);
        //结果变为set
        Stream<Person> stream4 = peoples.stream();
        System.out.println(stream4.collect(Collectors.toSet()));
        //list变map
        Stream<Person> stream5 = peoples.stream();
        out1.accept(stream5.collect(Collectors.toMap(p->p.getAge(),p->p)));

        /**
         * map与flagMap,降维度
         */
        //经过flatMap变换，Stream<String[]>就变为了Stream<String>，由二维变为了一维
        String [] strs1 = {"a","b","c"};
        String [] strs2 = {"d","e","f"};
        String [] strs3 = {"a","g","h"};
        Arrays.asList(strs1,strs2,strs3).stream().flatMap(str -> Stream.of(str)).map(s -> s+",").forEach(out2);//str为单个值
        Arrays.asList(strs1,strs2,strs3).stream().map(str -> str).map(s -> s+",").forEach(out2);//str为数组
        /**
         * 排序
         */
        Stream<Person> stream6 = peoples.stream();
        stream6.sorted(comparator).forEach(out);

    }


    /**
     * MODAL
     */
    private static class Person implements Supplier<Person> {
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

        /**
         * Gets a result.
         *
         * @return a result
         */
        @Override
        public Person get() {
            return new Person(22,"TEST","TEST", LocalDate.now()) ;
        }
    }
}
