package spring.lifeCycle.designMode;

public interface Processor {
    void before(String name);
    void after(String name,Product product);
}
