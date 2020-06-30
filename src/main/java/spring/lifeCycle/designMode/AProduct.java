package spring.lifeCycle.designMode;

import lombok.Data;

import java.util.List;


public class AProduct implements Product, ProductAware {
    private String name;

    public AProduct(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String setName(String setname) {
        name = setname;
        return name;
    }

    @Override
    public void registProcessor(List<Processor> processors) {
        System.out.println("执行AAware");
        processors.add(new ActionProcessor());
    }
}
