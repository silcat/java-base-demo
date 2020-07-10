package designMode.syncPromise;

import java.util.EventListener;
import java.util.concurrent.Future;

public interface ComplateFutureListener<F extends Future<?>>  extends EventListener {
    //完成回调监听
    void operationComplete(F future) throws Exception;
}
