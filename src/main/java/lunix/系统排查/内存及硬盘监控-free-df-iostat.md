#top命令
* 格式: free option  
  * option：
  ````
   -m m以MB为单位显示整个系统的内存使用情况，
   -h 自动选择以适合理解的容量单位显示：
 
  ````
* 格式: df option  
* option：
````
 -m m以MB为单位显示整个系统的硬盘使用情况，
 -h 自动选择以适合理解的容量单位显示：
````
* 格式: iostat option time count
  * option：
  ````
    -c 显示CPU使用情况
    -d 显示磁盘使用情况
    -k 以K为单位显示
    -m 以M为单位显示
    -N 显示磁盘阵列(LVM) 信息
    -n 显示NFS使用情况
    -p 可以报告出每块磁盘的每个分区的使用情况
    -t 显示终端和CPU的信息
    -x 显示详细信息
  ````
   * time：1（每隔1秒）
   * count：2（循环2次）
##命令示例
* free -m > test
##系统状态
````
              total        used        free      shared  buff/cache   available
Mem:         128836      100011        1424          62       27401       25229
Swap:         32767          14       32753
===============================================================================
total 系统总的可用物理内存大小
used 已被使用的物理内存大小
free 还有多少物理内存可用
shared 被共享使用的物理内存大小
buff/cache 被 buffer 和 cache 使用的物理内存大小
available 还可以被 应用程序 使用的物理内存大小
==============================================================================
free 是真正尚未被使用的物理内存数量。
available 是应用程序认为可用内存数量，available = free + buffer + cache (注：只是大概的计算方法)

````
* free -h > test
````
              total        used        free      shared  buff/cache   available
Mem:           125G         97G        1.2G         62M         26G         24G
Swap:           31G         14M         31G
````
* df -h 
````
Filesystem                                            Size  Used Avail Use% Mounted on
overlay                                               7.7T   59G  7.6T   1% /
tmpfs                                                  64M     0   64M   0% /dev
tmpfs                                                  63G     0   63G   0% /sys/fs/cgroup
/dev/sda6                                             7.7T   59G  7.6T   1% /etc/hosts
shm                                                    64M     0   64M   0% /dev/shm
tmpfs                                                  63G     0   63G   0% /proc/acpi
tmpfs                                                  63G     0   63G   0% /proc/scsi
tmpfs                                                  63G     0   63G   0% /sys/firmware
172.16.2.57:test_volume/haofenqi/haofenqi-api-server  183G  146G   38G  80% /opt/webroot/youxin_img_gfs
````
* iostat -d 2 3
##系统状态
````
Linux 3.10.0-693.2.2.el7.x86_64 (jellythink)    01/05/2019      _x86_64_        (1 CPU)

Device:            tps    kB_read/s    kB_wrtn/s    kB_read    kB_wrtn
vda               1.62        12.64        20.67  337375593  551756524

Device:            tps    kB_read/s    kB_wrtn/s    kB_read    kB_wrtn
vda               1.00         0.00         8.00          0         16

Device:            tps    kB_read/s    kB_wrtn/s    kB_read    kB_wrtn
vda               0.00         0.00         0.00          0          0
===============================================================================
tps：每秒I/O数（即IOPS。磁盘连续读和连续写之和）
kB_read/s：每秒从磁盘读取数据大小，单位KB/s
kB_wrtn/s：每秒写入磁盘的数据的大小，单位KB/s
kB_read：从磁盘读出的数据总数，单位KB
kB_wrtn：写入磁盘的的数据总数，单位KB
==============================================================================
````
* iostat -x
````
avg-cpu:  %user   %nice %system %iowait  %steal   %idle
           1.83    0.00    0.31    0.09    0.00   97.77

Device:         rrqm/s   wrqm/s     r/s     w/s    rkB/s    wkB/s avgrq-sz avgqu-sz   await r_await w_await  svctm  %util
vda               0.03     0.78    0.24    1.38    12.64    20.67    41.01     0.02   10.98   55.50    3.17   0.71   0.1
==========================================================================================
%user：CPU处在用户模式下的时间百分比
%nice：CPU处在带NICE值的用户模式下的时间百分比
%system：CPU处在系统模式下的时间百分比
%iowait：CPU等待输入输出完成时间的百分比
%steal：管理程序维护另一个虚拟处理器时，虚拟CPU的无意识等待时间百分比
%idle：CPU空闲时间百分比

当然了，iostat命令的重点不是用来看CPU的，重点是用来监测磁盘性能的。

Device：设备名称
rrqm/s：每秒合并到设备的读取请求数
wrqm/s：每秒合并到设备的写请求数
r/s：每秒向磁盘发起的读操作数
w/s：每秒向磁盘发起的写操作数
rkB/s：每秒读K字节数
wkB/s:每秒写K字节数
avgrq-sz：平均每次设备I/O操作的数据大小
avgqu-sz：平均I/O队列长度
await：平均每次设备I/O操作的等待时间 (毫秒)，一般地，系统I/O响应时间应该低于5ms，如果大于 10ms就比较大了
r_await：每个读操作平均所需的时间；不仅包括硬盘设备读操作的时间，还包括了在kernel队列中等待的时间
w_await：每个写操作平均所需的时间；不仅包括硬盘设备写操作的时间，还包括了在kernel队列中等待的时间
svctm：平均每次设备I/O操作的服务时间 (毫秒)（这个数据不可信！）
%util：一秒中有百分之多少的时间用于I/O操作，即被IO消耗的CPU百分比，一般地，如果该参数是100%表示设备已经接近满负荷运行了
====================================数据分析====================================================
%iowait：如果该值较高，表示磁盘存在I/O瓶颈
await：一般地，系统I/O响应时间应该低于5ms，如果大于10ms就比较大了
avgqu-sz：如果I/O请求压力持续超出磁盘处理能力，该值将增加。如果单块磁盘的队列长度持续超过2，一般认为该磁盘存在I/O性能问题。需要注意的是，如果该磁盘为磁盘阵列虚拟的逻辑驱动器，需要再将该值除以组成这个逻辑驱动器的实际物理磁盘数目，以获得平均单块硬盘的I/O等待队列长度
%util：一般地，如果该参数是100%表示设备已经接近满负荷运行了
最后，除了关注指标外，我们更需要结合部署的业务进行分析。对于磁盘随机读写频繁的业务，比如图片存取、数据库、邮件服务器等，此类业务吗，tps才是关键点。对于顺序读写频繁的业务，需要传输大块数据的，如视频点播、文件同步，关注的是磁盘的吞吐量。

````



