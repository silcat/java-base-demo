*https://juejin.cn/post/7001406102621388831/
*https://www.cnblogs.com/chenpt/p/9803298.html
*https://zhuanlan.zhihu.com/p/54048685
*https://zhuanlan.zhihu.com/p/338682253
#设置vm参数
##idea设置
- 设置vmoption
##tomcat设置
- 修改TOMCAT_HOME/bin/catalina.sh JAVA_OPTS="-server -Xms256m -Xmx512m"
##springboot设置
- 在项目部署后，在启动的时候，采用脚本或者命令行运行的时候设置。
- java -jar  -Xms256m -Xmx512m" demo.jar
#综述
- 新生代收集器：Serial ，ParNew ，parallel 
- 老年代收集器：Serial Old ，CMS ，Parallel Old
- G1都支持
- 默认垃圾回收器（年轻代与老年代）：1.8（Parallel），1.9(G1)
- 查看当前垃圾回收器：java -XX:+PrintCommandLineFlags -version
    ````
    -XX:InitialHeapSize=257798976 
    -XX:MaxHeapSize=4124783616
    -XX:+UseCompressedClassPointers
    -XX:+UseCompressedOops
    -XX:-UseLargePagesIndividualAllocation
    -XX:+UseParallelGC
    -XX:+PrintCommandLineFlags
    java version "1.8.0_211"
    Java(TM) SE Runtime Environment (build 1.8.0_211-b12)
    Java HotSpot(TM) 64-Bit Server VM (build 25.211-b12, mixed mode)
    ````
#存活对象回收算法
##引用计数算法
- 给对象添加一个引用计数器，每当有一个地方引用它时计数器就+1，当引用失效时计数器就-1,。只要计数器等于0的对象就是不可能再被使用的。
- 很难解决对象之间循环引用的问题，java虚拟机中是没有使用的
##可达性分析算法
- 通过一系列的称为“GC Roots”的对象作为起始点，从这些节点开始向下搜索，搜索所走过的路径称为引用链，当一个对象到GC Roots没有使用任何引用链时，则说明该对象是不可用的。
- java虚拟机中是采用，虽然解决了循环依赖问题但是会引起Stop The World"
- GC Roots 对象：两栈两方法对象（https://blog.csdn.net/weixin_38007185/article/details/108093716）
````
虚拟机栈中引用的对象,方法区中类静态属性引用的对象,方法区中常量引用的对象（字符串常量池对象）,本地方法栈中 JNI 引用的对象
````
- 引用类型:虽然被赋值有可达节点，但依然会被回收
````
强引用：指在代码中普遍存在的，类似 Object obj = new Object(); 这类的引用，只有强引用还存在，GC就永远不会收集被引用的对象。
软引用：指一些还有用但并非必须的对象。直到内存空间不够时（抛出OutOfMemoryError之前），才会被垃圾回收。采用SoftReference类来实现软引用
弱引用：用来描述非必须对象。当垃圾收集器工作时就会回收掉此类对象。采用WeakReference类来实现弱引用。
虚引用：一个对象是否有虚引用的存在，完全不会对其生存时间构成影响， 唯一目的就是能在这个对象被回收时收到一个系统通知， 采用PhantomRenference类实现
````
- GC Roots 对象回收（针对方法区对象）
````
回收废弃常量：字符串常量池“abc”无对象引用，将会回收
回收无用的类：该类所有的实例都已经被回收，也就是Java堆中无任何改类的实例，加载该类的ClassLoader已经被回收，该类对应的java.lang.Class对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法
````
#垃圾回收算法
##标记-清除算法
- 一次标记：在经过可达性分析算法后，对象没有与GC Root相关的引用链，那么则被第一次标记。并且进行一次筛选：当对象有必要执行finalize()方法时，则把该对象放入F-Queue队列中。
- 二次标记：对F-Queue队列中的对象进行二次标记。在执行finalize()方法时，如果对象重新与GC Root引用链上的任意对象建立了关联，则把他移除出“ 即将回收 ”集合。否则就等着被回收吧！！！
- 对被第一次标记切被第二次标记的，就可以判定位可回收对象了。
##复制算法
- 可用内存按容量分为大小相等的两块，每次只使用其中一块，当这一块的内存用完了，就将还存活的对象复制到另外一块内存上，然后再把已使用过的内存空间一次清理掉。
- 解决标记-清除算法导致的内存碎片问题但用空间换效率
- HotSpot虚拟机的改良算法：java虚拟机新生代中98%的对象都是"朝生夕死"； 所以并不需要按1:1比例来划分内存，新生代收集下来的存活对象，那么这些对象则直接通过担保机制进入老年代
## 标记-整理算法
-针对老年代特点的回收算法
-该算法根据存活对象进行整理。让存活对象都向一端移动，然后直接清理掉边界以外的内存
## 分代收集
#垃圾回收器
##Serial 收集器
- 单线程
- 年轻代复制算法，老年代标记-整理算法
##ParNew收集器
-ParNew收集器其实就是Serial收集器的多线程版本。
-除了使用多线程外其余行为均和Serial收集器一模一样（参数控制、收集算法、Stop The World、对象分配规则、回收策略等）。
##parallel
- 特点:该收集器的目标是达到一个可控制的吞吐量，故也称为吞吐量优先收集器
- 应用场景:科学计算多采用parallel
- 新生代收集器也是采用复制算法的收集器，并行的多线程收集器（ParNew收集器）
- 重要特点GC自适应调节策略：虚拟机会根据系统的运行状况收集性能监控信息，动态设置这些参数以提供最优的停顿时间和最高的吞吐量，这种调节方式称为GC的自适应调节策略。
- Parallel Scavenge收集器使用两个参数控制吞吐量：
````
XX:MaxGCPauseMillis 控制最大的垃圾收集停顿时间
XX:GCRatio 直接设置吞吐量的大小。
````
##CMS收集器

