#top命令
* 格式: pidstat option time count  
  * option：
  ````
    -u 默认的参数，显示各个进程的CPU使用统计
    -r 显示各个进程的内存使用统计
    -d 显示各个进程的IO使用情况
    -p 指定进程号
    -w 显示每个进程的上下文切换情况
    -t 显示选择任务的线程的统计信息外的额外信息
    -T { TASK | CHILD | ALL }
    这个选项指定了pidstat监控的。TASK表示报告独立的task，CHILD关键字表示报告进程下所有线程统计信息。ALL表示报告独立的task和task下面的所有线程。
    注意：task和子线程的全局的统计信息和pidstat选项无关。这些统计信息不会对应到当前的统计间隔，这些统计信息只有在子线程kill或者完成的时候才会被收集。
    -V：版本号
    -h：在一行上显示了所有活动，这样其他程序可以容易解析。
    -I：在SMP环境，表示任务的CPU使用率/内核数量
    -l：显示命令名和所有参数
  ````
* 格式: ps option pid 
* option：
````
   -f: full 展示进程详细信息
   -e: every 展示所有进程信息
   -ax: all 与 -e 同,展示所有进程信息
   -o: 设置输出格式, 可以指定需要输出的进程信息列
   -L: 展示线程信息
   -C: 获取指定命令名的进程信息
   -t: tty 展示关联指定 tty 的进程
   --forest: 展示进程数
   --sort: 按照某个或者某些进程信息列排序展示
````
##命令示例
* pidstat  
##系统状态
````
Linux 4.19.162-1.el7.centos.x86_64 (0-hfq-institution-testd.app.local)  12/15/2020      _x86_64_        (40 CPU)

06:19:19 PM   UID       PID    %usr %system  %guest    %CPU   CPU  Command
06:19:19 PM     0         1    0.00    0.00    0.00    0.00     4  sh
06:19:19 PM     0        66    0.25    0.03    0.00    0.29     6  glusterfs
06:19:19 PM    28        82    0.00    0.00    0.00    0.00     2  nscd
06:19:19 PM   502        90    0.51    1.04    0.00    1.55    10  java
===============================================================================
UID：用户ID
PID：进程ID
%usr：进程在用户空间占用CPU的百分比
%system：进程在内核空间占用CPU的百分比
%guest：任务花费在虚拟机上的CPU使用率（运行在虚拟处理器）
%CPU：任务总的CPU使用率
CPU：正在运行这个任务的处理器编号
Command：这个任务的命令名称
````
* pidstat -r -p 90 
````
06:24:12 PM   UID       PID  minflt/s  majflt/s     VSZ    RSS   %MEM  Command
06:24:12 PM   502        90      6.81      0.00 22405428 1024700   0.78  java
===============================================================================
minflt/s：从内存中加载数据时每秒出现的次要错误的数目，这些不要求从磁盘载入内存页面
majflt/s：从内存中加载数据时每秒出现的主要错误的数目，这些要求从磁盘载入内存页面
VSZ：虚拟地址大小，虚拟内存的使用KB
RSS：长期内存使用，任务的不可交换物理内存的使用量KB
%MEM：进程使用的物理内存百分比，top命令也会输出该字段
````
* pidstat -d -p 90 1 5
````
06:26:43 PM   UID       PID   kB_rd/s   kB_wr/s kB_ccwr/s  Command
06:26:44 PM   502        90      0.00      0.00      0.00  java
06:26:45 PM   502        90      0.00      0.00      0.00  java
06:26:46 PM   502        90      0.00      0.00      0.00  java
06:26:47 PM   502        90      0.00      0.00      0.00  java
06:26:48 PM   502        90      0.00      0.00      0.00  java
Average:      502        90      0.00      0.00      0.00  java
===============================================================================
kB_rd/s：进程每秒从磁盘读取的数据量(以kB为单位)
kB_wr/s：进程每秒向磁盘写入的数据量(以kB为单位)
kB_ccwr/s：任务写入磁盘被取消的速率（KB）；当任务截断脏的pagecache的时候会发生。
````
* pidstat -t -p 90 
````
06:28:50 PM   UID      TGID       TID    %usr %system  %guest    %CPU   CPU  Command
06:28:50 PM   502        90         -    0.51    1.04    0.00    1.55    10  java
06:28:50 PM   502         -        90    0.00    0.00    0.00    0.00    10  |__java
06:28:50 PM   502         -        97    0.00    0.00    0.00    0.00     1  |__java
06:28:50 PM   502         -        98    0.00    0.00    0.00    0.00     7  |__java
===============================================================================
TGID：主线程的标识线程组ID，也就是线程组leader的进程ID，等于pid。
TID：线程ID
````
* pidstat -T ALL -p 90 
````
06:34:24 PM   UID       PID    %usr %system  %guest    %CPU   CPU  Command
06:34:24 PM   502        90    0.51    1.04    0.00    1.55    10  java

