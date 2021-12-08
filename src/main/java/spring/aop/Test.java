package spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import spring.lifeCycle.LifeCycleConfig;
import spring.lifeCycle.Person;

import java.io.IOException;


public class Test {

    public static void main(String[] args) throws IOException {
        ApplicationContext factory = new AnnotationConfigApplicationContext(LifeCycleConfig.class);

        Aop aop = factory.getBean("aop",Aop.class);
        aop.setName("test");
        aop.test();

    }


}
