package designMode.syncPromise;

import java.util.concurrent.CompletableFuture;
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
    private ComplateFutureListener<? extends Future<?>>[] listeners;

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
    public IPromise<T> addListener(ComplateFutureListener<? extends Future<? super T>> listener) {
        checkNotNull(listener, "listener");

        synchronized (this) {
            addListener0(listener);
        }

        if (isDone()) {
            notifyListeners();
        }

        return this;
    }

    @Override
    public IPromise<T> removeListener(ComplateFutureListener<? extends Future<? super T>> listener) {
        return null;
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
        return false;
    }

    @Override
    public T get()  {
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit)  {
        return null;
    }
}
