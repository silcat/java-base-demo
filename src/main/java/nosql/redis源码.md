##数据结构
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