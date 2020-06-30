package spring.lifeCycle.designMode;

import java.util.List;

public interface ProductAware extends RootAware {
    void registProcessor(List<Processor> processors);
}