06:34:24 PM   UID       PID    usr-ms system-ms  guest-ms  Command
06:34:24 PM   502        90   2650550   5431890         0  java
==========================================================================
PID:进程id
Usr-ms:任务和子线程在用户级别使用的毫秒数。
System-ms:任务和子线程在系统级别使用的毫秒数。
Guest-ms:任务和子线程在虚拟机(running a virtual processor)使用的毫秒数。
Command:命令名
````
* ps -aux (查看进程cpu和内存)
````
USER       PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND
app         17  0.2  0.7 12799748 952352 ?     Sl   Dec11  20:32 java -server -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateSta
root      8453  0.0  0.0  53320  3912 pts/1    R+   11:16   0:00 ps -aux

````
* ps -ef(查看进程cpu和内存)
````
UID        PID  PPID  C STIME TTY          TIME CMD
root         1     0  0 Dec11 ?        00:00:00 sh /run.sh
app         17     1  0 Dec11 ?        00:21:12 java -server -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCT
root      8777  8442  0 15:32 pts/1    00:00:00 ps -ef
````
* ps -Lf 17(查看进程cpu的线程)
````
 UID        PID  PPID   LWP  C NLWP STIME TTY      STAT   TIME CMD
 app         17     1    17  0  159 Dec11 ?        Sl     0:00 java -server -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 app         17     1    24  0  159 Dec11 ?        Sl     0:10 java -server -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 app         17     1    25  0  159 Dec11 ?        Sl     0:01 java -serv
 =====================================================================================================
 PPID ：父进程id
 LWP：线程id
 NLWP : 线程的数量
````
* ps -Lf 17(查看进程cpu的线程)
````
 UID        PID  PPID   LWP  C NLWP STIME TTY      STAT   TIME CMD
 app         17     1    17  0  159 Dec11 ?        Sl     0:00 java -server -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 app         17     1    24  0  159 Dec11 ?        Sl     0:10 java -server -Xms512m -Xmx512m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 app         17     1    25  0  159 Dec11 ?        Sl     0:01 java -serv
 =====================================================================================================
 PPID ：父进程id
 LWP：线程id
 NLWP : 线程的数量
````
* ps -mp 17 -o THREAD,tid,time 
````
USER     %CPU PRI SCNT WCHAN  USER SYSTEM   TID     TIME
app       0.2   -    - -         -      -     - 00:21:25
app       0.0  19    - futex_    -      -    17 00:00:00
app       0.0  19    - futex_    -      -    24 00:00:10
app       0.0  19    - futex_    -      -    25 00:00:01
app       0.0  19    - futex_    -      -    26 00:00:01
app       0.0  19    - futex_    -      -    27 00:00:01
app       0.0  19    - futex_    -      -    28 00:00:01
app       0.0  19    - futex_    -      -    29 00:00:01
app       0.0  19    - futex_    -      -    30 00:00:01
````
* 理解 Linux的进程，线程，PID，LWP，TID，TGID
  * 1.含义
````
pid: 进程ID。
lwp: 线程ID。在用户态的命令(比如ps)中常用的显示方式。
tid: 线程ID，等于lwp。tid在系统提供的接口函数中更常用，比如syscall(SYS_gettid)和syscall(__NR_gettid)。
tgid: 线程组ID，也就是线程组leader的进程ID，等于pid。
------分割线------
pgid: 进程组ID，也就是进程组leader的进程ID。
pthread id: pthread库提供的ID，生效范围不在系统级别，可以忽略。
sid: session ID for the session leader。
tpgid: tty process group ID for the process group leader
````  
  * 2.理解
    *  PID=TGID: TID=LWP
    *  对于用户程序lwp是一个线程，但对于对于lunix系统层面讲没有线程，lwp是一个轻量级进程。