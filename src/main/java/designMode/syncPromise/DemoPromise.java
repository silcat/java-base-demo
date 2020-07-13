package designMode.syncPromise;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static lombok.Lombok.checkNotNull;

public class DemoPromise<T> extends CompletableFuture<T> implements IPromise<T> {
    /**
     * 状态及状态枚举
     */
    private volatile boolean uncancellable;
    private final AtomicInteger status = new AtomicInteger();
    private final int SUCCESS = 1;
    private final int FAILED = 2;
    private final int CANCELED = 3;

    /**
     * 回调监听器
     */
    private List<ComplateFutureListener<? extends IFuture<?>>> listeners;
    private boolean notifyingListeners;

    /**
     * 初始化链路状态
     */
    public static <V> IPromise<V> newFailedFuture(Throwable cause) {
        DemoPromise<V> future = new DemoPromise<V>();
        future.tryFailure(cause);
        return future;
    }

    public static  <V> IPromise<V> newSucceededFuture(V result) {
        DemoPromise<V> future = new DemoPromise<V>();
        future.trySuccess(result);
        return future;
    }

    @Override
    public synchronized boolean trySuccess(T result) {
        if (status.compareAndSet(0,SUCCESS)){
            complete(result);
            return true;
        }
        return false;
    }


    @Override
    public synchronized boolean tryFailure(Throwable cause) {
        if (status.compareAndSet(0,SUCCESS)){
            completeExceptionally(cause);
            return true;
        }
        return false;
    }
    @Override
    public synchronized boolean tryCancel(boolean mayInterruptIfRunning) {
        if (uncancellable) {
            return false;
        }
        if (status.compareAndSet(0, CANCELED)) {
            return super.cancel(mayInterruptIfRunning);
        }
        return false;
    }

    @Override
    public boolean setUncancellable() {
        if (!isDone()) {
            uncancellable = true;
        }
        return uncancellable;
    }

    @Override
    public IPromise<T> addListener(ComplateFutureListener<? extends IFuture<? super T>> listener) {
        checkNotNull(listener, "listener");

        synchronized (this) {
            if (listeners == null){
                listeners = Lists.newArrayList();
            }
            listeners.add(listener);
        }

        if (isDone()) {
            notifyListeners();
        }

        return this;
    }



    private void notifyListeners() {
        List<ComplateFutureListener<? extends IFuture<?>>>  listeners;
        synchronized (this) {
            if (notifyingListeners || this.listeners == null) {
                return;
            }
            notifyingListeners = true;
            listeners = this.listeners;
            this.listeners = null;
        }
        int size = listeners.size();
        for (;;) {
            for (int i = 0; i < size; i ++) {
                notifyListener0(this,listeners.get(0));
            }
            synchronized (this) {
                if (this.listeners == null) {
                    // Nothing can throw from within this method, so setting notifyingListeners back to false does not
                    // need to be in a finally block.
                    notifyingListeners = false;
                    return;
                }
                listeners = this.listeners;
                this.listeners = null;
            }
        }
    }

    private static void notifyListener0(IFuture future, ComplateFutureListener l) {
        try {
            l.operationComplete(future);
        } catch (Throwable t) {
        }
    }

    @Override
    public IPromise<T> removeListener(ComplateFutureListener<? extends IFuture<? super T>> listener) {
        synchronized (this) {
           this.listeners .remove(listener);
        }
        return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return status != null ;
    }

    @Override
    public T get()  {
        return getNow(null);
    }
    @Override
    public boolean isSuccess() {
        return isDone() && !isCompletedExceptionally();
    }
    @Override
    public T get(long timeout, TimeUnit unit)  {
        return null;
    }
}
