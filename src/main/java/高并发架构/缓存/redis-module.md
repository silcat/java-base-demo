#为什么使用模块
* 如果想实现redis锁，用户只能通过客户端编写lua命令，
* 通过模块我们可以自定义redis命令（分布式锁），拓展redis命令
#下载并编译
* wget .. -> tar zxvf xxx.tar.gz -> make
#如何加载模块
* 在redis.conf 配置文件中指定
    * loadmodule /path/to/mymodule.so
* 运行时使用命令加载模块
    * MODULE LOAD /path/to/mymodule.so    cd
* 移除模块
    * UNLOAD mymodule
* 查看模块
    * MODULE LIST
    
# rebloom模块
* bloom过滤器    
#集成spring-boot
* 类似redisTemplate:设置BF命令
````
  new RedisTemplate<>().execute((RedisCallback<Object>) connection -> {
            JedisConnection connection1 = (JedisConnection) connection;
          return connection1.getJedis().sendCommand(Protocol.Command.BF,"");
        });
````