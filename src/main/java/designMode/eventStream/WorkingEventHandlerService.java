package designMode.eventStream;

public class WorkingEventHandlerService implements EventHandlerService {

    private final IDemoPipeline pipeline;

    public WorkingEventHandlerService() {
        this.pipeline = new DemoPipeline();
    }

    @Override
    public void doSomeThing() {
        pipeline.doSomeThing();
    }
}
