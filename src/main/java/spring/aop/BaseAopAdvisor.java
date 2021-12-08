package spring.aop;


import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class BaseAopAdvisor {
    private BasePointcut basePointcut;
    private BaseAdvice baseAdvice;
    public BaseAopAdvisor() {
        basePointcut = new BasePointcut();
        baseAdvice = new BaseAdvice();
    }




}