# 参考文档
* https://www.cnblogs.com/mikevictor07/p/12013507.html
* https://www.cnblogs.com/reecelin/p/13504084.html
#InnoDB:
##后台进程：
* Master Thread：核心线程，负责缓冲池的数据异步入盘，包括脏页刷新、合并插入缓冲、undo页回收等。
* IO Thread：包括read thread 和writer thread，使用show variables like '%innodb_%io_thread%';查看。
* Purge Thread：回收事务提交后不再需要的undo log，通过show variables like '%innodb_purge_threads%'; 查看。
* Page clear thread：脏页的刷新操作，从master thread分离出来
##内存池
* https://cs704.cn/?p=352
* 缓冲池
    * 由于数据存放在磁盘，缓冲池简单来说就是一块内存区域，缓存从磁盘按页读取的数据，若未命中则重磁盘读取（速度慢）
    * 缓存池缓存的数据页类型有：索引页，数据页，undo页，insertByffer等
        * 其中索引页用B+Tree数据结构
            * https://mp.weixin.qq.com/s/IfiYbWd-YLxO2TvLB8_jcQ?st=&vid=1688852945251408&cst=6C7F761707808A9C9DBB55809CDFE8FC96E42038BBE00C6D343544828825D9FC2A01DF7D99BE6603C97E6B446CF9E610&deviceid=613fb980-925a-4260-9024-2a14c5f1bc83&version=3.1.2.2211&platform=win
            * https://blog.csdn.net/qq_33171970/article/details/88395278?utm_medium=distribute.wap_relevant.none-task-blog-searchFromBaidu-10.wap_blog_relevant_pic&dist_request_id=1328769.69063.16176761626891379&depth_1-utm_source=distribute.wap_relevant.none-task-blog-searchFromBaidu-10.wap_blog_relevant_pic
    ![img](https://cdn.nlark.com/yuque/0/2019/png/467414/1571583713156-7b4304d4-45c2-40ae-9efc-8b6f22b8a1c1.png)
* 索引页与数据页
     * 缓冲池的内存是根据LRU进行管理的，当缓冲池不能存放新读取页信息则首先释放队尾元素
      * 新读取的页不是插入LRU列表头，而是插入到尾端37%的位置，防止热点数据被刷出
      * LRU列表中的页被修改后变为dirty page，此时缓冲池中的页和磁盘不一致，通过checkpoint刷回磁盘，其中Flush list即为dirty page列表。
* Redo log buffer
   * InnoDB将重做日志首先刷入缓冲区中,
   * 刷新缓冲区到磁盘的重做日志文件中时机
        * Master thread定时任务刷新。
        * 每个事务提交。
        * 缓冲区空间小于1/2（如果缓冲区过小则导致频繁的磁盘刷新，降低性能）。 
* checkpoint 机制
    * LSN：1.标记innodb版本信息 2. LSN 是8字节 3.每个数据页，redoLog，checkPoint都存有LSN
    * checkpoint
        * sharpCheckpoint：当数据库关机会将所有脏页刷新到磁盘
        * futtyCheckpoint
            * MasterThread定时任务
            * Flush list（脏页列表）空闲页小于100
            * redoLog不可用,方便redolog循环使用
            * 缓冲池脏页数量够过多，缓冲池脏页数量超过75%，强制刷新一部分脏页到磁盘
            ````
            若每个重做日志大小为1G，定了了两个总共2G，则：
            asyn_water_mark = 75 % * 重做日志总大小。
            syn_water_mark = 90 % * 重做日志总大小。
            当checkpoint_age < asyn_water_mark时则不需要刷新脏页回盘。
            当syn_water_mark < checkpoint_age < syn_water_mark 时触发ASYNC FLUSH。
            当checkpoint_age>syn_water_mark触发sync flush，此情况很少发生，一般出现在大量load data或bulk insert时。
            ````
        * 作用
             * * https://mp.weixin.qq.com/s/YXH47C4P2Sc1OQblyZlZzg
             * 脏页刷新到磁盘
             * crash-safe
                * MySQL启动时，不管上次是正常关闭还是异常关闭，总是会进行恢复操作。会先检查数据页中的LSN，如果这个 LSN 小于 redo log 中的LSN，即write pos位置，说明在redo log上记录着数据页上尚未完成的操作，接着就会从最近的一个check point出发，开始同步数据。
                * 为了解决 partial page write 问题 ，当mysql将脏数据flush到data file的时候, 先使用memcopy 将脏数据复制到内存中的double write buffer ，之后通过double write buffer再分2次，每次写入1MB到共享表空间，然后马上调用fsync函数，同步到磁盘上，避免缓冲带来的问题，在这个过程中，doublewrite是顺序写，开销并不大，在完成doublewrite写入后，在将double write buffer写入各表空间文件，这时是离散写入。
                  如果发生了极端情况（断电），InnoDB再次启动后，发现了一个Page数据已经损坏，那么此时就可以从doublewrite buffer中进行数据恢复了。
                * ![img](https://cdn.nlark.com/yuque/0/2019/png/467414/1571622056598-4df46340-365c-46e8-bafa-7f6f08162fc0.png)
* Insert buffer
    * 若插入按照聚集索引primary key插入，页中的行记录按照primary存放，一般情况下不需要读取另一个页记录，插入速度很快（如果使用UUID或者指定的ID插入而非自增类型则可能导致非连续插入导致性能下降，由B+树特性决定）。
    * 如果按照非聚集索引插入就很有可能存在大量的离散插入，insert buffer对于非聚集索引的插入和更新操作进行一定频率的合并操作，再merge到真正的索引页中。
        * 使用insert buffer需满足条件：
            * 索引为辅助索引。
            * 索引非唯一。（唯一索引需要从查找索引页中的唯一性，可能导致离散读取）        
* innodb_additonal_mem_pool_size
   * 如果申请了很大的buffer pool，此参数应该相应增加，存储了LRU、锁等信息。            
##InnoDB关键特性
* double write.
* adaptive hash index.
* Async IO.
* Flush neighbor page.

