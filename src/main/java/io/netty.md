#参考文档
* https://www.jianshu.com/p/3b7ffc809eca
* https://blog.csdn.net/tangtong1/article/details/86570333
* https://mp.weixin.qq.com/s/Ru7QrCFPFSLUB4FNxZo8zQ?st=520E6F3AE0EDF7739AF0BCDBDF0CCDE9361717E7A5FDCD6A7340CC0ADFD78AE6FE58BEA5204A7E7F879E90EEDCB5E2049B6A11A2332B92D0852AFEA1C271A416AE2A470DF1E5AD94FAA5086FBBD71D45142E5AA395581F821DBA79CCE6AED873C1239185ED395591780D573449DE9B3AE21CCACB558F77DF4DE783586D00A9F39085CF92A366912A7CA60F34063CCF7F74E451321B99954F2C2026206F51D7635C5EDF5EE18A1C0603D6CC53B9F78408&vid=1688852945251408&cst=69B9FA90F17C98445B009F0BF5E93E37A2A076F130E3AC3CD2989A53847F3FBD84D326D747690D7DABD9B9D2BB01F5CF&deviceid=613fb980-925a-4260-9024-2a14c5f1bc83&version=3.1.2.2211&platform=win
# 网络编程概念
##网路协议
* 数据在网络中是以字节（二进制）的形式传输的，要让通信的双方都能正确的接收对方发送的信息，一定要定义好数据格式，要让对方知道如何解析，从哪一位开始到哪一位代表什么含义，数据的总长度是多少，以避免多读或少读发生解析错误和粘包等问题。
* 序列化工具：将对象序列化为字节传输及反序列化
    * fastjson
    * protobuf
    * protostuff
    * kryo
* 自定义协议
    * https://blog.csdn.net/mz4138/article/details/109277563
    * magicCode:magicCode一般是指硬写到代码里的整数常量，数值是编程者自己指定的,表示包头的起始点
    * ProtocolVer:版本号
    * Full length：总长度
    * MsgType：消息类型
    * Serializer：序列化方式，参考序列化工具
    * Compress：是否压缩
    * RequestId：请求唯一编号
    * body：请求内容  
    ````
    0     1     2     3     4     5     6     7     8     9    10     11    12    13    14   
    +-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+
    |   magic   |Proto |     Full length      | Msg |Seria|Compr|     RequestId         |
    |   code    |colVer|    (head+body)       |Type |lizer|ess  |                       |   header
    +-----------+-----------+-----------+-----------+-----------+-----------+-----------+                                                                                             |
    |                                        body                                       |
    |
    +-----------------------------------------------------------------------------------------------+
    ````
## 拆包与粘包
# netty基本概念及流程
* EventLoop：绑定了selector，channel的IO线程
* EventLoopGroup：工作线程组
# netty APi
## byteBuffer
* https://blog.csdn.net/qq_23536449/article/details/105352788
* https://segmentfault.com/a/1190000014938586?utm_source=sf-similar-article
````                 
    -----------------  -----------------  -----------------
    | 可丢弃（已读） |   可读字节        |    可写字节     |
    -----------------  -----------------  -----------------
                    ↓                   ↓                ↓
                    readerIndex          writeIndex        capacity
````
* readerIndex() 与 readerIndex(int)
    * 前者表示返回当前的读指针 readerIndex, 后者表示设置读指针
* writeIndex() 与 writeIndex(int)    
    * 前者表示返回当前的写指针 writerIndex, 后者表示设置写指针
