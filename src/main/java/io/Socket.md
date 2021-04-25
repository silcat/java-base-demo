#socket流程
````
Socket{
SocketImpl SocksSocketImpl      
}

            SocketImpl
                 |
        abstractPlainSocketImpl
         /                  \
       /                     \
  PlainSocketImpl {           \
    AbstractPlainSocketImpl  DualStackPlainSocketImpl/TwoStacksPlainSocketImpl;
  }     |
        |
  SocksSocketImpl
````
* Socket类是门面类将操作系统相关接口封装好暴露给用户使用，实际调用的方法是SocksSocketImpl类的方法
* PlainSocketImpl类初始化时会根据操作系统选择代理DualStackPlainSocketImpl或TwoStacksPlainSocketImpl实际执行;
* DualStackPlainSocketImpl 封装java 操作系统navicat接口如connect0(),accept0()等。
#redisTemplate 使用socket通信流程
````
redisTemplate{
    RedisConnectionFactory connectionFactor;
    excute(){
    Connect connect = connectionFactor.getConnect();
    connect.keys()
    }
}
RedisConnectionFactory
    |
JedisConnectionFactory{
    Config config;
    connect = getConnect(){
    //Jedis是对客户端对redis Socket请求的封装
    Jedis jedis = fetchJedisConnector();
    }
}
Jedis{
    Connection client；
    keys(){
    client.keys()
    }
}
Connection{ 
  sendCommand(cmd){
  //通过Socket关键字获取TCP连接
   Connection connect = new Socket().connect()
   Connection.write（cmd）
  }
}

````
* redisTemplate相当于门面模式，封装了对redis api，供客户端使用,通过连接工厂将客户端和redis 连接操作解耦
* 一个请求经过多次代理终于发送给redis