- 特点：以获取最短回收停顿时间为目标的收集器,基于标记-清除算法实现。并发收集、低停顿。
- 应用场景：适用于注重服务的响应速度，希望系统停顿时间最短，给用户带来更好的体验等场景下。如web程序、b/s服务。
- CMS收集器的运行过程分为下列4步：
````
初始标记：标记GC Roots能直接到的对象。速度很快但是仍存在Stop The World问题。
并发标记：进行GC Roots Tracing 的过程，找出存活对象且用户线程可并发执行。
重新标记：为了修正并发标记期间因用户程序继续运行而导致标记产生变动的那一部分对象的标记记录。仍然存在Stop The World问题。
并发清除：对标记的对象进行清除回收。
````
- CMS收集器的内存回收过程是与用户线程一起并发执行的,对Cpu资源敏感，默认回收线程数：（CPU数量+3）/4。
- CMS未采用标记-整理会产生内存碎片，大对象分配会因无法找到连续内存空间而触发FGC,解决方式:
````
+UseCMSCompactAtFullCollection参数：在CMS要进行FGC时开启内存碎片的合并整理过程。默认开启。
引发问题：内存整理导致停顿时间变长
-XX:CMSFullGCsBeforeCompaction参数：设置N次不压缩的FGC后跟着来一次带压缩的FGC。默认为0，即每次FGC都进行碎片整理。
-XX:CMSInitiatingOccupancyFaction”参数，CMS垃圾收集器，当老年代达到70%时，触发CMS垃圾回收
````
##G1收集器
- 是一种服务端应用使用的垃圾收集器，目标是用在多核、大内存的机器上，它在大多数情况下可以实现指定的GC暂停时间，同时还能保持较高的吞吐量。
###特点如下：
- 并行与并发：G1能充分利用多CPU、多核环境下的硬件优势，使用多个CPU来缩短Stop-The-World停顿时间。部分收集器原本需要停顿Java线程来执行GC动作，G1收集器仍然可以通过并发的方式让Java程序继续运行。
- 分代收集：G1能够独自管理整个Java堆，并且采用不同的方式去处理新创建的对象和已经存活了一段时间、熬过多次GC的旧对象以获取更好的收集效果。
- 空间整合：G1运作期间不会产生空间碎片，收集后能提供规整的可用内存。
- 可预测的停顿：G1除了追求低停顿外，还能建立可预测的停顿时间模型。能让使用者明确指定在一个长度为M毫秒的时间段内，消耗在垃圾收集上的时间不得超过N毫秒。
###G1模型
- 分区region:堆被分成一块块大小相等的heap region，一般有2000多块，这些region在逻辑上是连续的。每块region都会被打唯一的分代标志(eden,survivor,old)。在逻辑上，eden regions构成Eden空间，survivor regions构成幸存者空间，old regions构成了老年代空间
- 收集集合（CSet）：一组可被回收的分区的集合。在CSet中存活的数据会在GC过程中被移动到另一个可用分区，CSet中的分区可以来自Eden空间、survivor空间、或者老年代。CSet会占用不到整个堆空间的1%大小。
- 已记忆集合（RSet）：RSet记录了其他Region中的对象引用本Region中对象的关系，属于points-into结构（谁引用了我的对象）。RSet的价值在于使得垃圾收集器不需要扫描整个堆找到谁引用了当前分区中的对象，只需要扫描RSet即可。
###收集算法
- G1中YGC依然采用复制存活对象到survivor空间，
- G1在回收老年代的分区时，是将存活的对象从一个分区拷贝到另一个可用分区，这个拷贝的过程就实现了局部的压缩，类似标记-整理
### G1收集器步骤：
- 初始标记：仅标记GC Roots能直接到的对象，并且修改TAMS（Next Top at Mark Start）的值，让下一阶段用户程序并发运行时，能在正确可用的Region中创建新对象。（需要线程停顿，但耗时很短。）
- 并发标记：从GC Roots开始对堆中对象进行可达性分析，找出存活对象。（耗时较长，但可与用户程序并发执行）
- 最终标记：为了修正在并发标记期间因用户程序执行而导致标记产生变化的那一部分标记记录。且对象的变化记录在线程Remembered Set  Logs里面，把Remembered Set  Logs里面的数据合并到Remembered Set中。（需要线程停顿，但可并行执行。）
- 筛选回收：对各个Region的回收价值和成本进行排序，根据用户所期望的GC停顿时间来制定回收计划。（可并发执行）
### 调优最佳实践
- 不要手动设置新生代和老年代的大小，只设置这个堆的大小，如果手动设置了大小就意味着放弃了G1的自动调优。
- -XX:+UseG1GC 告诉JVM使用G1收集器
- -XX:MaxGCPauseMillis=200 设置最大GC目标最大停顿时间为200ms，这是一个软指标。JVM会最大努力去达到它，一般情况下这个值设置到100ms或者200ms，对这个参数的调优是一个持续的过程
- 大对象碎片调优
- 
````
-XX:G1HeapRegionSize 大对象碎片调优,堆内存耗尽之前，为了找到一系列连续的region也会导致FullGC
-XX:PretenureSizeThreshold 	大对象晋升老年代阈值。
````
- YGC调优：youngGC花费的时间一般跟young区的大小成正比例关系，更确切的说跟young区的回收集合中的要拷贝的存活对象的数量有关。
````
-XX:TargetSurvivorRatio:Survivor区的填充容量(默认50%)，Survivor区域里的一批对象(年龄1+年龄2+年龄n的多个年龄对象)总和超过了Survivor区域的50%，此时就会把年龄n(含)以上的对象都放入老年代
-XX:MaxTenuringThreshold:新生代晋升最大年龄阈值(默认15)
-XX:G1NewSizePercent:新生代内存初始空间(默认整堆5%) 减小young区的最小的大小，会减小停顿时间
-XX:G1MaxNewSizePercent:新生代内存最大空间
-XX:G1ReservePercent：G1为分配担保预留的空间比例，默认值为10.预留10%的内存空间，应对新生代的分配担保情形。如果经常发生新生代晋升失败而导致Full GC，那么可以适当调高此阈值。但是调高此值同时也意味着降低了老年代的实际可用空间。
````
- MixedGC时间太长：MixedGC主要用来回收old区的内存。MixedGC的回收集合包含了young区和old区的region，启用日志中的 gc+ergo+cset=trace，就可以得到young区和old区的回收对停顿时间分别做了多大贡献的信息
````
-XX:G1MixedGCCountTarget 增大该值，一次global concurrent marking之后，最多执行Mixed GC的次数
-XX:G1MixedGCLive ThresholdPercent 85%	会被MixGC的Region中存活对象占比。避免把那些回收时间长的region加入到回收集合。很多时候，存活对象占有率高的region会花费更多的时间来回收。
-XX:G1HeapWastePercent	0.05	触发Mixed GC的可回收空间百分比，默认值5%。在global concurrent marking结束之后，我们可以知道old gen regions中有多少空间要被回收，在每次YGC之后和再次发生Mixed GC之前，会检查垃圾占比是否达到此参数，只有达到了，下次才会发生Mixed GC
-XX:InitiatingHeapOccupancyPercent=45 启动并发标记标记百分比，当整堆内存使用量达到百分比时，G1使用它来触发一个基于整个堆的并发标记循环，而不仅仅是一个代。默念值是45
````
- 调优日志
````
‐XX:+PrintGCDetails->打印GC日志
‐XX:+PrintGCTimeStamps->打印GC时间
‐XX:+PrintGCDateStamps->打印GC日期
-XX:+PrintAdaptiveSizePolic
-XX:+PrintGCDetails
-Xloggc:gc.log 
````
###G1日志
* https://www.cnblogs.com/javaadu/p/11220234.html
````

