package designMode.pool.one;



public class PoolThreadCache  {
    private PoolAllocator.Source source;

    public PoolThreadCache(PoolAllocator.Source source) {
        this.source = source;
    }
    public PoolAllocator.Source  getSource(){
        return source;
    }
}
