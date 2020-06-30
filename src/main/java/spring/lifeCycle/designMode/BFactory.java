package spring.lifeCycle.designMode;


public class BFactory extends AbstractFactory {


    @Override
    protected Product createInstant(String name) {
        return new BProduct(name);
    }

}
