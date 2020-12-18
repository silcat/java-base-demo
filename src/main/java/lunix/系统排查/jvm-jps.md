#cat 命令
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
* jstat -gcutil 19570
````
S0     S1     E      O      M     CCS      YGC     YGCT    FGC    FGCT     GCT   
0.00 100.00  60.54   9.51  94.93  93.00     23    0.943     0    0.000    0.943                                                                          
===========================所占比例===================================================
                                                                     
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