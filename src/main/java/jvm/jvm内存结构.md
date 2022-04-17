#程序计数器
- 线程私有，生命周期与线程相同。
- 作用：**程序所执行的字节码的 行号指示器
#本地方法栈
-线程私有，生命周期与线程相同。
-作用： 与虚拟机栈作用相似，不过描述的是Native方法执行的内存模型
#虚拟机栈
- 线程私有，生命周期与线程相同。
- 存放数据：局部变量表（基本数据类型（int等基本类型）、对象引用类型），操作数栈、动态链接、方法出口 等信息
- 调优参数:

    ````
    1. Xss512k：设置栈空间参数的,线程分配的空间用完的时候(递归)，就会抛出栈溢出异常(StackOverflowError)
    ````
#Java堆
- 所有线程共享的一块内存区域，
- 存放数据：所有的对象实例以及数组
- gc主要对象java堆
- 默认垃圾回收器：（年轻代与老年代）：1.8（Parallel）。1.9(G1)
  - 调优参数:
      ````
      1.-Xms2G：初始化内存，默认为物理内存的“1/64”
      2.-Xmx2G：最大内存，默认为物理内存的“1/4”。此值可以设置与-Xmx相同，以避免每次垃圾回收完成后JVM重新分配内存
      3. -Xms3550m：设置JVM初始堆内存为3550M。此值可以设置与-Xmx相同，以避免每次垃圾回收完成后JVM重新分配内存。
      4.-Xmn2g：设置年轻代大小为2G。在整个堆内存大小确定的情况下，增大年轻代将会减小年老代，反之亦然。此值关系到JVM垃圾回收，对系统性能影响较大，官方推荐配置为整个堆大小的3/8。
     ```
## 新生代
* 年轻代用来存放新近创建的对象,产生大量的死亡对象,并且要是产生连续可用的空间, 所以使用复制清除算法和并行收集器进行垃圾回收.对年轻代的垃圾回收称作初级回收 (minor gc)。
* 分为Eden空间、幸存者1区、幸存者2区
* 我们进行垃圾回收的时候，清除正在使用的区域，将其中的存货对象，放入到另一个survivor区域，并进行整理，保证空间的连续。如果对象长时间存活，则将对象移动到老年区。存活下来的对象，他的年龄会增长1。
* YGC触发时机：edn空间不足
* 分代升级时机：
````
1.YGC时，To Survivor区不足以存放存活的对象，对象会直接进入到老年代。
2.经过多次YGC后，如果存活对象的年龄达到了设定阈值，则会晋升到老年代中。
3.动态年龄判定规则，To Survivor区中相同年龄的对象，如果其大小之和占到了 To Survivor区一半以上的空间，那么大于此年龄的对象会直接进入老年代，而不需要达到默认的分代年龄。
4.大对象：由-XX:PretenureSizeThreshold启动参数控制，若对象大小大于此值，就会绕过新生代, 直接在老年代中分配。
````
* 调优参数：
````
-Xmn2g：设置年轻代大小为2G。整个堆大小=年轻代大小 + 年老代大小 + 持久代大小
-XX:NewRatio=4:设置年轻代（包括Eden和两个Survivor区）与年老代的比值（除去持久代）。设置为4，则年轻代与年老代所占比值为1：4，年轻代占整个堆栈的1/5
-XX:SurvivorRatio=4：设置年轻代中Eden区与Survivor区的大小比值。设置为4，则两个Survivor区与一个Eden区的比值为2:4，一个Survivor区占整个年轻代的1/6
-XX:-UseAdaptiveSizePolicy  自适应策略会自动的进行Eden区与Survivor区的分配。
-XX:MaxTenuringThreshold=0：设置垃圾最大年龄。如果设置为0的话，则年轻代对象不经过Survivor区，直接进入年老代。
-XX:PretenureSizeThreshold=3145728 表示对象大于3145728（3M）时直接进入老年代分配，这里只能以字节作为单位
-XX:TargetSurvivorRatio 目标存活率，默认为50%，动态年龄判定规则
-XX:-UseAdaptiveSizePolicy
````
## 老年代
* Full GC 是发生在老年代的垃圾收集动作，Full GC的同时一般都会带着Minor GC，因为Full GC是为了Minor GC后存活的对象能够进入老年代
* 老年代空间分配担保机制：如果新生代有大量对象存活，同时Survivor区也放不下了，那么这些对象必须要放到老年代中，但是如果此时老年代中剩余空间也不足以放下这些对象触发
* full GC触发条件：
````
第一：是老年代可用内存小于新生代全部对象的大小，如果没开启空间担保参数，会直接触发Full GC，所以一般空间担保参数都会打开；注：jDK1.8之后已经取消了-XX:-HandlePromotionFailure机制
第二：是老年代可用内存小于历次新生代GC后进入老年代的平均对象大小，此时会提前Full GC；
第三：是新生代Minor GC后的存活对象大于Survivor，那么就会进入老年代，此时老年代内存不足。
第四如果老年代可用内存大于历次新生代GC后进入老年代的对象平均大小，但是老年代已经使用的内存空间超过了这个参数指定的比例，也会自动触发Full GC。默认92%
第五：如果fullgc后内存还是不够分配则会报oom。
````
#JVM参数
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
-XX:CompileThreshold	1000	JIT预编译阈值。通过JIT编译器，将方法编译成机器码的触发阀值，可以理解为调用方法的次数，例如调1000次，将方法编译为机器码
-XX:+CITime	　	打印启动时花费在JIT Compiler 时间
-XX:+UseThreadPriorities	　	是否开启线程优先级。默认开启。java 中的线程优先级的范围是1～10，默认的优先级是5。“高优先级线程”会优先于“低优先级线程”执行，也就是竞争CPU时间片时，高优先级线程会被优待。
-XX:+UseSpinning	　	是否使用适应自旋锁，1.6+默认开启。这是对synchronized的优化，当运行到synchronized的代码块时，会尝试自旋，如果在自旋期间获取到了锁，那么下次会逐渐的增加自旋时间，反之则减少自旋时间，当自旋时间减少到一定程度后，会关闭自旋机制一段时间，使用重量级锁。
-XX:PreBlockSpin	10	初始自旋次数，使用自旋锁时初始自旋次数，在此基础上上下浮动，生效前提开UseSpinning。
-XX:RelaxAccessControlCheck	默认关闭	放开通过放射获取方法和属性时的许可校验，在Class校验器中，放松对访问控制的检查,作用与reflection里的setAccessible类似。
##最佳实践
###G1垃圾回收器
````
-XX:+PrintAdaptiveSizePolicy 自适应策略，调节Young Old Size，一般G1不会设置新生代和老年代大小，而有G1根据停顿时间逐渐调整新生代和老年代的空间比例
-XX:G1ReservePercent	10	G1为分配担保预留的空间比例，预留10%的内存空间，应对新生代的分配担保情形。
-XX:G1HeapRegionSize	(Xms + Xmx ) /2 / 2048 , 不大于32M，不小于1M，且为2的指数	G1将堆内存默认均分为2048块，1M<region<32 M，当应用频繁分配大对象时，可以考虑调整这个阈值，因为G1的Humongous区域只能存放一个大对象，适当调整Region大小，尽量让其刚好超过大对象的两倍大小，这样就能充分利用Region的空间
-XX:G1ReservePercent	　	G1为分配担保预留的空间比例，默认值为10.预留10%的内存空间，应对新生代的分配担保情形
-XX:G1HeapWastePercent	0.05	触发Mixed GC的可回收空间百分比，默认值5%。在global concurrent marking结束之后，我们可以知道old gen regions中有多少空间要被回收，在每次YGC之后和再次发生Mixed GC之前，会检查垃圾占比是否达到此参数，只有达到了，下次才会发生Mixed GC
-XX:G1MixedGCLive ThresholdPercent	85%	会被MixGC的Region中存活对象占比。
-XX:G1MixedGCCountTarget	8	一次global concurrent marking之后，最多执行Mixed GC的次数
-XX:G1NewSizePercent	5%	新生代占堆的最小比例
-XX:G1MaxNewSizePercent	60%	新生代占堆的最大比例
-XX:GCPauseIntervalMillis	　	指定最短多长可以进行一次gc
-XX:G1OldCSetRegion ThresholdPercent	10%	Mixed GC每次回收Region的数量。一次Mixed GC中能被选入CSet的最多old generation region数量比列
````
