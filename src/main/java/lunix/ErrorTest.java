package lunix;


import java.util.concurrent.TimeUnit;

public class ErrorTest {
    public static void main(String[] args)  {
        testDeadBlock();
    }

    public static void testBlock(){
        MyTask task = new MyTask(true);
        Thread t1 = new Thread(task);
        t1.setName("t1");
        Thread t2 = new Thread(task);
        t2.setName("t2");
        t1.start();
        t2.start();
    }
    public static void testDeadBlock(){
        MyTask task1 = new MyTask(true);
        MyTask task2 = new MyTask(false);
        Thread t1 = new Thread(task1);
        t1.setName("t1");
        Thread t2 = new Thread(task2);
        t2.setName("t2");
        t1.start();
        t2.start();
    }
    static class MyTask implements Runnable {

        private boolean flag;

        public MyTask(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            if(flag) {
                synchronized (Mutex.mutex1) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    synchronized (Mutex.mutex2) {
                        System.out.println("ok");
                    }
                }
            } else {
                synchronized (Mutex.mutex2) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    synchronized (Mutex.mutex1) {
                        System.out.println("ok");
                    }
                }
            }
        }

    }

    static class Mutex {
        public static Integer mutex1 = 1;
        public static Integer mutex2 = 2;
    }

    /**
     * testDeadBlock() jstack解析
     */
    //Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.181-b13 mixed mode):
    //"t2" #15 prio=5 os_prio=0 tid=0x000000001ff81000 nid=0x1e500 waiting for monitor entry [0x00000000208fe000]
    //   java.lang.Thread.State: BLOCKED (on object monitor)
    //        at lunix.ErrorTest$MyTask.run(ErrorTest.java:61)
    //        - waiting to lock <0x000000076b400388> (a java.lang.Integer)
    //        - locked <0x000000076b400398> (a java.lang.Integer)
    //        at java.lang.Thread.run(Thread.java:748)
    //
    //"t1" #14 prio=5 os_prio=0 tid=0x000000001f29d000 nid=0x1e504 waiting for monitor entry [0x00000000207ff000]
    //   java.lang.Thread.State: BLOCKED (on object monitor)
    //        at lunix.ErrorTest$MyTask.run(ErrorTest.java:49)
    //        - waiting to lock <0x000000076b400398> (a java.lang.Integer)
    //        - locked <0x000000076b400388> (a java.lang.Integer)
    //        at java.lang.Thread.run(Thread.java:748)
    //

    //
    //
    //Found one Java-level deadlock:
    //=============================
    //"t2":
    //  waiting to lock monitor 0x000000001cfd3228 (object 0x000000076b400388, a java.lang.Integer),
    //  which is held by "t1"
    //"t1":
    //  waiting to lock monitor 0x000000001cfd0d08 (object 0x000000076b400398, a java.lang.Integer),
    //  which is held by "t2"
    //
    //Java stack information for the threads listed above:
    //===================================================
    //"t2":
    //        at lunix.ErrorTest$MyTask.run(ErrorTest.java:61)
    //        - waiting to lock <0x000000076b400388> (a java.lang.Integer)
    //        - locked <0x000000076b400398> (a java.lang.Integer)
    //        at java.lang.Thread.run(Thread.java:748)
    //"t1":
    //        at lunix.ErrorTest$MyTask.run(ErrorTest.java:49)
    //        - waiting to lock <0x000000076b400398> (a java.lang.Integer)
    //        - locked <0x000000076b400388> (a java.lang.Integer)
    //        at java.lang.Thread.run(Thread.java:748)
    //
    //Found 1 deadlock.

}
