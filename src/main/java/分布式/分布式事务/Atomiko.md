#简介
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

        

