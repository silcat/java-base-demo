#架构
* ![](img/rpc.png)
* 注册中心
    * 可用性：集群
        * AP：euraka，consul
        * CP：zookeeper   
* 服务端server
   * 负载均衡
   * 网络传输
        * 通信框架:netty
        * 序列化/反序列化协议
        * TCP/UDP
* 客户端
  * 动态代理:客户端调用服务端接口就行调用本地方法一样
  * 网络传输
        * 通信框架:netty
        * 序列化/反序列化协议
        * TCP/UDP
* 拓展功能
    * 降级/熔断
    * 监控/链路追踪
#通信方式
* netty
* socket
#序列化协议
##Json
* 适合网络传输或者对性能要求不高的场景
* {"demo":"json"}
##Kryo 
* 和hession比，更小更快，适合，序列化后结构为二进制数组
* dubbo推荐序列化方式
    * https://zhuanlan.zhihu.com/p/272816835
    * int= 7 ：序列后占用1个字节（正常是4个字节） 
* Kryo 不是线程安全。
    * https://juejin.cn/post/6993647089431347237
    * ThreadLocal + Kryo保证线程安全。 
    * 对象池 + Kryo 解决线程不安全
##Protobuf
* https://www.jianshu.com/p/cae40f8faf1e
* 序列化后结构为二进制
* 需要序列化的项目需要编写IDL文件并编译为class文件放入项目，麻烦
##hessian
* dubbo默认，跨语言，序列化后结构为二进制数组
#自定义通信协议
````

 *   0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 *   |   magic   code        |version | full length         | messageType| codec|compress|    RequestId       |
 *   +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
 *   |                                                                                                       |
 *   |                                         body                                                          |
 *   |                                                                                                       |
 *   |                                        ... ...                                                        |
 *   +-------------------------------------------------------------------------------------------------------+
 * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
````





