##数据结构
* 查看文档：https://www.cnblogs.com/sgh1023/p/10123767.html
* String
    * get(key) 获取值
    * set(key,value) 放入值
    * incr(key)	key自增1，如果key不存在，自增后get(key)=1
    * decr(key)	key自减1，如果key不存在，自减后get(key)=-1
    * setnx(key, value)	key不存在，才设置
* Map
* List
    * lpush(key,value)    将元素推入列表的左端
    * rpush(key,value)     将元素推入列表的右端   
    * lpop(key)    将元素从列表左端弹出   
    * rpop(key)    将元素从列表右端弹出  
    * blpop(key) 在没有消息的时候，它会阻塞住直到消息到来  
    * LINDEX    获取列表在给定位置上的单个元素   
    * lrange(key，start，end)     获取列表在给定范围的所有元素
* Set
    * sadd(key,value)  向集合key添加element（如果element已经存在，添加失败）  
    * srem(key)       将集合key中的element移除   
    * sismember(key,value)  检查元素value是否存在于集合key中
    * smembers(key)     返回集合包含的所有元素
* SortedSet
    * zadd(key,score,value)    将一个带有给定分值的成员添加到有序集合里面
    * zrange(key,begin,end)    根据元素在有序排列中所处的位置，从有序集合中取出多个元素
    * zrangebyscore(key,beginscore,endscore)    获取有序集合在给定分值范围内的所有元素
    * zrem(key,value)   如果给定成员存在于有序集合中，那么移除这个成员
* Pub/Sub
    * publish(channel,value) 发布消息
    * subsribe(channel) 订阅消息
    * unsubsribe(channel)  取消订阅
* BloomFilter
* HyperLogLog
    * 极小空间完成独立数量的统计，本质是字符串类型
    * pfdadd（key,value）添加数据
    * pfcount(key) 统计数量
    * pfmerge(key1,key2) 合并
* Geo
    * 存储经纬度，计算两地距离，范围计算等
    * geoadd（key，经度，纬度，cityName） 添加坐标 
    * geopos（key，cityName）   获取地理位置
    * geodist（key，cityName1，cityName2，km）两地间的距离
* redisModule
    * redis 4.0后支持module功能之后，可以以插件的形式给redis扩展一些新的功能，比如本篇说到的rediSQL，rebloom。
    * https://www.cnblogs.com/huangxincheng/p/10292303.html

## redis数据持久化
* 参考文档：https://mp.weixin.qq.com/s/cOK6IRQkavnV8EUhppfgug   
* AOF:先把命令追加到操作日志的尾部，保存所有的历史操作
    * AOF默认不开启，需要redis.config 配置 appendonly yes
    * 触发机制
        * aways：每次发生变立即记录到磁盘，需要redis.config 配置 appendosync always
        * everysec：每秒同步一次，需要redis.config 配置 appendosync everysec
     * 重写机制
       * 手动触发：直接调用bgrewriteaof命令。
       * 自动触发：根据auto-aof-rewrite-min-size和auto-aof-rewrite-percentage参数确定自动触发时机。
        ````
        auto-aof-rewrite-min-size：表示运行AOF重写时文件最小体积，默认为64MB。
        auto-aof-rewrite-percentage（设为x）：代表当前AOF文件空间（aof_current_size）和上一次重写后AOF文件空间（aof_base_size）的百分比（如auto-aof-rewrite-percentage 100 当为两倍大小是重写）。
        ```` 
       * AOF重写功能不希望阻塞主进程的执行，所以redis把AOF重写放到一个子进程去执行,子进程只能看到fork那一瞬间产生的镜像数据。redis设置了一个 AOF重写缓存区（aof_rewrite_bug） 用来存储AOF重写期间产生的命令，等子进程重写完成后通知父进程，父进程把重写缓存区的数据追加到新的AOF文件(注：这里值得注意，AOF重写期间如果有大量的写入，父进程在把aof_rewrite_buf写到新的aof文件时会造成大量的写盘操作，会造成性能的下降，redis 4.0以后增加管道机制来优化这里（把aof_rewrite_buf追加工作交给子进程去做) 
