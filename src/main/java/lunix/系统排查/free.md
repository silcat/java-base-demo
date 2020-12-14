#top命令
* 格式: free option  
  * option：
  ````
   -m m以MB为单位显示整个系统的内存使用情况，
   -h 自动选择以适合理解的容量单位显示：
 
  ````
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



