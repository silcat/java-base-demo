package designMode.syncPromise;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;

public class DemoAsyncExecutor implements IDemoAsyncExecutor {

   @Override
   public <V> IPromise<V> computeAsync(V v) {
       IPromise<V> mainpromise = createPromise();
       IPromise<V> promise = createPromise();
       async(v,promise);
       promise.addListener(new ComplateFutureListener<Future<? super V>>() {
           @Override
           public void operationComplete(Future<? super V> future) throws Exception {
               mainpromise.trySuccess((V) future.get());
           }
       });
       return promise;
   }

    @Override
    public <V> IPromise<V> computeAsyncB(V v) {
        return computeAsync(v);
    }

    @Override
    public <V> IPromise<V> computeAsyncC(V v) {
        return computeAsync(v);
    }

    @Async
    public  <V> void async(V v, IPromise<V> mainPromise)  {
        if (v == null){
            mainPromise.tryFailure(new Exception("null指针"));
            System.out.print("任务执行失败"+ v.toString());
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            mainPromise.tryFailure(e);
            System.out.print("任务执行失败"+ v.toString());
        }
        System.out.print("任务执行成功"+ v.toString());
    }

    protected <V> IPromise<V> createPromise() {
      return new DemoPromise<V>();
   }
}
