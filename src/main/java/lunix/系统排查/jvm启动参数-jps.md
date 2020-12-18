#cat 命令
* 格式: jps option （全部文本内容）
  * option：
  ````
   -l:主类名称 若执行是jar包则输出jar路径
   -m:输出JVM启动时传递给main函数参数
   -v:输出JVM启动参数
  ````
##表达式
* jps
````
20769 Jps
90 jar                                                                 
````
* jps -v
  
````
19877 Jps -Denv.class.path=.:/opt/app/jdk1.8.0_131/lib/dt.jar:/opt/app/jdk1.8.0_131/lib/tools.jar -Dapplication.home=/opt/app/jdk1.8.0_131 -Xms8m
90 jar -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+StartAttachListener -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/app/logs/java.hprof
````

#JVM启动参数
##分类
````
java启动参数共分为三类
其一是标准参数（-），所有的JVM实现都必须实现这些参数的功能，而且向后兼容；
其二是非标准参数（-X），默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容；
其三是非Stable参数（-XX），此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用；
````
##标准参数
````
-verbose:class 输出jvm载入类的相关信息，当jvm报告说找不到类或者类冲突时可此进行诊断。
-verbose:gc 输出每次GC的相关情况。
-verbose:jni 输出native方法调用的相关情况，一般用于诊断jni调用错误信息。
````
##非标准参数
````
-Xmx3550m：设置JVM最大堆内存为3550M。
-Xms3550m：设置JVM初始堆内存为3550M。此值可以设置与-Xmx相同，以避免每次垃圾回收完成后JVM重新分配内存。
-Xss128k：设置每个线程的栈大小。JDK5.0以后每个线程栈大小为1M，之前每个线程栈大小为256K。应当根据应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。需要注意的是：当这个值被设置的较大（例如>2MB）时将会在很大程度上降低系统的性能。
-Xmn2g：设置年轻代大小为2G。在整个堆内存大小确定的情况下，增大年轻代将会减小年老代，反之亦然。此值关系到JVM垃圾回收，对系统性能影响较大，官方推荐配置为整个堆大小的3/8。
-Xloggc:file
 与-verbose:gc功能类似，只是将每次GC事件的相关情况记录到一个文件中，文件的位置最好在本地，以避免网络的潜在问题。
 若与verbose命令同时出现在命令行中，则以-Xloggc为准。
-Xprof
 跟踪正运行的程序，并将跟踪数据在标准输出输出；适合于开发环境调试。
````
##非标准参数
 * 行为参数
