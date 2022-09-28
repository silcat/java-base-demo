#什么是socket
* Socket理解成是应用层与TCP/IP协议族通信的抽象层、函数库。
#流程
* ![](img/socket.png)
##socket流程
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
#BIO
* 客户端
   * 3次握手：new Socket（）->Socket.connect(address)->DualStackPlainSocketImpl.connect0(nativefd, address, port)
* 服务端
   * 3次握手：serverSocket.accept()—>DualStackPlainSocketImpl.accept0(nativefd, isaa);
* 其中accept0方法是阻塞的
* read和写操作也是阻塞
#NIO
* 包括 bytebuffer，channal，selector三个组件
* accept0方法是非阻塞阻塞的没有数据立即返回,读写依旧阻塞
* 每 accept 一个客户端连接后，将这个文件描述符（connfd）放到一个数组里
* 执行select0（）方法也是阻塞
   


