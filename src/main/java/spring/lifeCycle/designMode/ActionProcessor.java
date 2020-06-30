package spring.lifeCycle.designMode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionProcessor implements Processor {

    @Override
    public void before(String name) {

    }

    @Override
    public void after(String name, Product product) {
        if (product instanceof AProduct){
            product.setName("Aproccess");
        }else {
            product.setName("Bproccess");
        }
        log.info("执行Proceesor："+ product.getName());
    }
}
