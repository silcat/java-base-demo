package designMode.syncPromise;

import org.springframework.stereotype.Component;

import java.util.EventListener;
import java.util.concurrent.Future;

@Component
public interface ComplateFutureListener<T extends IFuture<?>>  extends EventListener {
    /**
     *
     */
    void operationComplete(T future) throws Exception;

}
