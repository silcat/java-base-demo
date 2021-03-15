##数据结构
* https://redisbook.readthedocs.io/en/latest/compress-datastruct/intset.html#id2
* sds
    * 数据结构
    ````
    struct sdshdr {
    
        // buf 已占用长度
        int len;
    
        // buf 剩余可用长度
        int free;
    
        // 实际保存字符串数据的地方
        char buf[];
    };
    ````
    * sds可以实现复杂度为 θ(1) 的长度计算操作：int len字段
    * sdshdr 可以高效地执行追加操作,且所需的内存重分配次数大大减少：free字段
        *
            ````
            def sdsMakeRoomFor(sdshdr, required_len):
            
                # 预分配空间足够，无须再进行空间分配
                if (sdshdr.free >= required_len):
                    return sdshdr
            
                # 计算新字符串的总长度
                newlen = sdshdr.len + required_len
            
                # 如果新字符串的总长度小于 SDS_MAX_PREALLOC:值为 1024 * 1024byte 
                # 那么为字符串分配 2 倍于所需长度的空间
                # 否则就分配所需长度加上 SDS_MAX_PREALLOC 数量的空间
                if newlen < SDS_MAX_PREALLOC:
                    newlen *= 2
                else:
                    newlen += SDS_MAX_PREALLOC
            
                # 分配内存
                newsh = zrelloc(sdshdr, sizeof(struct sdshdr)+newlen+1)
            
                # 更新 free 属性
                newsh.free = newlen - sdshdr.len
            
                # 返回
                return newsh
           ````
* 双端链表
    * 数据结构
    ````
    listNode {
        // 前驱节点
        struct listNode *prev;
        // 后继节点
        struct listNode *next;  
        // 值
        void *value;
    
    } listNode;
    struct list { 
        // 表头指针
        listNode *head;
        // 表尾指针
        listNode *tail;    
        // 节点数量
        unsigned long len;
        // 复制函数
        void *(*dup)(void *ptr);
        // 释放函数
        void (*free)(void *ptr);
        // 比对函数
        int (*match)(void *ptr, void *key);
    } list;
    ````
   * 特点
        * o(1)进行lpush，返回length操作