22-04-18T05:37:34.116+0800: 0.666: [GC pause (G1 Evacuation Pause) (young) 0.666: [G1Ergonomics (CSet Construction) start choosing CSet, _pending_cards: 256, predicted base time: 7.48 ms, remaining time: 192.52 ms, target pause time: 200.00 ms]
 0.666: [G1Ergonomics (CSet Construction) add young regions to CSet, eden: 19 regions, survivors: 2 regions, predicted young region time: 146.02 ms]
 0.666: [G1Ergonomics (CSet Construction) finish choosing CSet, eden: 19 regions, survivors: 2 regions, old: 0 regions, predicted pause time: 153.50 ms, target pause time: 200.00 ms]
, 0.0030407 secs]
   [Parallel Time: 1.9 ms, GC Workers: 10]
      [GC Worker Start (ms): Min: 666.4, Avg: 666.5, Max: 666.5, Diff: 0.1]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.2, Max: 1.1, Diff: 1.1, Sum: 1.9]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.2]
         [Processed Buffers: Min: 0, Avg: 0.1, Max: 1, Diff: 1, Sum: 1]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.2]
      [Object Copy (ms): Min: 0.7, Avg: 1.5, Max: 1.7, Diff: 1.0, Sum: 15.3]
      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]
         [Termination Attempts: Min: 1, Avg: 57.6, Max: 76, Diff: 75, Sum: 576]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 1.7, Avg: 1.8, Max: 1.8, Diff: 0.1, Sum: 17.8]
      [GC Worker End (ms): Min: 668.3, Avg: 668.3, Max: 668.3, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.2 ms]
   [Other: 1.0 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.8 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 19.0M(19.0M)->0.0B(73.0M) Survivors: 2048.0K->3072.0K Heap: 21.4M(128.0M)->5357.8K(128.0M)]
 [Times: user=0.09 sys=0.06, real=0.00 secs] 
````




