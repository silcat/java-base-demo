package designMode.eventStream;


public class EventHandlerContext implements IEventHandlerContext {
    private final IEventHandler handler;
    volatile EventHandlerContext next;
    volatile EventHandlerContext prev;
    private volatile int handlerState = 0;

    public EventHandlerContext(IEventHandler handler) {
        this.handler = handler;
    }

    @Override
    public EventHandlerContext fireEventDoSometing() {
        invokeEventDoSometing(findNextEventHandler());
        return this;
    }

    @Override
    public IEventHandler handler() {
        return this.handler;
    }

    static void invokeEventDoSometing(EventHandlerContext ctx){
        ctx.invokeEventDoSometing();
    }

    private void invokeEventDoSometing(){
        if (isExcute()){
            fireEventDoSometing();
        }else {
            handler().handDoSomething(this);
            this.handlerState = 1;
        }
    }
    public Boolean isExcute(){
        return this.handlerState != 0;
    }
    private EventHandlerContext findNextEventHandler(){
        EventHandlerContext ctx = this;
        do {
            ctx = ctx.prev;
        } while (ctx.next != null);
        return ctx;
    }
}
