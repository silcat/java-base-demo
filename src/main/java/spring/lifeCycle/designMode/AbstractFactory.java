package spring.lifeCycle.designMode;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractFactory extends AbstractHolder implements ConfigurableBeanFactory {

    private final List<Processor> processors = new CopyOnWriteArrayList<>();

    @Override
    public void addProcessor(Processor processor) {
        processors.add(processor);
    }

    @Override
    public Product getProduct(String name) {
        if (Optional.ofNullable(this.getCachedProducBean(name)).isPresent()){
            return this.getCachedProducBean(name);
        }else {
            return createProduct(name);
        }
    }


    protected  Product createProduct(String name){
        applyProcessorsBefore(name);
        Product instant = createInstant(name);
        invokeAwareMethods(name,(Object) instant);
        applyProcessorsAfter(name,instant);
        this.putProducBean(name,instant);
        return instant;
    }

    private void invokeAwareMethods(final String beanName, final Object bean) {
        if (bean instanceof RootAware) {
            if (bean instanceof ProductAware) {
                ((ProductAware) bean).registProcessor(processors);
            }
        }
    }

    public void applyProcessorsBefore(String beanName)  {
        for (Processor processor : getProcessors()) {
            processor.before(beanName);
        }
    }
    public void applyProcessorsAfter(String beanName,Product product)  {
        for (Processor processor : getProcessors()) {
            processor.after(beanName,product);
        }
    }
    protected List<Processor> getProcessors() {
        return this.processors;
    }
    protected abstract Product createInstant(String name);
}
