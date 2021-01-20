# 参考文档
* https://bbs.huaweicloud.com/blogs/137174
# Sharding-JDBC提供的5种分片策略
##  none 不分片策略
````    
*  对应NoneShardingStrategy ，不分片策略 ,SQL会被发给所有节点去执行,这个规则没有子项目可以配置。
````   
##  inline 行表达式分片策略  必掌握
````  
  对应InlineShardingStrategy。使用Groovy的表达式，提供对SQL语句中的=和IN的分片操作支持，只支 持单分片键。对于简单的分片算法，可以通过简单的配置使用，从而避免繁琐的Java代码开发，如: t_user_$->{u_id % 8} 表示t_user表根据u_id模8，而分成8张表，表名称为t_user_0到t_user_7。
  
* databaseStrategy:
    inline:
    shardingColumn: user_id
    algorithmInlineExpression: ds${user_id % 2}
  行表达式语法
  
  Ø ${begin..end}表示范围区间
  Ø ${[unit1, unit2, unit_x]}表示枚举值
  Ø 行表达式中如果出现连续多个${ expression }或$->{ expression }表达式，整个表达式最终的结果将会根据每个子表达式的结果进行笛卡尔组合。
````  
##  standard 标准分片策略  需了解
 ````   
  Ø 对应StandardShardingStrategy。提供对SQL语句中的=, IN和BETWEEN AND的分片操作支持。
  Ø StandardShardingStrategy只支持单分片键，提供PreciseShardingAlgorithm和RangeShardingAlgorithm两个分片算法。
  Ø PreciseShardingAlgorithm是必选的，用于处理=和IN的分片。
  Ø RangeShardingAlgorithm是可选的，用于处理BETWEEN AND分片，如果不配置RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理。
  
  databaseStrategy:
    standard: #单列sharidng算法,需要配合对应的preciseShardingAlgorithm,rangeShardingAlgorithm接口的实现使用
    shardingColumn: # 列名,允许单列
    preciseShardingAlgorithm: # preciseShardingAlgorithm接口的实现类
    rangeShardingAlgorithm: # rangeShardingAlgorithm接口的实现类
````  
##  complex 复合分片策略 需了解
````    
  Ø 对应ComplexShardingStrategy。复合分片策略提供对SQL语句中的=, IN和BETWEEN AND的分片操作支持。
  Ø ComplexShardingStrategy支持多分片键，由于多分片键之间的关系复杂，因此并未进行过多的封装，而是直接将分片键值组合以及分片操作符透传至分片算法，完全由应用开发者实现，提供最大的灵活度。
````  
##  hint分片策略 需了解
````  
  Ø 对应HintShardingStrategy。通过Hint而非SQL解析的方式分片的策略。
  Ø 对于分片字段非SQL决定，而由其他外置条件决定的场景，可使用SQL Hint灵活的注入分片字段。例：内部系统，按照员工登录主键分库，而数据库中并无此字段。SQL Hint支持通过Java API和SQL注释(待实现)两种方式使用。
  
  databaseStrategy:
    hint: #基于标记的sharding分片
      shardingAlgorithm: # 需要是HintShardingAlgorithm接口的实现,目前代码中,有为测试目的实现的OrderDatabaseHintShardingAlgorithm可参考
  Sharding-JDBC配置默认数据源、分片策略
  可配置默认的数据源、数据源分片策略、表分片策略 需了解
````  
#Sharding-JDBC—分布式事务
* https://www.cnblogs.com/dalianpai/p/14001823.html

       
        