* RDB:RDB持久化是指在指定的时间间隔内将内存中的数据集快照写入磁盘，实际操作过程是fork一个子进程，先将数据集写入临时文件，写入成功后，再替换之前的文件
    * RDB默认开启
    * 持久化时机
        * save命令：通过阻塞当前redis服务直到RDB完成为止，生产一般不会使用
        * bgsave命令：命令会在后台fork一个与redis主线程一模一样的子线程，由子线程完成数据持久化
        * 配置：redis.config 配置 save 900 1 表示900秒内有1一个key发生变化则进行持久化保存数据
* 混合持久化
    * 4.0版本的混合持久化功能 默认关闭，我们可以通过 aof-use-rdb-preamble配置参数控制该功能的启用。5.0 版本 默认开启。
    * 持久化时机
        * DB-AOF混合持久化体现在aofrewrite时，即在AOF重写时把fork的镜像写成RDB，后续AOF重写缓冲区里的数据继续追加到aof文件中。
        * 混合持久化开启，同时配置RDB的save参数,Redis会生成rdb文件和AOF文件，而不是省略RDB文件。

##redis的高可用高并发
* 参考文档：https://mp.weixin.qq.com/s/cOK6IRQkavnV8EUhppfgug
* 主从同步 
    * 数据同步持久化方式
    ````
    1.slave启动后会向master发送SYNC命令，master节点收到从数据库的命令后通过bgsave保存快照（「RDB持久化」），并且期间的执行的些命令会被缓存起来。
    2.然后master会将保存的快照发送给slave，并且继续缓存期间的写命令。
    3.slave收到主数据库发送过来的快照就会加载到自己的数据库中。
    4.最后master讲缓存的命令同步给slave，slave收到命令后执行一遍，这样master与slave数据就保持一致了。
    ````       
    * 同步失败或延迟会造成主从数据不一致的情况
    ````
    数据库主库和从库不一致，常见有这么几种优化方案：
    （1）业务可以接受，系统不优化
    （2）强制读主，高可用主库，用缓存提高读性能
    （3）在cache里记录哪些记录发生过写请求，来路由读主还是读从
    ````
* 哨兵模式
    * 「监控」：监控master和slave是否正常运行，以及哨兵之间也会相互监控,「自动故障恢复」：当master出现故障的时候，会自动选举一个slave作为master顶上去。        
    * 哨兵解决和主从不能自动故障恢复的问题，但是同时也存在难以扩容以及单机存储、读写能力受限的问题，并且集群之前都是一台redis都是全量的数据，这样所有的redis都冗余一份，就会大大消耗内存空间。
* 集群模式
    * 参考文档
        * https://www.jianshu.com/p/ac9bc1bb5337
        * https://blog.csdn.net/men_wen/article/details/72896682
        * https://mp.weixin.qq.com/s/cOK6IRQkavnV8EUhppfgug
    * 集群模式实现了Redis数据的分布式存储，实现数据的分片，每个redis节点存储不同的内容，并且解决了在线的节点收缩（下线）和扩容（上线）问题。    
##redis的缓存淘汰机制
* https://www.jianshu.com/p/857282187164
* Redis的所有数据都存储在内存中，但是内存是一种有限的资源,当Redis使用的内存超过配置的 maxmemory 时，便会触发数据淘汰策略。Redis提供了多种数据淘汰的策略，如下：
  
````
volatile-lru: 最近最少使用算法，从设置了过期时间的键中选择空转时间最长的键值对清除掉
volatile-lfu: 最近最不经常使用算法，从设置了过期时间的键中选择某段时间之内使用频次最小的键值对清除掉
volatile-ttl: 从设置了过期时间的键中选择过期时间最早的键值对清除
volatile-random: 从设置了过期时间的键中，随机选择键进行清除
allkeys-lru: 最近最少使用算法，从所有的键中选择空转时间最长的键值对清除
allkeys-lfu: 最近最不经常使用算法，从所有的键中选择某段时间之内使用频次最少的键值对清除
allkeys-random: 所有的键中，随机选择键进行删除
noeviction: 不做任何的清理工作，在redis的内存超过限制之后，所有的写入操作都会返回错误；但是读操作都能正常的进行
````
* 可以在启动Redis时，通过配置项maxmemory_policy来指定要使用的数据淘汰策略。例如要使用volatile-lru策略可以通过以下配置来指定：maxmemory_policy volatile-lru
