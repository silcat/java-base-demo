#查看当前jvm参数
* 格式: jps option （全部文本内容）
  * option：
  ````
   -l:主类名称 若执行是jar包则输出jar路径
   -m:输出JVM启动时传递给main函数参数
   -v:输出JVM启动参数
  ````
##表达式
* jps -v
  
````
19877 Jps -Denv.class.path=.:/opt/app/jdk1.8.0_131/lib/dt.jar:/opt/app/jdk1.8.0_131/lib/tools.jar -Dapplication.home=/opt/app/jdk1.8.0_131 -Xms8m
90 jar -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+StartAttachListener -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/app/logs/java.hprof
````
* jinfo -flags pid 查看当前JVM运行的参数
````
Attaching to process ID 90, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.131-b11
Non-default VM flags: -XX:CICompilerCount=15 -XX:ConcGCThreads=7 -XX:+DisableExplicitGC -XX:G1HeapRegionSize=1048576 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=null -XX:InitialHeapSize=536870912 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=536870912 -XX:MaxNewSize=321912832 -XX:MinHeapDeltaBytes=1048576 -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+StartAttachListener -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps -XX:+UseG1GC 
Command line:  -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+StartAttachListener -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/app/logs/java.hprof
````
*  java -XX:+PrintCommandLineFlags -version 查看JVM默认的垃圾回收器
````
-XX:InitialHeapSize=16777216 -XX:MaxHeapSize=268435456 -XX:+PrintCommandLineFlags -XX:-UseLargePagesIndividualAllocation
java version "1.8.0_202"
Java(TM) SE Runtime Environment (build 1.8.0_202-b08)
Java HotSpot(TM) Client VM (build 25.202-b08, mixed mode, sharing)
````
* jmap -heap pid 查看当前堆状态
````
Garbage-First (G1) GC with 10 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 134217728 (128.0MB)
   NewSize                  = 1048576 (1.0MB)
   MaxNewSize               = 79691776 (76.0MB)
   OldSize                  = 4194304 (4.0MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 12582912 (12.0MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 4294901760 (4095.9375MB)
   G1HeapRegionSize         = 1048576 (1.0MB)

Heap Usage:
G1 Heap:
   regions  = 128
   capacity = 134217728 (128.0MB)
   used     = 64071776 (61.103607177734375MB)
   free     = 70145952 (66.89639282226562MB)
   47.73719310760498% used
G1 Young Generation:
Eden Space:
   regions  = 12
   capacity = 65011712 (62.0MB)
   used     = 12582912 (12.0MB)
   free     = 52428800 (50.0MB)
   19.35483870967742% used
Survivor Space:
   regions  = 7
   capacity = 7340032 (7.0MB)
   used     = 7340032 (7.0MB)
   free     = 0 (0.0MB)
   100.0% used
G1 Old Generation:
   regions  = 43
   capacity = 61865984 (59.0MB)
   used     = 44148832 (42.103607177734375MB)
   free     = 17717152 (16.896392822265625MB)
   71.36204606395657% used

25544 interned Strings occupying 2803336 bytes.
````
* top  系统状态监控
````
 PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+  COMMAND                                                                                           
 3460 java      20   0   33508   2180   1536 S  47.2  0.0   5765:44   ntpd   
````
* jstat  进程监控
````
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
 0.0   7168.0  0.0   7168.0 63488.0  20480.0   60416.0    43114.1   29312.0 28047.5  0.0    0.0       15    0.081   0      0.000    0.081
````
* jstack pid（进程） |grep 11a（线程16位） -A30线程监控
* jvisualvm 打开vm管理器
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
##JVM参数
##通用参数
###堆
````
-Xms128M	物理内存的1/64(<1GB)	设置java程序启动时堆内存128M。默认(MinHeapFreeRatio参数可以调整)空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制。	生产环境 -Xms 与 -Xmx 最好一样，避免抖动
-Xmx256M	物理内存的1/4(<1GB)	设置最大堆内存256M。默认(MaxHeapFreeRatio参数可以调整)空余堆内存大于70%时，JVM会减少堆直到 -Xms的最小限制，
一般这两个值会设置成一致的，避免在GC完之后动态的调整堆大小，调整内存会消耗系统资源，对应用会有影响
-Xss1M	1024K(1.8+)	设置线程栈的大小 1M（默认1M）
-XX:MinHeapFreeRatio=40	　	设置堆空间最小空闲比例（默认40）（当-Xmx与-Xms相等时，该配置无效）
-XX:ThreadStackSize	　	-Xss 设置在后面，以-Xss为准；  -XX:ThreadStackSize设置在后面，主线程以 -Xss为准，其他线程以  -XX:ThreadStackSize为准
-XX:MinHeapFreeRatio=40	　	设置堆空间最小空闲比例（默认40）（当-Xmx与-Xms相等时，该配置无效）
-XX:MaxHeapFreeRatio=70	　	设置堆空间最大空闲比例（默认70）（当-Xmx与-Xms相等时，该配置无效）
-XX:+UseCompressedOops	　	使用压缩指针，32G内存下默认开启。开启指针压缩，则Object Head 里的Klass Pointer为4字节，复杂属性的引用指针为4字节，数组的长度用4字节表示，这样能够节省部分内存空间
````
###垃圾回收器
````
-XX:MaxGCPauseMillis	200ms	设置最大垃圾收集停顿时间，G1会尽量达到此期望值，如果GC时间超长，那么会逐渐减少GC时回收的区域，以此来靠近此阈值。
-XX:+UseAdaptiveSizePolicy	　	打开自适应GC策略（该摸式下，各项参数都会被自动调整）
-XX:+UseSerialGC	　	在年轻代和年老代使用串行回收器
-XX:+UseParallelGC	　	使用并行垃圾回收收集器，默认会同时启用 -XX:+UseParallelOldGC（默认使用该回收器）。
-XX:+UseParallelOldGC	　	开启老年代使用并行垃圾收集器，默认会同时启用 -XX:+UseParallelGC
-XX:ParallelGCThreads=4	　	设置用于垃圾回收的线程数为4（默认与CPU数量相同）
-XX:+UseConcMarkSweepGC	　	使用CMS收集器（年老代）
-XX:CMSInitiatingOccupancyFraction=80	　	设置CMS收集器在年老代空间被使用多少后触发
-XX:+CMSClassUnloadingEnabled	　	允许对类元数据进行回收。相对于并行收集器，CMS收集器默认不会对永久代进行垃圾回收。如果希望对永久代进行垃圾回收，可通过此参数设置。
-XX:+UseCMSInitiatingOccupancyOnly	　	只在达到阈值的时候，才进行CMS回收
-XX:+CMSIncrementalMode	　	设置为增量模式。适用于单 CPU 情况。该标志将开启CMS收集器的增量模式。增量模式经常暂停CMS过程，以便对应用程序线程作出完全的让步。因此，收集器将花更长的时间完成整个收集周期。因此，只有通过测试后发现正常CMS周期对应用程序线程干扰太大时，才应该使用增量模式。由于现代服务器有足够的处理器来适应并发的垃圾收集，所以这种情况发生得很少。
-XX:+UseG1GC	　	使用G1回收器
-XX:G1HeapRegionSize=16m	　	使用G1收集器时设置每个Region的大小（范围1M - 32M）
-XX:MaxGCPauseMillis=500	　	设置最大暂停时间，单位毫秒。可以减少 STW 时间。
-XX:+DisableExplicitGC	　	禁止显示GC的调用（即禁止开发者的 System.gc();）
-XX:GCTimeRatio=n	G1为9，CMS为99。	设置垃圾回收时间占程序运行时间的百分比。公式为 1/(1+n)并发收集器设置。G1为9，CMS为99。GC时间占总时间的比例，默认值为99，即允许1%的GC时间。仅仅在Parallel Scavenge收集时有效，公式为1/(1+n)。
-XX:CMSFullGCsBeforeCompaction=n	　	由于并发收集器不对内存空间进行压缩、整理，所以运行一段时间以后会产生“碎片”，使得运行效率降低。此值设置运行多少次 GC 以后对内存空间进行压缩、整理。
-XX:+UseCMSCompactAtFullCollection	　	打开对年老代的压缩。可能会影响性能，但是可以消除碎片。
-XX:+CMSParallelRemarkEnabled	　	是否启用并行标记（仅限于ParNewGC）。开启 -XX:+CMSParallelRemarkEnabled
关闭 -XX:-CMSParallelRemarkEnabled
````
###堆快照
````
-XX:+PrintGCDetails	　	打印GC信息
-XX:+PrintGCTimeStamps	　	打印每次GC的时间戳（现在距离启动的时间长度）
-XX:+PrintGCDateStamps	　	打印GC日期
-XX:+PrintHeapAtGC	　	每次GC时，打印堆信息
-Xloggc:/usr/local/tomcat/logs/gc.$$.log	　	GC日志存放的位置
````
###日志
````
-XX:+UseGCLogFileRotation	　	开启滚动日志记录
-XX:NumberOfGCLogFiles=5	　	滚动数量，命名为filename.0, filename.1 ..... filename.n-1, 然后再从filename.0 开始，并覆盖已经存在的文件
-XX:GCLogFileSize=100k	　	每个文件大小，当达到该指定大小时，会写入下一个文件。GC文件滚动大小，需配置UseGCLogFileRotation，设置为0表示仅通过jcmd命令触发
-Xloggc:/gc/log	　	日志文件位置
-XX:+PrintTenuringDistribution	　	打印存活实例年龄信息
-XX:+PrintHeapAtGC	　	GC前后打印堆区使用信息
-XX:+PrintGCApplicationStoppedTime	　	打印GC时应用暂停时间
调优参数
-XX:+PrintFlagsFinal	　	输出所有XX参数和值
-XX:+PrintCommandLineFlags	　	输出JVM设置过的详细的XX参数的名称和值
-XX:+HeapDumpOnOutOfMemoryError	　	抛出内存溢出错误时导出堆信息到指定文件
-XX:HeapDumpPath=/path/heap/dump/gc.hprof	　	当HeapDumpOnOutOfMemoryError开启的时候，dump文件的保存路径，默认为工作目录下的java_pid<pid>.hprof文件
XX:MaxDirectMemorySize	-Xmx	直接内存大小，直接内存非常重要，很多IO处理都需要直接内存参与，直接内存不被jvm管理，所以就不存在GC时内存地址的移动问题，直接内存会作为堆内存和内核之间的中转站。
````
##堆
###新生代
````
-Xmn10M	　	设置新生代区域大小为10M	如果以上三个同时设置了，谁在后面谁生效。生产环境使用-Xmn即可，避免抖动
-XX:NewSize=2M	　	设置新生代初始大小为2M
-XX:MaxNewSize=2M	　	设置新生代最大值为2M
-XX:NewRatio=2	2	新生代:老年代 = 1:2，默认值为2。使用G1时一般此参数不设置，由G1来动态的调整，逐渐调整至最优值。
-XX:SurvivorRatio=8	8	设置年轻代中eden区与survivor区的比例为8，即：eden:from:to = 8:1:1
-XX:TargetSurvivorRatio=50	　	设置survivor区使用率。当survivor区达到50%时，将对象送入老年代
-XX:+UseTLAB	　	在年轻代空间中使用本地线程分配缓冲区(TLAB)，默认开启。Thread Local Allocation Buffer，此区域位于Eden区。当多线程分配内存区块时，因为内存分配和初始化数据是不同的步骤，所以在分配时需要对内存区块上锁，由此会引发区块锁竞争问题。此参数会让线程预先分配一块属于自己的空间(64K-1M)，分配时先在自己的空间上分配，不足时再申请，这样能就不存在内存区块锁的竞争，提高分配效率。
-XX:TLABSize=512k	　	设置TLAB大小为512k
-XX:MaxTenuringThreshold=15	15	设置垃圾对象最大年龄，对象进入老年代的年龄（Parallel是15，CMS是6）。如果设置为 0 的话，则年轻代对象不经过 Survivor 区，直接进入年老代。
-XX:+UseTLAB	　	使用线程本地分配缓冲区。Server模式默认开启。Thread Local Allocation Buffer，此区域位于Eden区。当多线程分配内存区块时，因为内存分配和初始化数据是不同的步骤，所以在分配时需要对内存区块上锁，由此会引发区块锁竞争问题。此参数会让线程预先分配一块属于自己的空间(64K-1M)，分配时先在自己的空间上分配，不足时再申请，这样能就不存在内存区块锁的竞争，提高分配效率。
-XX:InitiatingHeap OccupancyPercent	45	启动并发GC时的堆内存占用百分比。G1用它来触发并发GC周期,基于整个堆的使用率,而不只是某一代内存的使用比例。值为 0 则表示“一直执行GC循环)’. 默认值为 45 (例如, 全部的 45% 或者使用了45%)。
````
###老年代
````
-XX:PretenureSizeThreshold	Region的一半	大对象晋升老年代阈值。
-XX:+HandlePromotionFailure	DK1.6之后，默认开启	老年代是否允许分配担保失败，，即老年代的剩余最大连续空间不足以引用新生代的整个Eden和Survivor区的所有存活对象的极端情况（以此判断是否需要对老年代进行以此FullGC）。
````
###永久代(1.7之前)
-XX:MaxPermSize=n	　	设置永久代大小
###元空间
````
-XX:MetaspaceSize=64M	　	设置元数据空间初始大小（取代-XX:PermSize）
-XX:MaxMetaspaceSize=128M	　	设置元数据空间最大值（取代之前-XX:MaxPermSize）
````
##其它
````
-XX:CompileThreshold	1000	JIT预编译阈值。通过JIT编译器，将方法编译成机器码的触发阀值，可以理解为调用方法的次数，例如调1000次，将方法编译为机器码
-XX:+CITime	　	打印启动时花费在JIT Compiler 时间
-XX:+UseThreadPriorities	　	是否开启线程优先级。默认开启。java 中的线程优先级的范围是1～10，默认的优先级是5。“高优先级线程”会优先于“低优先级线程”执行，也就是竞争CPU时间片时，高优先级线程会被优待。
-XX:+UseSpinning	　	是否使用适应自旋锁，1.6+默认开启。这是对synchronized的优化，当运行到synchronized的代码块时，会尝试自旋，如果在自旋期间获取到了锁，那么下次会逐渐的增加自旋时间，反之则减少自旋时间，当自旋时间减少到一定程度后，会关闭自旋机制一段时间，使用重量级锁。
-XX:PreBlockSpin	10	初始自旋次数，使用自旋锁时初始自旋次数，在此基础上上下浮动，生效前提开UseSpinning。
-XX:RelaxAccessControlCheck	默认关闭	放开通过放射获取方法和属性时的许可校验，在Class校验器中，放松对访问控制的检查,作用与reflection里的setAccessible类似。
````
