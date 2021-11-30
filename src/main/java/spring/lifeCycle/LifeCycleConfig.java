package spring.lifeCycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"spring"})
public class LifeCycleConfig {
    @Bean
    public Person person(){
        return new Person();
    }
}
