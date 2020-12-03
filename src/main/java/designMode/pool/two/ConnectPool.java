package designMode.pool.two;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConnectPool<E> {
    private final Set<E> leased;
    private final LinkedList<E> available;
    private final ConnectFactory<E> connectFactory;
    private final int maxConnect;
    public ConnectPool() {
        this.leased = Sets.newHashSet();
        this.available =  new LinkedList();
        this.connectFactory = new ConnectFactory();
        this.maxConnect = 100;
    }
    public Future<E> lease(){
        return new Future<E>() {
            private volatile boolean cancelled;
            private volatile boolean done;
            private volatile E entry;

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }
            @Override
            public boolean isCancelled() {
                return this.cancelled;
            }
            @Override
            public boolean isDone() {
                return this.done;
            }
            @Override
            public E get() {
                return this.get(0L, TimeUnit.MILLISECONDS);
            }
            @Override
            public E get(long timeout, TimeUnit tunit)  {
                synchronized(this) {
                    //判断available有可用连接
                    if (Optional.ofNullable(getFreeConnect()).isPresent()){
                        return getFreeConnect();
                    }
                    //没有可用连接且连接占满等待
                    if (leased.size() > maxConnect){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //没有可用连接创建连接
                    E o = connectFactory.creatConnect();
                    leased.add(o);
                    return  o;

                }
            }
        };
    }
    private E getFreeConnect() {
        E first = available.getFirst();
        available.remove(first);
        leased.add(first);
        return first;
    }
    final public class ConnectFactory<E>{
        public E creatConnect(){

            return null;
        }
    }
}
