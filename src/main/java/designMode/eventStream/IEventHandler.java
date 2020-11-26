package designMode.eventStream;

public interface IEventHandler {
    void begin(IEventHandlerContext ctx);
    void read(IEventHandlerContext ctx);
    void write(IEventHandlerContext ctx);
    void end(IEventHandlerContext ctx);
}
