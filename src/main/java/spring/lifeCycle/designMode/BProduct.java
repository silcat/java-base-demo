package spring.lifeCycle.designMode;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class BProduct implements Product, ProductAware {
    private String name;

    public BProduct(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public String setName(String setname){
        this.name = setname;
        return setname;
    }
    @Override
    public void registProcessor(List<Processor> processors) {
        log.info("执行BAware");
    }
}
