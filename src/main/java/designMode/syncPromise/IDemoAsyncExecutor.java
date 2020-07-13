package designMode.syncPromise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface IDemoAsyncExecutor {
   <V> IPromise<V> computeAsync(V v );
   <V> V get(IPromise<V> v );

}