* 字典
    * 数据结构
    ````
     struct dict {
        // 特定于类型的处理函数
        dictType *type;
        // 类型处理函数的私有数据
        void *privdata;
        // 哈希表（2 个）
        dictht ht[2];
        // 记录 rehash 进度的标志，值为 -1 表示 rehash 未进行
        int rehashidx;
        // 当前正在运作的安全迭代器数量
        int iterators;
    } dict;
    typedef struct dictht {
    
        // 哈希表节点指针数组（俗称桶，bucket）
        dictEntry **table;
        // 指针数组的大小
        unsigned long size;
        // 指针数组的长度掩码，用于计算索引值
        unsigned long sizemask;
        // 哈希表现有的节点数量
        unsigned long used;
    
    } dictht
    ````
    * 流程：是否存在->是否碰撞->是否rehash
    * 渐进式rehash
        * 通过将 rehash 分散到多个步骤中进行， 从而避免了集中式的计算
        * 每次执行一次添加、查找、删除操作， ， ht[0]->table 哈希表第一个不为空的索引上的所有节点就会全部迁移到 ht[1]->table
        * 当 Redis 的服务器常规任务执行时 在规定的时间内， 尽可能地对数据库字典中那些需要 rehash 的字典进行 rehash ， 从而加速数据库字典的 rehash 进程（progress）。
        * rehash 时，字典会同时使用两个哈希表，所以在这期间的所有查找、删除等操作，除了在 ht[0] 上进行，还需要在 ht[1] 上进行。
        * 在执行添加操作时，新的节点会直接添加到 ht[1] 而不是 ht[0] ，这样保证 ht[0] 的节点数量在整个 rehash 过程中都只减不增
 * 跳跃表
    * https://www.cnblogs.com/hunternet/p/11248192.html
    * https://www.jianshu.com/p/c2841d65df4c
    * 数据结构
    ````
    typedef struct zskiplist {
        // 头节点，尾节点
        struct zskiplistNode *header, *tail;
        // 节点数量
        unsigned long length;
        // 目前表内节点的最大层数
        int level;
    } zskiplist;
    typedef struct zskiplistNode {
        // member 对象
        robj *obj;  
        // 分值
        double score; 
        // 后退指针
        struct zskiplistNode *backward;   
        // 层
        struct zskiplistLevel {    
            // 前进指针
            struct zskiplistNode *forward;   
            // 这个层跨越的节点数量
            unsigned int span;   
        } level[];
    
    } zskiplistNode;
    /**
     * 有序集合结构体
     */
    typedef struct zset {
        /*
         * Redis 会将跳跃表中所有的元素和分值组成 
         * key-value 的形式保存在字典中
         * todo：注意：该字典并不是 Redis DB 中的字典，只属于有序集合
         */
        dict *dict;
        /*
         * 底层指向的跳跃表的指针
         */
        zskiplist *zsl;
    } zset;
    ````       
  * intset
    * 数据结构
    ````
    typedef struct intset {
        // 保存元素所使用的类型的长度:16位和32位
        uint32_t encoding;   
        // 元素个数
        uint32_t length;
        // 保存元素的数组：元素是从小到大排列的
        int8_t contents[]; 
    } intset
    ````
 * ziplist
    * 数据结构
    ````
    area        |<---- ziplist header ---->|<----------- entries ------------->|<-end->|
    
    size          4 bytes  4 bytes  2 bytes    ?        ?        ?        ?     1 byte
                +---------+--------+-------+--------+--------+--------+--------+-------+
    component   | zlbytes | zltail | zllen | entry1 | entry2 |  ...   | entryN | zlend |
                +---------+--------+-------+--------+--------+--------+--------+-------+
                                           ^                          ^        ^
    address                                |                          |        |
                                    ZIPLIST_ENTRY_HEAD                |   ZIPLIST_ENTRY_END
                                                                      |
                                                             ZIPLIST_ENTRY_TAIL
    area        |<------------------- entry -------------------->|
    
                +------------------+----------+--------+---------+
    component   | pre_entry_length | encoding | length | content |
                +------------------+----------+--------+---------+                                                         
    ````
    * ziplist采用了一段连续的内存来存储数据,相比hashtable减少了内存碎片和指针的内存占用
#redis数据类型
* redisObject 是 Redis 类型系统的核心， 数据库中的每个键、值，以及 Redis 本身处理的参数， 都表示为这种数据类型。
* 数据结构
    ````
    /*
     * Redis 对象
     */
    typedef struct redisObject {
        // 类型
        unsigned type:4;
        // 对齐位
        unsigned notused:2;
        // 编码方式
        unsigned encoding:4;
        // LRU 时间（相对于 server.lruclock）
        unsigned lru:22;
        // 引用计数
        int refcount;
        // 指向对象的值
        void *ptr;
    } robj;
    ````
