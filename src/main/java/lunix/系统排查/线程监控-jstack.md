#cat 命令
* 格式: jstack option （全部文本内容）
  * option：
  ````
    -F 当进程挂起，执行jstack <pid> 命令没有任何输出后，将强制转储堆内的线程信息
    -m 在混合模式下，打印 java 和 native c/c++ 框架的所有栈信息
    -l 长列表。打印关于锁的附加信息，例如属于 java.util.concurrent 的 ownable synchronizers 列表
    -h | -help 打印帮助信息
  ````
##表达式

* jps
  
````
37280 Jps
91 jar
````
* top -Hp 91
````
PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND 
91 app       20   0   23.1g   1.6g  17488 S  0.0  1.3   0:00.00 java 
282 app       20   0   23.1g   1.6g  17488 S  0.7  1.3   6:50.06 java                                                                             
                                                                        
````
* printf "%x\n" 282
````
11a
````
* jstack -l 91 |grep 11a -A30
````
"org.springframework.kafka.KafkaListenerEndpointContainer#3-2-C-1" #120 prio=5 os_prio=0 tid=0x00007fa66d82d000 nid=0x11a runnable [0x00007fa3d61ea000]
   java.lang.Thread.State: RUNNABLE
        at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
        at sun.nio.ch.EPollArrayWrapper.poll(EPollArrayWrapper.java:269)
        at sun.nio.ch.EPollSelectorImpl.doSelect(EPollSelectorImpl.java:93)
        at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
        - locked <0x00000000c6b00f68> (a sun.nio.ch.Util$3)
        - locked <0x00000000c6b00f58> (a java.util.Collections$UnmodifiableSet)
        - locked <0x00000000c6b00f78> (a sun.nio.ch.EPollSelectorImpl)
        at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
        at org.apache.kafka.common.network.Selector.select(Selector.java:691)
        at org.apache.kafka.common.network.Selector.poll(Selector.java:411)
        at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:510)
        at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:271)
        at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:242)
        at org.apache.kafka.clients.consumer.KafkaConsumer.pollForFetches(KafkaConsumer.java:1247)
        at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1187)
        at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1115)
        at brave.kafka.clients.TracingConsumer.poll(TracingConsumer.java:78)
        at brave.kafka.clients.TracingConsumer.poll(TracingConsumer.java:72)
        at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:748)
        at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:704)
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.lang.Thread.run(Thread.java:748)

   Locked ownable synchronizers:
        - <0x00000000c6a81d90> (a java.util.concurrent.locks.ReentrantLock$FairSync)

"ThreadPoolTaskScheduler-1" #119 prio=5 os_prio=0 tid=0x00007fa66d82b800 nid=0x119 waiting on condition [0x00007fa3d62eb000]
   java.lang.Thread.State: TIMED_WAITING (parking)
   ===================================================线程状态Thread.State==================================================================   
 第一行里，"ThreadPoolTaskScheduler-1"是 Thread Name 。tid指Java Thread id。nid指native线程的id。prio是线程优先级。[0x00007fa3d61ea000]是线程栈起始地址。  
===================================================线程状态Thread.State==================================================================   
-    死锁，Deadlock（重点关注） 

-    等待获取监视器，Waiting on monitor entry（重点关注）
-    等待资源，Waiting on condition（重点关注） 
     等待资源，或等待某个条件的发生。具体原因需结合 stacktrace来分析。
     1.如果堆栈信息明确是应用代码，则证明该线程正在等待资源。一般是大量读取某资源，且该资源采用了资源锁的情况下，线程进入等待状态，等待资源的读取。
     又或者，正在等待其他线程的执行等。
     2.如果发现有大量的线程都在处在 Wait on condition，从线程 stack看，正等待网络读写，这可能是一个网络瓶颈的征兆。因为网络阻塞导致线程无法执行。
     一种情况是网络非常忙，几乎消耗了所有的带宽，仍然有大量数据等待网络读写；
     3.另一种情况也可能是网络空闲，但由于路由等问题，导致包无法正常的到达。
     4.另外一种出现 Wait on condition的常见情况是该线程在 sleep，等待 sleep的时间到了时候，将被唤醒。
    
-    阻塞，Blocked（重点关注）  
     1.等待阻塞：运行的线程执行wait()方法，JVM会把该线程放入等待池中。(wait会释放持有的锁)
     2.同步阻塞：运行的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入锁池中。
     3.其他阻塞：运行的线程执行sleep()或join()方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入就绪状态。（注意,sleep是不会释放持有的锁）
-    停止，Parked
-    暂停，Suspended
-    对象等待中，Object.wait() 或 TIMED_WAITING
-    执行中，Runnable   
===================================================线程状态================================================================== 
````
