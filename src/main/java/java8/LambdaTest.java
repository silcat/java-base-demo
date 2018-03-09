package java8;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 *  语法:() -> {}代码块替代
 *       () -> statement         无参数单语句
 *       () -> {statements}        无参数多语句
 *       (params) -> statement      单语句
 *       (params) -> { statements } 多语句
 */
public class LambdaTest {

    public static void main(String... args) {
        ArrayList<Person> peoples = new ArrayList<>();
        peoples.add(new Person(22,"小明","北京",LocalDate.now()) );
        peoples.add(new Person(23,"小花","上海",LocalDate.now().plusDays(1)) );
        peoples.add(new Person(24,"小灰","杭州",LocalDate.now().minusDays(1)));

        /**
         * 替代匿名内部类
         */
        new Thread(()->System.out.println("无参且单语句lambda表达式")).start();
        Collections.sort(peoples,(o1,o2)-> {return compareDateTime(o1,o2);});
        /**
         * 替代foreach遍历list：mapfilter
         */
        peoples.stream().filter((person)->person.age>23).forEach(person ->System.out.println("过滤年龄大于23:"+person.toString()) );
        peoples.stream().map((person)->{person.setAge(person.age+1);return person;}).forEach(person ->System.out.println("返回年龄加1的对象:"+person.toString()) );
        peoples.stream().filter(person -> person.age==23).map((person)->person.age+1).forEach(person ->System.out.println("年龄加1，仅返回age:"+person.toString()) );
        /**
         * 方法引用
         */
        //静态方法引用
        peoples.stream().filter(person -> person.age==25).forEach(people ->System.out.println(Person.staticMethodUse()));
        //实例方法引用
        peoples.stream().filter(person -> person.age==25).forEach(people->System.out.println(people.methodUseWithParam(people)));

        //函数接口引用
        //对象调用
        peoples.stream().filter(person -> person.age==25).forEach(people->System.out.println(people.funcInterface(people::funcUse,people)));
        //类调用静态方法
        peoples.stream().filter(person -> person.age==25).forEach(people->System.out.println(Person.funcInterface(people::funcUse,people)));
        //构造方法引用
        peoples.stream().filter(person -> person.age==25).forEach(people->System.out.println(Person.funcInterface(people::funcUse,people)));
        MyClassFunc myClassFunc= Person::new;
        Person person = myClassFunc.func(22, "小明", "北京", LocalDate.now());
        System.out.print("构造方法引用:"+person.toString());
    }


    private static Integer compareDateTime(Person o1,Person o2) {
        if (o1.getDateTime().isBefore(o2.getDateTime())){
            return 1;
        }else if (o1.getDateTime().isAfter(o2.getDateTime())){
            return -1;
        }else {
            return  0;
        }
    }

    /**
     * 函数式接口
     */
    @FunctionalInterface
    public interface StringFunc {
        String func(Person person);
    }

    @FunctionalInterface
    public interface MyClassFunc {
        Person func(Integer age, String name, String provence, LocalDate dateTime);
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

        public static String funcInterface(StringFunc stringFunc,Person person){
            return stringFunc.func(person);
        }
        public  String funcUse(Person person){
            return"函数式方法引用"+person.toString();
        }
        public  String methodUseWithParam(Person person){
            return"带参数的实例方法引用"+person.toString();
        }
        public static String staticMethodUse(){
            return"静态方法引用";
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
