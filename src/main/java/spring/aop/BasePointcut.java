package spring.aop;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
public class BasePointcut {
    private BaseMethodMatch methodMatch;
    public BasePointcut() {
        methodMatch = new BaseMethodMatch();
    }
    public boolean matches(Method method, Class<?> targetClass) {
        return methodMatch.matches(method,targetClass);
    }
}