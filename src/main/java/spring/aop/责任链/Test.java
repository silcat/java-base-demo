package spring.aop.责任链;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.aop.Aop;
import spring.lifeCycle.LifeCycleConfig;

import java.io.IOException;


public class Test {

    public static void main(String[] args) throws IOException {
        ApplicationContext factory = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        ChainInvoction aop = factory.getBean("chainInvoction",ChainInvoction.class);
        aop.process();
    }


}
