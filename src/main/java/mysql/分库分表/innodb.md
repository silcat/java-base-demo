# 参考文档
* https://mp.weixin.qq.com/s/YXH47C4P2Sc1OQblyZlZzg
* https://www.cnblogs.com/mikevictor07/p/12013507.html
* https://www.cnblogs.com/reecelin/p/13504084.html
#InnoDB:
##后台进程：
* Master Thread：核心线程，负责缓冲池的数据异步入盘，包括脏页刷新、合并插入缓冲、undo页回收等。
* IO Thread：包括read thread 和writer thread，使用show variables like '%innodb_%io_thread%';查看。
* Purge Thread：回收事务提交后不再需要的undo log，通过show variables like '%innodb_purge_threads%'; 查看。
* Page clear thread：脏页的刷新操作，从master thread分离出来
##内存池
* 缓冲池
    * 由于数据存放在磁盘，缓冲池简单来说就是一块内存区域，缓存从磁盘按页读取的数据，若未命中则重磁盘读取（速度慢）
    * 缓存池缓存的数据页类型有：索引页，数据页，undo页，insertByffer等
        * 其中索引页用B+Tree数据结构：https://mp.weixin.qq.com/s/IfiYbWd-YLxO2TvLB8_jcQ?st=&vid=1688852945251408&cst=6C7F761707808A9C9DBB55809CDFE8FC96E42038BBE00C6D343544828825D9FC2A01DF7D99BE6603C97E6B446CF9E610&deviceid=613fb980-925a-4260-9024-2a14c5f1bc83&version=3.1.2.2211&platform=win
    ![img](https://cdn.nlark.com/yuque/0/2019/png/467414/1571583713156-7b4304d4-45c2-40ae-9efc-8b6f22b8a1c1.png)
    * 缓冲池的内存是根据LRU进行管理的，当缓冲池不能存放新读取页信息则首先释放队尾元素

        
        

