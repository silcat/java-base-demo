#cat 命令
* 格式: jstat option 
  * option：
  ````
    -class 类加载统计是
    -gc 垃圾回收统计
    -gcutil 总结垃圾回收统计
  ````
* 格式: jmap -option pid
  * option：
  ````
   - heap： 显示Java堆详细信息
   - histo[:live]： 显示堆中对象的统计信息
   - dump:<dump-options>：生成堆转储快照
   - no option： 查看进程的内存映像信息,类似 Solaris pmap 命令。
   - clstats：打印类加载器信息
   - finalizerinfo： 显示在F-Queue队列等待Finalizer线程执行finalizer方法的对象
   - F： 当-dump没有响应时，使用-dump或者-histo参数. 在这个模式下,live子参数无效.
   - help：打印帮助信息
   - J<flag>：指定传递给运行jmap的JVM的参数
  ````  
##表达式

* jstat -gc 90
  
````
 S0C    S1C     S0U    S1U      EC       EU        OC         OU       MC       MU      CCSC   CCSU       YGC     YGCT    FGC    FGCT     GCT   
 0.0   48128.0  0.0   48128.0 612352.0 285696.0  388096.0   36921.0   100684.0 95580.3 12620.0 11736.7     23    0.943    0      0.000    0.943
=======================================================================================================
S0C：第一个幸存区的大小
S1C：第二个幸存区的大小
S0U：第一个幸存区的使用大小
S1U：第二个幸存区的使用大小
EC：伊甸园区的大小
EU：伊甸园区的使用大小
OC：老年代大小
OU：老年代使用大小
MC：方法区大小
MU：方法区使用大小
CCSC:压缩类空间大小
CCSU:压缩类空间使用大小
YGC：年轻代垃圾回收次数
YGCT：年轻代垃圾回收消耗时间
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
````
* jstat -gcutil 19570
````
S0     S1     E      O      M     CCS      YGC     YGCT    FGC    FGCT     GCT   
0.00 100.00  60.54   9.51  94.93  93.00     23    0.943     0    0.000    0.943                                                                          
==============================================================================
G1垃圾回收器只有一类幸存者区
                                                                     
````
* jstat -gccapacity 90
````
 NGCMN    NGCMX     NGC         S0C   S1C       EC          OGCMN      OGCMX       OGC         OC           MCMN     MCMX      MC       CCSMN    CCSMX     CCSC    YGC    FGC 
 0.0      1048576.0 660480.0    0.0 48128.0 612352.0        0.0        1048576.0   388096.0   388096.0      0.0 1136640.0  100684.0      0.0 1048576.0  12620.0     23     0
 ===========================所占比例===================================================
 NGCMN：新生代最小容量
 NGCMX：新生代最大容量
 NGC：当前新生代容量
 S0C：第一个幸存区大小
 S1C：第二个幸存区的大小
 EC：伊甸园区的大小
 OGCMN：老年代最小容量
 OGCMX：老年代最大容量
 OGC：当前老年代大小
 OC:当前老年代大小
 MCMN:最小元数据容量
 MCMX：最大元数据容量
 MC：当前元数据空间大小
 CCSMN：最小压缩类空间大小
 CCSMX：最大压缩类空间大小
 CCSC：当前压缩类空间大小
 YGC：年轻代gc次数
 FGC：老年代GC次数     
````
* jmap -heap 90
````
Attaching to process ID 90, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.131-b11

using thread-local object allocation.
Garbage-First (G1) GC with 28 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 536870912 (512.0MB)
   NewSize                  = 1363144 (1.2999954223632812MB)
   MaxNewSize               = 321912832 (307.0MB)
   OldSize                  = 5452592 (5.1999969482421875MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 1048576 (1.0MB)

Heap Usage:
G1 Heap:
   regions  = 512
   capacity = 536870912 (512.0MB)
   used     = 178270304 (170.01181030273438MB)
   free     = 358600608 (341.9881896972656MB)
   33.20543169975281% used
G1 Young Generation:
Eden Space:
   regions  = 77
   capacity = 335544320 (320.0MB)
   used     = 80740352 (77.0MB)
   free     = 254803968 (243.0MB)
   24.0625% used
Survivor Space:
   regions  = 3
   capacity = 3145728 (3.0MB)
   used     = 3145728 (3.0MB)
   free     = 0 (0.0MB)
   100.0% used
G1 Old Generation:
   regions  = 91
   capacity = 198180864 (189.0MB)
   used     = 94384224 (90.01181030273438MB)
   free     = 103796640 (98.98818969726562MB)
   47.625296456473215% used

38537 interned Strings occupying 4401704 bytes.
````
* jmap -histo:live 90 
````
          对象数量          占用内存   对象名
 num     #instances         #bytes  class name
----------------------------------------------
   1:        134799       15606792  [C
   2:          7194        7586072  [B
   3:         36819        4065440  [Ljava.lang.Object;
   4:        123305        3945760  java.util.concurrent.ConcurrentHashMap$Node
   5:         38576        3394688  java.lang.reflect.Method
   6:        133789        3210936  java.lang.String
   7:         19543        2171720  java.lang.Class
                                                                     
````
* jmap -dump:live,format=b,file=heap.bin 90