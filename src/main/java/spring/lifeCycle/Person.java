package spring.lifeCycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Person implements BeanFactoryAware, BeanNameAware, InitializingBean {

    private String name;
    private String cons;
    private String test ="1";
    public static String sta ="2";


    public Person() {
        cons ="2";
        System.out.println("【构造器】实例化");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("【注入属性】注入属性name");
        this.name = name;
    }


    // 这是BeanFactoryAware接口方法
    @Override
    public void setBeanFactory(BeanFactory arg0) throws BeansException {
        System.out.println("【BeanFactoryAware接口】调用BeanFactoryAware.setBeanFactory()");
    }

    // 这是BeanNameAware接口方法
    @Override
    public void setBeanName(String arg0) {
        System.out.println("【BeanNameAware接口】调用BeanNameAware.setBeanName()");
    }

    // 这是InitializingBean接口方法
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out
                .println("【InitializingBean接口】调用InitializingBean.afterPropertiesSet()");
    }

//    // 这是DiposibleBean接口方法
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("【DiposibleBean接口】调用DiposibleBean.destory()");
//    }

    // 通过<bean>的init-method属性指定的初始化方法
    @PostConstruct
    public void myInit() {
        name = "test";
        System.out.println("【init-method】调用<bean>的init-method属性指定的初始化方法");
    }

    // 通过<bean>的destroy-method属性指定的初始化方法
//    @PreDestroy
//    public void myDestory() {
//        System.out.println("【destroy-method】调用<bean>的destroy-method属性指定的初始化方法");
//    }
}

