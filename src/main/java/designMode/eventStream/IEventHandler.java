package designMode.eventStream;

public interface IEventHandler {
    void read(IEventHandlerContext ctx);
    void write(IEventHandlerContext ctx);
    void accept(IEventHandlerContext ctx);

}
