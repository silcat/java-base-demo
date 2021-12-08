package spring.aop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class AutoCreaterAopProxyBeanPostProcessor implements BeanPostProcessor {

    public AutoCreaterAopProxyBeanPostProcessor() {
        super();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        BaseAopAdvisor aopAdvisor = getAdvosor(bean, beanName);
        if (Optional.ofNullable(aopAdvisor).isPresent()){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback(aopAdvisor.getBaseAdvice());
            Object o = enhancer.create();
            return o;
        }
        return bean;
    }
    private BaseAopAdvisor getAdvosor(Object bean, String beanName) {
        BaseAopAdvisor aopAdvisor = new BaseAopAdvisor();
        BasePointcut basePointcut = aopAdvisor.getBasePointcut();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            if (basePointcut.matches(method,bean.getClass())){
                return aopAdvisor;
            }
        }
        return null;
    }





}