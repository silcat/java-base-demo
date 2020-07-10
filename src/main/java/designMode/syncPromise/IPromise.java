package designMode.syncPromise;

import java.util.concurrent.Future;

/**
 * 凭证：封装future，一个凭证状态只能修改一次
 * 凭证有3种状态：成功，失败与取消
 */
public interface IPromise<V> extends Future<V> {
    /**
     * 设置凭证状态
     */
    boolean trySuccess(V result);
    boolean tryFailure(Throwable cause);
    public boolean tryCancel(boolean mayInterruptIfRunning);
    boolean setUncancellable();

    /**
     * 回调监听
     */
    IPromise<V> addListener(ComplateFutureListener<? extends Future<? super V>> listener);
    IPromise<V> removeListener(ComplateFutureListener<? extends Future<? super V>> listener);


}