````
    ==============================================G1=================================================================
    -XX:+UseG1GC 使用G1垃圾回收器
    -XX:MaxGCPauseMillis=200 最大暂停时间，这是一个软目标，Java虚拟机将尽最大努力实现它
    -XX:InitiatingHeapOccupancyPercent=40	触发并发垃圾收集周期的整个堆空间的占用比例。它被垃圾收集使用，用来触发并发垃圾收集周期，基于整个堆的占用情况，不只是一个代上(比如：G1)。0值 表示’do constant GC cycles’。默认是45
    -XX:G1ReservePercent=n	为了降低晋升失败机率设置一个假的堆的储备空间的上限大小，默认值是10
    -XX:NewRatio=n	年轻代与年老代的大小比例，默认值是2
    -XX:SurvivorRatio=n	eden与survivor空间的大小比例，默认值8
    -XX:MaxTenuringThreshold=n	最大晋升阈值，默认值15
    -XX:ParallerGCThreads=n	设置垃圾收集器并行阶段的线程数量。默认值根据Java虚拟机运行的平台有所变化
    -XX:ConcGCThreads=n	并发垃圾收集器使用的线程数量，默认值根据Java虚拟机运行的平台有所变化
    -XX:G1HeapRegionSize=n	使用G1收集器，Java堆被细分成一致大小的区域。这设置个体的细分的大小。这个参数的默认值由工学意义上的基于堆的大小决定
    ==============================================并发=================================================================
    -XX:+UseConcMarkSweepGC：即CMS收集，设置年老代为并发收集。CMS收集是JDK1.4后期版本开始引入的新GC算法。它的主要适合场景是对响应时间的重要性需求大于对吞吐量的需求，能够承受垃圾回收线程和应用线程共享CPU资源，并且应用中存在比较多的长生命周期对象。CMS收集的目标是尽量减少应用的暂停时间，减少Full GC发生的几率，利用和应用程序线程并发的垃圾回收线程来标记清除年老代内存。
    -XX:+UseParNewGC：设置年轻代为并发收集。可与CMS收集同时使用。JDK5.0以上，JVM会根据系统配置自行设置，所以无需再设置此参数。
    -XX:CMSFullGCsBeforeCompaction=0：由于并发收集器不对内存空间进行压缩和整理，所以运行一段时间并行收集以后会产生内存碎片，内存使用效率降低。此参数设置运行0次Full GC后对内存空间进行压缩和整理，即每次Full GC后立刻开始压缩和整理内存。
    -XX:+UseCMSCompactAtFullCollection：打开内存空间的压缩和整理，在Full GC后执行。可能会影响性能，但可以消除内存碎片。
    -XX:+CMSIncrementalMode：设置为增量收集模式。一般适用于单CPU情况。
    -XX:CMSInitiatingOccupancyFraction=70：表示年老代内存空间使用到70%时就开始执行CMS收集，以确保年老代有足够的空间接纳来自年轻代的对象，避免Full GC的发生。
    ==============================================并行=================================================================
    -XX:-UseParallelGC 年轻代使用并行收集 （-XX:-UseParallelOldGC 对Full GC启用并行，当-XX:-UseParallelGC启用时该项自动启用)
    -XX:ParallelGCThreads=20：配置并行收集器的线程数，即：同时有多少个线程一起进行垃圾回收。此值建议配置与CPU数目相等。
    -XX:+UseParallelOldGC：配置年老代垃圾收集方式为并行收集。JDK6.0开始支持对年老代并行收集。
    -XX:MaxGCPauseMillis=100：设置每次年轻代垃圾回收的最长时间（单位毫秒）。如果无法满足此时间，JVM会自动调整年轻代大小，以满足此时间。
    -XX:+UseAdaptiveSizePolicy：设置此选项后，并行收集器会自动调整年轻代Eden区大小和Survivor区大小的比例，以达成
    ==============================================串行=================================================================
    -XX:-UseSerialGC 启用串行GC
    =================================================================================================================
    -XX:+ScavengeBeforeFullGC：年轻代GC优于Full GC执行。
    -XX:-DisableExplicitGC：不响应 System.gc() 代码。
    -XX:+UseThreadPriorities：启用本地线程优先级API。即使 java.lang.Thread.setPriority() 生效，不启用则无效。
    -XX:SoftRefLRUPolicyMSPerMB=0：软引用对象在最后一次被访问后能存活0毫秒（JVM默认为1000毫秒）。
    -XX:TargetSurvivorRatio=90：允许90%的Survivor区被占用（JVM默认为50%）。提高对于Survivor区的使用率。
````
 * 调优参数
````
    -XX:NewSize=1024m：设置年轻代初始值为1024M。
    -XX:MaxNewSize=1024m：设置年轻代最大值为1024M。
    -XX:PermSize=256m：设置持久代初始值为256M。
    -XX:MaxPermSize=256m：设置持久代最大值为256M。
    -XX:ThreadStackSize=512 设置线程栈大小，若为0则使用系统默认值
    -XX:NewRatio=4：设置年轻代（包括1个Eden和2个Survivor区）与年老代的比值。表示年轻代比年老代为1:4。
    -XX:SurvivorRatio=4：设置年轻代中Eden区与Survivor区的比值。表示2个Survivor区（JVM堆内存年轻代中默认有2个大小相等的Survivor区）与1个Eden区的比值为2:4，即1个Survivor区占整个年轻代大小的1/6。
    -XX:MaxTenuringThreshold=7：表示一个对象如果在Survivor区（救助空间）移动了7次还没有被垃圾回收就进入年老代。如果设置为0的话，则年轻代对象不经过Survivor区，直接进入年老代，对于需要大量常驻内存的应用，这样做可以提高效率。如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多次复制，这样可以增加对象在年轻代存活时间，增加对象在年轻代被垃圾回收的概率，减少Full GC的频率，这样做可以在某种程度上提高服务稳定性。
    -XX:LargePageSizeInBytes=4m 设置用于Java堆的大页面尺寸
    -XX:MaxHeapFreeRatio=70 GC后java堆中空闲量占的最大比例
    -XX:MinHeapFreeRatio=40 GC后java堆中空闲量占的最小比例
    -XX:ReservedCodeCacheSize=32m 保留代码占用的内存容量
    -XX:+UseLargePages 使用大页面内存
