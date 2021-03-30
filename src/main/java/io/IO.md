#参考文档
* https://segmentfault.com/a/1190000027078841
* https://mp.weixin.qq.com/s/YdIdoZ_yusVWza1PU7lWaw
* https://tech.meituan.com/2016/11/04/nio.html
* https://www.zhihu.com/question/56673416
* https://mp.weixin.qq.com/s?__biz=MjM5Njg5NDgwNA==&mid=2247484905&idx=1&sn=a74ed5d7551c4fb80a8abe057405ea5e&chksm=a6e304d291948dc4fd7fe32498daaae715adb5f84ec761c31faf7a6310f4b595f95186647f12&scene=21#wechat_redirect
# 基础概念
* 文件描述符fd：客户端产生的socket连接实际上是一个文件描述符fd,而每一个用户进程读取的实际上也是一个个文件描述符fd,
## 网络请求流程
* 用户发起系统调用
    * 服务端与客户端是否建立连接，网卡数据是否拷贝到内核缓冲区
    * 阻塞非阻塞针对此步骤而言
        * 阻塞：数据还未到达内核缓冲区，发起系统调用该进程会阻塞直至有数据到来
        * 非阻塞：数据还未到达内核缓冲区，发起系统调用该进程会立即返回
* IO读写:内核态数据拷贝到用户态
    * 同步与异步针对此步骤而言：IO读写(内核态与用户态的数据拷贝)是否需要进程阻塞，如果需要进程阻塞等待则是同步IO，否则就是异步IO
* ![img](https://segmentfault.com/img/bVbZMAV)
##5种IO模型
* 阻塞式IO
    * 用户发起系统调用 和 IO读写 都需要 阻塞
    * 方式： 每次新建连接都需要新建一个线程
    * 评价：需要消耗线程资源
* 非阻塞式IO
    * 用户发起系统调用若内核数据区没有数据不会阻塞立即返回结果，如果有数据依旧会实际IO读写操作依旧阻塞
    * 方式： 每 accept 一个客户端连接后，将这个文件描述符（connfd）放到一个数组里，使用一个线程循环遍历fd执行非阻塞的读写
    * 评价：可以一个线程处理所有连接，但循环系统调用，系统用户态与内核态频繁切换
* IO多路复用
    * 操作系统提供select，poll， epoll提供三个函数帮助用户进程遍历所有的文件描述符，将数据准备好的文件描述符fd返回给用户进程。该方式是阻塞的。
    * select
        * int select(int nfds,fd_set *readfds,fd_set *writefds,fd_set *exceptfds,struct timeval *timeout);
        * 首先一个线程不断接受客户端连接，并把 socket 文件描述符放到一个 list ，
        * 调用select传入 fd 数组（需要拷贝fd 数组一份到内核），将fd 数组交给操作系统去遍历
        * 操作系统会将准备就绪的文件描述符fd做上标识，用户层将不会再有无意义的系统调用开销
        * select 函数返回后，用户依然需要遍历刚刚提交给操作系统的 list
        * 评价：既做到了一个线程处理多个客户端连接（文件描述符），又减少了系统调用的开销
    * poll：和select类似，不过select最多只支持1024个fd，而poll则没有这个限制
    * epoll
        * 内核中保存一份文件描述符集合，无需用户每次都重新传入，只需告诉内核修改的部分即可。
        * 内核不再通过轮询的方式找到就绪的文件描述符，而是通过异步 IO 事件唤醒。
        * 内核仅会将有 IO 事件的文件描述符返回给用户，用户也无需遍历整个文件描述符集合。
        * ![img](https://mmbiz.qpic.cn/mmbiz_png/BBjAFF4hcwolcxS62c1ZRibFc0NUVCJ46h2GrIOb4GbapWqATZwAALWXWH8505zthGzEEyiawU3TicRQgHMj0B0eg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
        * ![img](https://mmbiz.qpic.cn/mmbiz_png/BBjAFF4hcwolcxS62c1ZRibFc0NUVCJ46EJickksDhHBUDOsZw10M4PARys1wiaBeK3wDx2frGnnxFt02Kv2tGiaDQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
    * 评价：而多路复用快的原因在于，操作系统提供了这样的系统调用，使得原来的 while 循环里多次系统调用，变成了一次系统调用 + 内核层遍历这些文件描述符。  
* 信号驱动式IO
* 异步IO(AIO)
    * 应用进程通过 aio_read 告知内核启动某个操作，并且在整个操作完成之后再通知应用进程，包括把数据从内核空间拷贝到用户空间，是真正意义上的无阻塞的IO操作
## Reactor思想
* Reactor模式：由一个不断等待和循环的单独进程（线程）处理IO就绪这个事件（select，epoll），它接受所有handler的注册，在就绪后就调用分发指定handler进行处理，这个角色的名字就叫做Reactor   
* ![img](https://ask.qcloudimg.com/http-save/yehe-1757319/hyn7xqm953.jpeg?imageView2/2/w/1620)