* 使用数据结构由 type 属性和 encoding 属性决定 ，ptr指定存储数据结构
    * [如何决定使用何种数据结构存储数据](https://redisbook.readthedocs.io/en/latest/_images/graphviz-243b3a1747269b8e966a9bdd9db2129d983f2b23.svg)
* BLPOP 、 BRPOP 和 BRPOPLPUSH 这个几个阻塞命令的实现原理上
    * 若阻塞则生成blockkey（字典）：key为阻塞键，value为客户端
    * 若有新数据，则生成readyKey（list）,查找阻塞的客户端并通知
        ````
        typedef struct readyList {
            redisDb *db;
            robj *key;
        } readyList;
       ````
* 事务的实现
* https://redisbook.readthedocs.io/en/latest/feature/transaction.html#id2    
* 开启事务时，除了 EXEC 、 DISCARD 、 MULTI 和 WATCH命令，会将其他命令全部放进一个事务队列（数组）而不是立即执行
* 当EXEC 命令执行时，服务器根据客户端所保存的事务队列， 以先进先出（FIFO）的方式执行事务队列中的命令
* 使用watched，会保存在 watched_keys （字典）：key为键，value为客户端

# redis数据库的结果
* 数据结构
````
typedef struct redisDb {

    // 保存着数据库以整数表示的号码
    int id;
    // 保存着数据库中的所有键值对数据
    // 这个属性也被称为键空间（key space）
    dict *dict;
    // 保存着键的过期信息(字典：key为键，value为过期时间)
    dict *expires;
    // 实现列表阻塞原语，如 BLPOP
    // 在列表类型一章有详细的讨论
    dict *blocking_keys;
    dict *ready_keys;
    // 用于实现 WATCH 命令
    // 在事务章节有详细的讨论
    dict *watched_keys;

} redisDb;
````
* 过期键的清除
    * 定时删除：在设置键的过期时间时，创建一个定时事件，当过期时间到达时，由事件处理器自动执行键的删除操作。
        * 很耗费性能，redis不采用，采用以下方式
    * 惰性删除：放任键过期不管，但是在每次从 dict 字典中取出键值时，要检查键是否过期，如果过期的话，就删除它，并返回空；如果没过期，就返回键值。
    * 定期删除：每隔一段时间，对 expires 字典进行检查，删除里面的过期键
        * 规定的时间限制内， 尽可能地遍历各个数据库的 expires 字典， 随机地检查一部分键的过期时间， 并删除其中的过期键。
* 过期键对 AOF 、RDB 和复制的影响
    * 在创建新的 RDB 文件时，程序会对键进行检查，过期的键不会被写入到更新后的 RDB 文件中。因此，过期键对更新后的 RDB 文件没有影响。        
    * 更新AOf文件时， 键已经过期，但是还没有被惰性删除或者定期删除之前，这个键不会产生任何影响，AOF 文件也不会因为这个键而被修改。当过期键被惰性删除、或者定期删除之后，程序会向 AOF 文件追加一条 DEL 命令，来显式地记录该键已被删除。 
    * 和 RDB 文件类似， 当进行 AOF 重写时， 程序会对键进行检查， 过期的键不会被保存到重写后的 AOF 文件。
    * 当服务器带有附属节点时， 过期键的删除由主节点统一控制
        ````
        如果服务器是主节点，那么它在删除一个过期键之后，会显式地向所有附属节点发送一个 DEL 命令。
        如果服务器是附属节点，那么当它碰到一个过期键的时候，它会向程序返回键已过期的回复，但并不真正的删除过期键。因为程序只根据键是否已经过期、而不是键是否已经被删除来决定执行流程，所以这种处理并不影响命令的正确执行结果。当接到从主节点发来的 DEL 命令之后，附属节点才会真正的将过期键删除掉。
        附属节点不自主对键进行删除是为了和主节点的数据保持绝对一致， 因为这个原因， 当一个过期键还存在于主节点时，这个键在所有附属节点的副本也不会被删除。
        这种处理机制对那些使用大量附属节点，并且带有大量过期键的应用来说，可能会造成一部分内存不能立即被释放，但是，因为过期键通常很快会被主节点发现并删除，所以这实际上也算不上什么大问题。
       ````     
# RDB
* RDB 将数据库的快照（snapshot）以二进制的方式保存到磁盘中。
* rdbSave 会将数据库数据保存到 RDB 文件，并在保存完成之前阻塞调用者。
* SAVE 命令直接调用 rdbSave ，阻塞 Redis 主进程； BGSAVE 用子进程调用 rdbSave ，主进程仍可继续处理命令请求。
* SAVE 执行期间， AOF 写入可以在后台线程进行， BGREWRITEAOF 可以在子进程进行，所以这三种操作可以同时进行。
* 为了避免产生竞争条件， BGSAVE 执行时， SAVE 命令不能执行。
* 为了避免性能问题， BGSAVE 和 BGREWRITEAOF 不能同时执行。
* 调用 rdbLoad 函数载入 RDB 文件时，不能进行任何和数据库相关的操作，不过订阅与发布方面的命令可以正常执行，因为它们和数据库不相关联。
* RDB文件结构
    ````
    +-------+-------------+-----------+-----------------+-----+-----------+
    | REDIS | RDB-VERSION | SELECT-DB | KEY-VALUE-PAIRS | EOF | CHECK-SUM |
    +-------+-------------+-----------+-----------------+-----+-----------+
                          |<-------- DB-DATA ---------->|
              +----------------------+---------------+-----+-------+
              | OPTIONAL-EXPIRE-TIME | TYPE-OF-VALUE | KEY | VALUE |
              +----------------------+---------------+-----+-------+
                |<-------- KEY-VALUE-PAIRS ---------->|
    ````    
    * TYPE-OF-VALUE 对应以上7中数据结构，表示value的格式