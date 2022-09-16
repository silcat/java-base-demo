#shardingsphere与seata的简单介绍
## shardingsphere
* https://shardingsphere.apache.org/document/legacy/4.x/document/cn/overview/
* 简介
    * 定位为轻量级Java框架，在Java的JDBC层提供的额外服务。 它使用客户端直连数据库，以jar包形式提供服务，无需额外部署和依赖，可完全兼容HikariCP，mybaits和mysql
    * 提供了分库分表，读写分离，分布式事务治理（实现了XA,TCC和和集成了seata）等功能
* 基本概念
    * 逻辑表：user对应dao里tableName
    * 真实表：逻辑表拆分的分表，存在数据库中真实表名，如user_0,user_1
    * 数据节点:库名.真实表名:ms0.user_0,ms0.user_1
    * 分片键：用于分片的数据库字段，如user表中uid就是user的分片键
    * 分片算法：数据库根据分片键拆分表的规则：如分两个表则  uid % 2 为分片算法
        * https://blog.csdn.net/womenyiqilalala/article/details/106113983
        * NoneShardingStrategy 不分片策略
        * inline 行表达式分片策略：select * from user where uid =2；
        ````
         tableStrategy:
            inline:
            shardingColumn: uid //分表键
            algorithmInlineExpression: user_${uid % 2}//分表算法
          行表达式语法
          
          Ø ${begin..end}表示范围区间
          Ø ${[unit1, unit2, unit_x]}表示枚举值
        ````  
        * standard 标准分片策略:对SQL语句中的=, IN和BETWEEN AND的分片操作支持
         ````   
          StandardShardingStrategy只支持单分片键，提供PreciseShardingAlgorithm和RangeShardingAlgorithm两个分片算法。
          PreciseShardingAlgorithm是必选的，用于处理=和IN的分片。
          RangeShardingAlgorithm是可选的，用于处理BETWEEN AND分片，如果不配置RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理。
          tableStrategy:
            standard:
            shardingColumn: uid
            preciseShardingAlgorithm: # preciseShardingAlgorithm接口的实现类
            rangeShardingAlgorithm: # rangeShardingAlgorithm接口的实现类
        ```` 
        * complex 复合分片策略，同standard，不过它支持多分片建
        * hint分片策略 
    * 绑定表：若两个表的分片键相同，这两个表可以作为绑定表：如user，user_pid可以作为绑定表
    
* 自带UUID和雪花ID主键生成策略，支持自定义分布式主键生成
# seata
## 简介
* https://mp.weixin.qq.com/s?__biz=MzUzNzYxNjAzMg==&mid=2247498350&idx=1&sn=8fd6a7a0c306c566c790f9851310d7df&chksm=fae6f1a1cd9178b74be60eb4ecb1d0d56801129296a183a9be6bd6990bfebff3c19363d65165&scene=132#wechat_redirect
* Seata 是一款开源的分布式事务解决方案，star 高达 18100+，社区活跃度极高，致力于在微服务架构下提供高性能和简单易用的分布式事务服务。
* 对于seata的使用而言，使用非常简单，对于AT模式来说，只要加一个注解@GlobalTransactional就能实现分布式事务
* 支持Eureka等注册中心， Appolo等配置中心，支持spring-cloud框架
##基本概念
* TC-事务协调者 : 维护全局和分支事务的状态，驱动全局事务提交或回滚，为一个单独服务器
* TM-事务管理器 : 定义全局事务的范围：开始全局事务、提交或回滚全局事务，申请 XID，同java服务一起部署
* RM-资源管理器 : 管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚(生成undo_log)，，同java服务一起部署
* Transaction ID XID : 全局唯一的ID，保存在当前线程的RootContext，并在微服务中传播
* AT模式：https://mp.weixin.qq.com/s?__biz=MzUzNzYxNjAzMg==&mid=2247498350&idx=1&sn=8fd6a7a0c306c566c790f9851310d7df&chksm=fae6f1a1cd9178b74be60eb4ecb1d0d56801129296a183a9be6bd6990bfebff3c19363d65165&scene=132#wechat_redirect

        

