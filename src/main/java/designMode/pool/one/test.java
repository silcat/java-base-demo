package designMode.pool.one;


public class test {

    public static void main(String[] args)  {

        PoolAllocator poolAllocator = new PoolAllocator(2);
        for (int i = 0;i<4;i++){
            new Thread(new Runnable() {@Override
            public void run() {
                PoolAllocator.Source source = poolAllocator.get();
                int i1 = source.get();
                System.out.println(Thread.currentThread().getName()+source.getName()+ "pre source value:"+ i1);
                source.add();
                System.out.println(Thread.currentThread().getName()+source.getName()+"after source value:"+ source.get());
            }},"thread-"+i).start();
        }


    }
}
