package spring.lifeCycle.designMode;


public class  AFactory extends AbstractFactory {


    @Override
    protected Product createInstant(String name) {
        return new AProduct(name);
    }

}
