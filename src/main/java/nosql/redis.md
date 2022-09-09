##为什么使用缓存
* 高性能:非实时变化的数据-查询mysql耗时需要300ms,存到缓存redis，每次查询仅仅1ms,性能瞬间提升百倍。    
* 高并发:mysql 单机支撑到2K QPS就容易报警了，但是使用缓存的话，单机支撑的并发量轻松1s几万~十几万。原因是缓存位于内存，内存对高并发的良好支持。
* 现有硬件系统难以处理的高性能高并发都可以用缓存来优化
##缓存的使用方式
* 本地缓存：
    * 局部变量map结构缓存部分业务数据。缺点仅限于类的自身作用域内，类间无法共享缓存
    * 静态变量map一次获取缓存内存中，静态变量实现类间可共享，进程内可共享，缓存的实时性稍差
        * 结合ZooKeeper的自动发现机制，实时变更本地静态变量缓存
        * https://www.jianshu.com/p/40cdd4bfe183
    * Ehcache纯Java的进程内缓存框架
        * https://www.cnblogs.com/liululee/p/13354481.html
    * spring注解
* 分布式缓存 
    * memcached缓存
        * MemCache虽然被称为"分布式缓存"，但是MemCache本身完全不具备分布式的功能，MemCache集群之间不会相互通信，所谓的"分布式"，完全依赖于客户端程序的实现
        * MemCache集群的方式也是从分区容错性的方面考虑的，假如Node1宕机了，此时由于集群中其他节点数据还存在。
    * redis缓存 
* Spring注解缓存
    * 参考文档
        * https://developer.ibm.com/zh/articles/os-cn-spring-cache/
        * https://www.xuebuyuan.com/2174369.html
#为什么要用redis
* Redis 支持更丰富的数据类型，持久化，容灾，集群，更快，事务，lua脚本，多种过期策略等
* redis除了缓存还能实现分布式锁，限流，消息队列，排行榜等等
# redis为什么这么快
* ![](img/why-redis-so-fast.png)
* 基于内存:内存的访问速度是磁盘的上千倍；
* 非阻塞IO和 IO 多路复用的单线程模型:基于 Reactor 模式设计开发了一套高效的事件处理模型
* 数据结构简单:内置了多种优化过后的数据结构实现，查询和修改都是大部分都是o(1)。
* 避免上下文切换：单线程模型，无锁，无死锁
# 为什么说redis是单线程
* redise 基于 Reactor 模式，使用 I/O 多路复用程序用一个线程（bossGroup）也可以来监听多个套接字，文件事件处理器以单线程方式运行（workgroup线程池为1，而netty大于1）
* 单线程易于维护
* redis的性能瓶颈不在在于内存和带宽，而非CPU
* 确切的说redis的工作线程是单线程的
##单线程的缺点
* 使用高耗时的Redis命令是很危险的，会占用唯一的一个线程的大量处理时间，导致所有的请求都被拖慢
* 大key删除
* 其他如hgetall、lrange、smembers、zrange、sinte keys scan命令等o(n)的命令
##redis多线程
* 为了解决大key删除的问题，redis 4引用多线程解决惰性删除，工作线程依旧是单线程
* redis 6为了提高网络 IO 读写性能，网络数据的读写这类耗时操作上使用了多线程，工作线程仍然是单线程顺序执行。因此，你也不需要担心线程安全问题。
* redis 6默认关闭多线程的，如需开启配置redis.conf文件
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

* 可以在启动Redis时，通过配置项maxmemory_policy来指定要使用的数据淘汰策略。例如要使用volatile-lru策略可以通过以下配置来指定：maxmemory_policy volatile-lru
##redis 过期key的删除策略
* https://www.cnblogs.com/chenpingzhao/p/5022467.html?utm_source=tuicool&utm_medium=referral
* 被动删除：当读/写一个已经过期的key时，会触发惰性删除策略，直接删除掉这个过期key
* 主动删除：由于惰性删除策略无法保证冷数据被及时删掉，所以Redis会定期主动淘汰一批已过期的key
    * Redis 将 serverCron 确保它每隔一段时间就会自动运行一次 serverCron 会一直定期执行，直到服务器关闭为止。
    * 用户可以通过修改 hz选项来调整 serverCron 的每秒执行次数
    * 当REDIS运行在主从模式时，只有主结点才会执行上述这两种过期删除策略，然后把删除操作”del key”同步到从结点
* 当前已用内存超过maxmemory限定时，触发主动清理策略
   * https://www.jianshu.com/p/857282187164
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
* redis.config配置
````
hz 10 
maxmemory-policy volatile-lru 
maxmemory-samples 5
````  
* 删除过期key的数据一致性
     * 当一个key过期时DEL操作将被记录在AOF文件并传递到所有相关的slave。也即过期删除操作统一在master实例中进行并向下传递，而不是各salve各自掌控。这样一来便不会出现数据不一致的情形。
     * 当slave连接到master后并不能立即清理已过期的key（需要等待由master传递过来的DEL操作），slave仍需对数据集中的过期状态进行管理维护以便于在slave被提升为master会能像master一样独立的进行过期处理。
 ## redis优化策略
 * https://zhuanlan.zhihu.com/p/293042405
     