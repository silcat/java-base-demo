package designMode.eventStream;


/**
 * 添加事件到管道
 */
public interface IDemoPipeline extends EventHandlerService{
    DemoPipeline addFirst(String name, IEventHandler handler);
    DemoPipeline addLast(String name, IEventHandler handler);
}
