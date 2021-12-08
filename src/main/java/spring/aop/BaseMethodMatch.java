package spring.aop;


import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Optional;


@Component
public class BaseMethodMatch {
    public BaseMethodMatch() {
    }
    public boolean matches(Method method, Class<?> targetClass) {
        TestAop annotation = method.getAnnotation(TestAop.class);
        if (Optional.ofNullable(annotation).isPresent()){
            return true;
        }
        return false;
    }

}