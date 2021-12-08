package spring.aop;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
public class BaseAdvice implements MethodInterceptor {
    private BaseMethodMatch methodMatch;
    public BaseAdvice() {
        methodMatch = new BaseMethodMatch();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        if (methodMatch.matches(method,o.getClass())){
            before();
        }
       return methodProxy.invokeSuper(o,objects);

    }
    private void before(){
        System.out.println("BaseAdvice :前置拦截" );
    }
}