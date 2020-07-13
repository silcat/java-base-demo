package designMode.syncPromise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class DemoAsyncExecutor implements IDemoAsyncExecutor {

   @Override
   public <V> IPromise<V> computeAsync(V v) {
       IPromise<V> mainpromise = createPromise();
       IPromise<V> promise = createPromise();
       computeAsyncB(v,promise);
       promise.addListener(new ComplateFutureListener<IFuture<? super V>>() {
           @Override
           public void operationComplete(IFuture<? super V> future) throws Exception {
               log.info("A执行完成");
               if (future.isSuccess()){
                   mainpromise.trySuccess((V) future.get());
               }else {
                   mainpromise.tryFailure(new Exception());
               }

           }
       });
       return mainpromise;
   }

    private <V> IPromise<V> computeAsyncB(V v ,IPromise<V> mainpromise) {
        IPromise<V> promise = createPromise();
        async(v,promise);
        computeAsyncC(v,promise);
        promise.addListener(new ComplateFutureListener<IFuture<? super V>>() {
            @Override
            public void operationComplete(IFuture<? super V> future) throws Exception {
                log.info("B执行完成");
                if (future.isSuccess()){
                    mainpromise.trySuccess((V) future.get());
                }else {
                    mainpromise.tryFailure(new Exception());
                }

            }

        });
        return mainpromise;
    }

    private <V> IPromise<V> computeAsyncC(V v,IPromise<V> mainpromise) {
        IPromise<V> promise = DemoPromise.newSucceededFuture(v);
        async(v,promise);
        promise.addListener(new ComplateFutureListener<IFuture<? super V>>() {
            @Override
            public void operationComplete(IFuture<? super V> future) throws Exception {
                log.info("C执行完成");
                if (future.isSuccess()){
                    mainpromise.trySuccess((V) future.get());
                }else {
                    mainpromise.tryFailure(new Exception());
                }

            }

        });
        return DemoPromise.newSucceededFuture(v);
    }

    @Async
    public  <V> void async(V v, IPromise<V> mainPromise)  {
        Thread thread = Thread.currentThread();
        if (v == null){
            mainPromise.tryFailure(new Exception("null指针"));
            System.out.print("任务执行失败"+ v.toString());
        }
        try {
            Thread.sleep(10000L);
            log.info(thread.getName()+"任务执行成功");
        } catch (Exception e) {
            mainPromise.tryFailure(e);
            System.out.print("任务执行失败"+ v.toString());
        }

    }

    @Override
    public <V> V get(IPromise<V> future)  {
        if (!future.isDone()) {
            final CountDownLatch l = new CountDownLatch(1);
            future.addListener(new ComplateFutureListener<IFuture<? super V>>() {
                @Override
                public void operationComplete(IFuture<? super V> future) throws Exception {
                    l.countDown();
                }
            });

            boolean interrupted = false;
            while (!future.isDone()) {
                try {
                    l.await();
                } catch (InterruptedException e) {
                    interrupted = true;
                    break;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        if (future.isSuccess()) {
            try {
                return future.get();
            } catch (Exception e) {
            }
        }
        return null;
    }

    private <V> IPromise<V> createPromise() {
      return new DemoPromise<V>();
   }

}
