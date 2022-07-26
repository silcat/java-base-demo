#线程
* https://mp.weixin.qq.com/s/yaJ2vJdVBQwXp873xo2fcA
* https://blog.csdn.net/qq_32273417/article/details/109148693
#synchronized如何保证线程安全
##原子性
* 被synchronized修饰的类或对象的所有操作都是原子的，因为在执行操作之前必须先获得类或对象的锁，直到执行完才能释放。
##可见性
* 一个线程如果要访问该类或对象必须先获得它的锁，而这个锁的状态对于其他任何线程都是可见的，并且在释放锁之前会将对变量的修改刷新到共享内存当中，保证资源变量的可见性。
##有序性
* 指令重排并不会影响单线程的顺序，它影响的是多线程并发执行的顺序性。synchronized保证了每个时刻都只有一个线程访问同步代码块，也就确定了线程执行同步代码块是分先后顺序的，保证了有序性。
#synchronized底层原理
* 数据同步需要依赖锁，那锁的同步又依赖谁？synchronized给出的答案是在软件层面依赖JVM的Monito对象
* synchronized 同步语句块的实现使用的是 monit锁升级.png...  bbnbbnbn=/orenter 和 monitorexit 指令，其中 monitorenter 指令指向同步代码块的开始位置， monitorexit 指令则指明同步代码块的结束位置。
* 在执行monitorenter时，会尝试获取对象的锁，如果锁的计数器为 0 则表示锁可以被获取，获取后将锁计数器设为 1 也就是加 1。
* 在执行 monitorexit 指令后，将锁计数器设为 0，表明锁被释放。如果获取对象锁失败，那当前线程就要阻塞等待，直到锁被另外一个线程释放为止。
* Monitor可以把它理解为 一个同步工具，也可以描述为 一种同步机制，它通常被 描述为一个对象，常说Synchronized的对象锁，MarkWord锁标识位为10，其中指针指向的是Monitor对象的起始地址。在Java虚拟机（HotSpot）中，Monitor是由ObjectMonitor实现的
- ![img.png](synchonize.png)
#synchronized的优化
* synchronized锁的级别不再是单一的重量级锁了，而是有无锁、偏向锁、轻量级锁和重量级锁四种状态
##锁升级
- !/.,mnnnnnnnnv)