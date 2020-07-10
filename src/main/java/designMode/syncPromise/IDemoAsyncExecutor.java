package designMode.syncPromise;

public interface IDemoAsyncExecutor {
   <V> IPromise<V> computeAsync(V v );
   <V> IPromise<V> computeAsyncB(V v );
   <V> IPromise<V> computeAsyncC(V v );
}
