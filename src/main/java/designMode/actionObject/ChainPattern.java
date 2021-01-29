package designMode.actionObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 责任链模式（Chain of Responsibility）是一种处理请求的模式，它让多个处理器都有机会处理该请求，直到其中某个处理成功为止。
 */
public class ChainPattern {
    public static void main(String[] args)  {
        Request request = new Request("test", 40);
        HandleHandlerChain handlerChain = new HandleHandlerChain();
        handlerChain.addFirstHandler(new HandleHandler(10));
        handlerChain.addFirstHandler(new HandleHandler(30));
        handlerChain.addFirstHandler(new HandleHandler(50));
        boolean process = handlerChain.process(request);

    }
    @AllArgsConstructor
    @Data
    public static  class Request {
        private String name;
        private int amount;
    }
    //处理器
    public interface Handler {
        // 返回Boolean.TRUE = 成功
        // 返回Boolean.FALSE = 拒绝

        Boolean process(Request request);
    }
    public static  class AutoHandler implements  Handler{

        @Override
        public Boolean process(Request request) {
            return null;
        }
    }
    public static  class HandleHandler implements  Handler{
        private int limit;
        public HandleHandler(int limit) {
            this.limit = limit;
        }

        @Override
        public Boolean process(Request request) {
            if (request.amount> limit){
                return false;
            }
            return true;
        }
    }
    //处理器执行链
    public static class AutoHandlerChain {
        // 持有所有Handler:
        private List<Handler> handlers = new ArrayList<>();

        public void addHandler(Handler handler) {
            this.handlers.add(handler);
        }

        public boolean process(Request request) {
            // 依次调用每个Handler:
            for (Handler handler : handlers) {
                Boolean r = handler.process(request);
                if (r != null) {
                    // 如果返回TRUE或FALSE，处理结束:
                    System.out.println(request + " " + (r ? "Approved by " : "Denied by ") + handler.getClass().getSimpleName());
                    return r;
                }
            }
            throw new RuntimeException("Could not handle request: " + request);
        }
    }
    public static class HandleHandlerChain {
        private HandlerContext head ;

        public HandleHandlerChain() {
            this.head = new HandlerContext(new HandleHandler(10));
        }

        public void addFirstHandler(Handler handler) {
            HandlerContext next = head.next;
            head.next = new HandlerContext(handler);
            head.next.next =  next;
        }

        public boolean process(Request request) {
           return   head.process(request);
        }
        public class HandlerContext implements  Handler  {
            private final Handler handler;
            volatile HandlerContext next;
            private volatile int handlerState = 0;

            public HandlerContext(Handler handler) {
                this.handler = handler;
            }

            @Override
            public Boolean process(Request request) {
                Boolean process = handler.process(request);
                if (process){
                    if (next != null){
                       process = next.process(request);
                    }
                }
                return process;
            }
        }
    }

}