* writableBytes()：capacity - writeIndex
* readbleBytes()：writeIndex - readerIndex
* readInt()：读取数据并且readerIndex增加4
* readBytes(byte[] dst): 将数据读取到数组且readerIndex增加length
* writeInt(int)：写数据并且writerIndex增加4
* writeBytes(byte[] dst): 将数据写到数组且writerIndex增加length
* retain()：引用加1  
* releas()：引用减一
* duplicate()：复制当前对象，复制后的对象与前对象共享缓冲区，它具有自己的读索引、写索引和标记索引（初始化与原对象一致该值）
* copy()：复制一份全新的对象，内容和缓冲区都不是共享的
* slice(int, int)：获取子缓存区ByteBuf实例，复制后的对象与前对象共享缓冲区，它具有自己的读索引、写索引和标记索引（初始化该值）
* clear():将readerIndex和writeIndex都设置为0，但是不会清楚内存中的内容
##pipeline 与 channelHandler
```` 
    pipeline
|---------------------------------------------------------
|    content                                              |
|     ------------------------                            |
|     |   -----------------  | <----|                     |
|     |  | channelHandler  | | ---->| 略                  |
|     |   -----------------  |      |                     |
|     ------------------------                            |      
---------------------------------------------------------     
       
````
* ChannelHandler 
    * channelHandler的生命周期
        * handlerAdded() -> channelRegistered() -> channelActive() -> channelRead() -> channelReadComplete() -> channelInactive() -> channelUnregistered() -> handlerRemoved()
          ````
          handlerAdded() ：指的是当检测到新连接之后，调用 ch.pipeline().addLast(new LifeCyCleTestHandler()); 之后的回调，表示在当前的 channel 中，已经成功添加了一个 handler 处理器。
          channelRegistered()：这个回调方法，表示当前的 channel 的所有的逻辑处理已经和某个 NIO 线程建立了绑定关系，accept 到新的连接，然后创建一个线程来处理这条连接的读写，只不过 Netty 里面是使用了线程池的方式，只需要从线程池里面去抓一个线程绑定在这个 channel 上即可，这里的 NIO 线程通常指的是 NioEventLoop,不理解没关系，后面我们还会讲到。
          channelActive()：当 channel 的所有的业务逻辑链准备完毕（也就是说 channel 的 pipeline 中已经添加完所有的 handler）以及绑定好一个 NIO 线程之后，这条连接算是真正激活了，接下来就会回调到此方法。
          channelRead()：客户端向服务端发来数据，每次都会回调此方法，表示有数据可读。
          channelReadComplete()：服务端每次读完一次完整的数据之后，回调该方法，表示数据读取完毕。
          channelInactive(): 表面这条连接已经被关闭了，这条连接在 TCP 层面已经不再是 ESTABLISH 状态了
          channelUnregistered(): 既然连接已经被关闭，那么与这条连接绑定的线程就不需要对这条连接负责了，这个回调就表明与这条连接对应的 NIO 线程移除掉对这条连接的处理
          handlerRemoved()：最后，我们给这条连接上添加的所有的业务逻辑处理器都给移除掉。
          ````
    * ChannelInboundHandler:入栈处理器，顶级接口
        * 解码
            * 提供String，json，xml，Protobuf等协议的解码器
            * ByteToMessageDecoder
            * MessageToMessageDecoder
            * ReplayingDecoder
                * http://note.jues.org.cn/node/100：解决粘包和半包
                * 当前ByteBuf中数据小于代取数据，等待数据满足，才能取数据，可以认为集成这个类读数据一定可以成功 
            * Netty自带拆包器
                * FixedLengthFrameDecoder
                * LineBasedFrameDecoder
                * DelimiterBasedFrameDecoder
                * LengthFieldBasedFrameDecoder
                    * https://www.jianshu.com/p/a0a51fd79f62
        * 事件处理
            * ChannelInboundHandlerAdapter
            * SimpleChannelInboundHandler  
    * ChannelOutBoundHandler：出站处理器,顶级接口
        * 编码
            * 提供String，json，xml，Protobuf等协议的编码器
            * ByteToMessageEncoder
            * MessageToMessageEncoder
            * LengthFieldPrepender
        * ChannelOutboundHandlerAdapter
# 实现简易的RPC框架
* https://blog.csdn.net/dawei0523/article/details/84742090