````
 * 调试参数
````
    -XX:-HeapDumpOnOutOfMemoryError：当首次遭遇内存溢出时Dump出此时的堆内存。
    -XX:ErrorFile=./hs_err_pid.log：保存错误日志或数据到指定文件中
    -XX:HeapDumpPath=./java_pid.hprof：指定Dump堆内存时的路径
    -XX:-CITime：打印消耗在JIT编译的时间。
    -XX:OnError=";"：出现致命ERROR后运行自定义命令。
    -XX:OnOutOfMemoryError=";"：当首次遭遇内存溢出时执行自定义命令
    -XX:-PrintClassHistogram：按下 Ctrl+Break 后打印堆内存中类实例的柱状信息，同JDK的 jmap -histo 命令。
    -XX:-PrintConcurrentLocks：按下 Ctrl+Break 后打印线程栈中并发锁的相关信息，同JDK的 jstack -l 命令。
    -XX:-PrintCompilation：当一个方法被编译时打印相关信息。
    -XX:-PrintGC：每次GC时打印相关信息。
    -XX:-PrintGCDetails：每次GC时打印详细信息。
    -XX:-PrintGCTimeStamps：打印每次GC的时间戳。
    -XX:-TraceClassLoading：跟踪类的加载信息。
    -XX:-TraceClassLoadingPreorder：跟踪被引用到的所有类的加载信息。
    -XX:-TraceClassResolution：跟踪常量池。
    -XX:-TraceClassUnloading：跟踪类的卸载信息。

```` 
## G1垃圾回收器
* 1.参考文章
  - https://www.jianshu.com/p/aef0f4765098
  - https://blog.csdn.net/shlgyzl/article/details/95041113
  - https://www.cnblogs.com/aspirant/p/8663872.html
* 2.综述
  - G1其实是Garbage First的意思，是优先处理那些垃圾多的内存块的意思。
  - G1内存也分成了old（老年代），eden（伊甸）和survive（只有一类幸存者区）三类和特殊的Humongous（大对象）区，但在G1三大类内存划分不是同CMS一样把内存分为连续的3块内存，而是G1把内存分为2048块, 每个小块会被标记为E/S/O中的一个。
  - G1的设计原则就是简单可行的性能调优，因为不存在严格意义上的年轻代和老年代了，只需要划分总的堆内存（-Xmx）不需要设置-Xmn。
  - G1的另一个显著特点他能够让用户设置应用的暂停时间。而G1每次并不会回收整代内存而是小块内存回收。故到底回收多少内存就看用户配置的暂停时间，配置的时间短就少回收点，配置的时间长就多回收点，伸缩自如。
  - G1比较适合内存稍大一点的应用(一般来说至少4G以上)，小内存的应用还是用传统的垃圾回收器比如CMS比较合适。
  - G1最显著于CMS的，在于它对空间做了整理，这样减少了空间的碎片化
  - G1采用TLABs的技术，就会带来碎片。
    - TLABs即每个线程在eden区分配已开独立Buffer，每个线程隔离开，不需要和其他线程交互，自然可以使用bump-the-point，当线程各自的Buffer满了之后需要申请新的Buffer。
    - 线程Buffer里面还有剩余的空间，但是却因为分配的对象过大以至于这些空闲空间无法容纳，此时线程只能去申请新的Buffer，而原来的Buffer中的空闲空间就被浪费了。
      Buffer的大小和线程数量都会影响这些碎片的多寡。
* 3.G1并发垃圾回收过程  
````
- 在垃圾回收的最开始有一个短暂的时间段(Inital Mark)会停止应用(stop-the-world)
- 然后应用继续运行，同时G1开始Concurrent Mark
- 再次停止应用，来一个Final Mark (stop-the-world)
- 最后根据Garbage First的原则，选择一些内存块进行回收。(stop-the-world)
````