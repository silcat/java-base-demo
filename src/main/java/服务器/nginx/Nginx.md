#文档
* https://moonbingbing.gitbooks.io/openresty-best-practices/content/ngx/reverse_proxy.html
* http://dictionary.sharkchili.xyz/#/%E8%BF%90%E7%BB%B4/nginx%E5%9F%BA%E7%A1%80%E5%85%A5%E9%97%A8%E6%80%BB%E7%BB%93?id=%e5%8f%8d%e5%90%91%e4%bb%a3%e7%90%86%e7%a4%ba%e4%be%8b1
#nginx配置文件
##全局块
* 运行Nginx服务器的用户（组）、允许生成的worker process数、Nginx进程PID存放路径、日志的存放路径和类型以及配置文件引入等。
##event块
* 多worker process下的网络连接进行序列化，是否允许同时接收多个网络连接，选取哪种事件驱动模型处理连接请求，每个worker process可以同时支持的最大连接数等。
  
* 这一部分的指令对Nginx服务器的性能影响较大，在实际配置中应该根据实际情况灵活调整。
##http块
* http块是Nginx服务器配置中的重要部分，代理、缓存和日志定义等绝大多数的功能和第三方模块的配置都可以放在这个模块中。
###limit_conn模块(对connection做处限制)
* 该模块nginx会默认编译进去的，用于限制单位时间段内单客户连接并发数。使用的是共享内存，对所有worker子进程都生效。
###limit_req模块(对request处理速率做出限制)
* 用于限制特定网段、ip地址的访问
###access模块(限制特定ip或网段)
* 用于限制特定网段、ip地址的访问
###auth_basic模块(限制特定用户访问)
* 基于http authentication协议进行用户密码认证，默认已编译进nginx。
###rewrite模块
* 该指令会直接返回http响应码或者重定向到指定的url，return指令之后的语句都不会被执行。
* RETURN指令
    * return 404
    * return 302 /notFound; 响应302 并跳转到notFound这个映射
* REWRITE指令
    * 指定请求重定向到对应映射   
    * 语法 rewrite <regex> <replacement> [flag];
        * <regex>:正则表达式        
        * <replacement>:跳转后的内容     
        * [flag]rewrite支持的flag标记
            * last：url重写后，马上发起一个新请求，再次进入server块，重试location匹配，超过10次匹配不到报500错误，地址栏不变
            * break：url重写后，直接使用当前资源，不再使用location余下的语句，完成本次请求，地址栏不变 
    * 示例:rewrite ^/（.*） http：//www.ywx.org/$1 permanent；
        * 在上述指令中，rewrite为固定关键字，表示开启一条rewrite匹配规则
        * regex部分是^/（.*），这是一个正则表达式，表示匹配所有，
        * 匹配成功后跳转到http://www.ywx.org/$1。
        * 这里的$1是取前面regex部分括号里的内容。
        * 结尾的permanent；是永久301重定向标记，即跳转到后面的http://www.ywx.org/$1地址上。
###缓存模块
* 缓存到nginx服务器
* 合并请求，先缓存，其他锁住等待后获取缓存
* 获取陈旧缓存，当服务端宕机
###nginx高可用
* VRRP协议
   * 虚拟路由冗余协议，是IETF解决局域网内单台网关路由器故障导致网络故障的网络协议
    * 核心概念
        * 虚拟网关:由一个Master和多个Backup组成构成，逻辑上他们可以看作一个网关，统称为虚拟网关。
        * Master网关:虚拟网关中实际负责报文转发的路由器，局域网内目标为虚拟网关的报文通通会转发到这台路由器上。
        * 虚拟IP地址:虚拟网关对外提供服务的ip地址值，即Master和Backup路由器的共用的一个虚拟ip，虽然是共有，但是单位时间内只有Master得到这个值，因为Master才是虚拟网关真正工作的人。
        * 虚拟MAC地址:回应ARP请求时使用的虚拟MAC地址。
    * VRRP工作原理
        一个VRRP组(VRID相等的路由器称为一个VRRP组)可以有多台路由器，VRRP会在其中比较他们的优先级，若优先级相等则到ip绝对值最大的一台路由器作为Master，作为路由转发路由。 其余的路由器作为Backup路由，当Master不在对其发送VRRP消息时，他们就就会竞争Master资格 
* keepalived
    * 根据vrrp实现的nginx高可用组件
###全局变量
* tcp
* http
* 可以是$(tcp变量) 引用       
