package designMode.eventStream;

public class DemoPipeline implements IDemoPipeline {
    final EventHandlerContext head;
    final EventHandlerContext tail;

    public DemoPipeline() {
        this.head = new EventHandlerContext(new DefaultHandler());
        this.tail = new EventHandlerContext(new DefaultHandler());
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public DemoPipeline addFirst(String name, IEventHandler handler) {
        EventHandlerContext newCtx = new EventHandlerContext(handler);
        EventHandlerContext nextCtx = head.next;
        newCtx.prev = head;
        newCtx.next = nextCtx;
        head.next = newCtx;
        nextCtx.prev = newCtx;
        return this;
    }

    @Override
    public DemoPipeline addLast(String name, IEventHandler handler) {
        EventHandlerContext newCtx = new EventHandlerContext(handler);
        EventHandlerContext prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
        return null;
    }


    @Override
    public void read() {
        head.fireEventRead();
    }

    @Override
    public void write() {
        head.fireEventWrite();
    }

    @Override
    public void accept() {
        head.fireEventAccept();
    }

    public class DefaultHandler implements IEventHandler{

        @Override
        public void read(IEventHandlerContext ctx) {
            ctx.fireEventRead();
        }

        @Override
        public void write(IEventHandlerContext ctx) {
            ctx.fireEventWrite();
        }

        @Override
        public void accept(IEventHandlerContext ctx) {
            ctx.fireEventAccept();
        }
    }
}
