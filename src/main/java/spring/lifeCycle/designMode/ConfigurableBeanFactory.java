package spring.lifeCycle.designMode;



public interface ConfigurableBeanFactory extends Factory {
    void addProcessor(Processor peopleProcessor);
}
