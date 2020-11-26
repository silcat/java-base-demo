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
    public EventHandlerContext fireEventRead() {
        invokeEventRead(findNextEventHandler());
        return this;
    }

    @Override
    public EventHandlerContext fireEventWrite() {
        invokeEventWrite(findNextEventHandler());
        return this;
    }

    @Override
    public IEventHandler handler() {
        return this.handler;
    }

    private void invokeEventRead(){
        if (isExcute()){
            fireEventRead();
        }else {
            handler().read(this);
            this.handlerState = 1;
        }
    }
    static void invokeEventRead(EventHandlerContext ctx){
        ctx.invokeEventRead();
    }

    private void invokeEventWrite(){
        if (isExcute()){
            fireEventWrite();
        }else {
            handler().write(this);
            this.handlerState = 1;
        }
    }
    static void invokeEventWrite(EventHandlerContext  ctx){
        ctx.invokeEventWrite();
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
