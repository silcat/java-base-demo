package spring.lifeCycle.designMode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractHolder {

    private final Map<String, Product> productMap = new ConcurrentHashMap<>(256);

    protected Product getCachedProducBean(String beanName) {
        return this.productMap.get(beanName);
    }
    protected void putProducBean(String beanName ,Product product) {
         this.productMap.put(beanName,product);
    }

}
