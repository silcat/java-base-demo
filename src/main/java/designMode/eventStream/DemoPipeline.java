package designMode.eventStream;

public class DemoPipeline implements IDemoPipeline {
    final EventHandlerContext head;
    final EventHandlerContext tail;

    public DemoPipeline() {
        this.head = new EventHandlerContext(new IEventHandler(){
            @Override
            public void handDoSomething(IEventHandlerContext ctx) {

            }
        });
        this.tail =  new EventHandlerContext(new IEventHandler(){
            @Override
            public void handDoSomething(IEventHandlerContext ctx) {

            }
        });
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
        return null;
    }

    @Override
    public void doSomeThing() {
        head.fireEventDoSometing();
    }
}
