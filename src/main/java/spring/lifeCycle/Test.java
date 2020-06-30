package spring.lifeCycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;


public class Test {
    /**
     *
     * 创建AnnotationConfigApplicationContext :主要为准备创建类的数据和相关方法
    *  1.代理 DefaultListableBeanFactory（实际容器），注入流程处理类（BeanFactoryPostProcessor），加载配置
     * 2.创建DefaultListableBeanFactory：1.RootBeanDefinitionMAP（所有类的元数据）2.流程处理类（BeanPostProcessor）
     */
    //现在开始初始化容器
    //spring.lifeCycle.MyBeanFactoryPostProcessor - 这是BeanFactoryPostProcessor实现类构造器！！
    //BeanFactoryPostProcessor调用postProcessBeanFactory方法
    //这是BeanPostProcessor实现类构造器！！
    //InstantiationAwareBeanPostProcessorAdapter实现类构造器
    /**
     * getBean：获取或创建类：1获取RootBeanDefinition 2.实例化前 3创建空实例 4.设置属性 5执行Aware接口 6.执行InitializingBean 7.实例化后 8.返回对象
     */
    //InstantiationAwareBeanPostProcessor调用postProcessBeforeInstantiation方法
    //【构造器】实例化
    //【注入属性】注入属性name
    //【BeanNameAware接口】调用BeanNameAware.setBeanName()
    // BeanPostProcessor接口方法postProcessBeforeInitialization对属性进行更改！
    //【init-method】调用<bean>的init-method属性指定的初始化方法
    //【InitializingBean接口】调用InitializingBean.afterPropertiesSet()
    //BeanPostProcessor接口方法postProcessAfterInitialization对属性进行更改！
    //InstantiationAwareBeanPostProcessor调用postProcessAfterInitialization方法
    //获取完整对象

    public static void main(String[] args) throws IOException {
        System.out.println("现在开始初始化容器");
        ApplicationContext factory = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        System.out.println("容器初始化成功");
        //得到Preson，并使用
        Person person = factory.getBean("person",Person.class);
        System.out.println(person.getName());

        System.out.println("现在开始关闭容器！");

    }


}
