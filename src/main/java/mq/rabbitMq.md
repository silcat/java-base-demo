#参考文档
* https://zhuanlan.zhihu.com/p/48779080
#简介

#组件
##producer： 
* producer 是一个发送消息的应用
##exchange：
* producer 并不会直接将消息发送到 queue 上，而是将消息发送给 exchange，由 exchange 按照一定规则转发给指定queue
* Fanout Exchange：忽略key对比，发送Message到Exchange下游绑定的所有Queue
* Direct Exchange：比较Message的routing key和Queue的binding key，完全匹配时，Message才会发送到该Queue
* Topic Exchange：比较Message的routing key和Queue的binding key，按规则匹配成功时，Message才会发送到该Queue
* 默认Exchange：比较Message的routing key和Queue的名字，完全匹配时，Message才会发送到该Queue
##queue： 
* queue 用来存储 producer 发送的消息
## consumer： 
* consumer是接收并处理消息的应用
##Producers消息发送流程
   * 参考：https://blog.csdn.net/qq_32727095/article/details/108032582?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_v2~rank_aggregation-2-108032582.pc_agg_rank_aggregation&utm_term=kafka%20%E5%90%8C%E6%AD%A5%E6%8F%90%E4%BA%A4%20%E5%BC%82%E6%AD%A5%E6%8F%90%E4%BA%A4%20%E6%89%8B%E5%8A%A8&spm=1000.2123.3001.4430
   * 流程：
       * 多个Queue的场景中，消息会被Exchange按一定的路由规则分发到指定的Queue中去：
       * 生产者指定Message的routing key，并指定Message发送到哪个Exchange
       * Queue会通过binding key绑定到指定的Exchange
       * Exchange根据对比Message的routing key和Queue的binding key，然后按一定的分发路由规则，决定Message发送到哪个Queue
