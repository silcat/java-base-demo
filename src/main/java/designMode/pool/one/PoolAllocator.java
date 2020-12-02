package designMode.pool.one;

public class PoolAllocator {
    private final Source[] source ;
    private final int size ;
    private volatile int index;
    private final PoolThreadLocalCache threadCache;

    public PoolAllocator( int size) {
        index = 1;
        this.size = size;
        threadCache = new PoolThreadLocalCache();
        this.source = new Source[size];
        for (int i= 0;i<size ;i++){
            source[i] = new Source("source-"+i);
        }
    }

    public Source get(){
        PoolThreadCache cache = threadCache.get();
        return cache.getSource();
    }

    private synchronized int getNextSource(){
        if (index + 1 >= size){
            index = 0;
            System.out.println(Thread.currentThread().getName()+"获取资源缓存"+index);
            return index;
        }else {
            index = index + 1;
            System.out.println(Thread.currentThread().getName()+"获取资源缓存"+index);
            return index;
        }

    }

    final public class PoolThreadLocalCache extends ThreadLocal<PoolThreadCache> {
        @Override
        protected PoolThreadCache initialValue() {
            System.out.println(Thread.currentThread().getName()+"：初始化资源缓存");
            return new PoolThreadCache(source[getNextSource()]);
        }
    }
    final public class Source{
        private volatile int index = 0;
        final private String name;
        public Source(String name) {
            this.name = name;
        }
        public void add(){
            index = index +1;
        }
        public int get(){
            return  index;
        };
        public String getName(){
            return  name;
        };
    }

}
