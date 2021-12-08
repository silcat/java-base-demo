package spring.lifeCycle;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import spring.aop.Aop;
import spring.aop.AutoCreaterAopProxyBeanPostProcessor;

@Configuration
public class LifeCycleConfig {

    @Bean
    public Aop aop(){
        return new Aop();
    }
    @Bean
    public BeanPostProcessor autoCreaterAopProxyBeanPostProcessor(){
        return new AutoCreaterAopProxyBeanPostProcessor();
    }
}
