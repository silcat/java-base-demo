package io;

import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class NioReactorServer {
    private static final int PORT = 9001;
    public static void main(String[] args)
    {
        SelectorThreadGroup boss = new BossSelectorThreadGroup(1);
        SelectorThreadGroup work = new SelectorThreadGroup(3);
        ((BossSelectorThreadGroup) boss).bind(PORT,work);
    }
    @Data
    public static class SelectorThreadGroup {

        //管理的Selector线程组
        SelectorThread[] selectors;
        //准备一个server，给主线程绑定
        ServerSocketChannel server;
        //轮询选择selector时候的数组下标，用AtomicInteger保证线程安全
        AtomicInteger selectIndex = new AtomicInteger(0);

        public SelectorThreadGroup(int threadNums) {
            //开辟一个存放Selector的组
            selectors = new SelectorThread[threadNums];
            //这个时候selector还都是空，需要new
            for (int i = 0; i < threadNums; i++) {
                selectors[i] = new SelectorThread(this);

                //selector线程跑起来
                new Thread(selectors[i]).start();
                //程序运行到这个地方，分析一下，执行SelectorThread的run方法
                //执行到 int nums = selector.select(); 这就阻塞住了，并且不会有返回
            }
        }

        public SelectorThread nextSelector(Channel channel) {
            SelectorThread selectorThread = next(channel);
            selectorThread.taskQueue.add(channel);
            return selectorThread;

        }

        /**
         * server（ServerSocketChannel）要选selector，client(SocketChannel)也要选selector，
         * 因此，参数设置为ServerSocketChannel和SocketChannel的父类-Channel
         * @param channel
         */
        private SelectorThread next(Channel channel) {
            //selector在哪？要怎么去选？
            //从SelectorThreadGroup管理的SelectorThread[] selectors组中选出来一个就可以了
            //第1个客户端过来，分配到selectors[0]上，第2个客户端分配到selectors[1]上...以此类推
            //这就是一个轮询的概念，取模，因为是由多个selector线程，要考虑线程安全
            if (selectors.length-1 ==0){
                return selectors[0];

            }
            int index = selectIndex.incrementAndGet() % (selectors.length-1);
            return selectors[index];
        }
    }
    @Data
    public static class BossSelectorThreadGroup extends  SelectorThreadGroup{
        //持有管理的Selector线程组SelectorThreadGroup的引用
        public  SelectorThreadGroup workGroup;
        public BossSelectorThreadGroup(int threadNums) {
            super(threadNums);
        }
        public void bind(int port, SelectorThreadGroup work) {
            try {
                server = ServerSocketChannel.open();

                //server准备好后，要绑定到指定端口上
                server.socket().bind(new InetSocketAddress("127.0.0.1",port));
                server.configureBlocking(false);
                workGroup = work;
                //server要注册到哪个selector上？
                //这个时候我们定义一个干活的，专门选出将server注册到哪个selector
                SelectorThread selectorThread = nextSelector(server);
                server.register(selectorThread.selector,SelectionKey.OP_ACCEPT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static class SelectorThread implements Runnable {

        //首先得有一个Selector
        Selector selector;

        //用队列去进行线程间的通信
        LinkedBlockingQueue<Channel> taskQueue = new LinkedBlockingQueue<Channel>();

        //持有管理的Selector线程组SelectorThreadGroup的引用
        SelectorThreadGroup group;

        //构造方法中，打开Selector
        public SelectorThread(SelectorThreadGroup group) {
            try {
                selector = Selector.open();
                this.group = group;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            //Loop
            //因为工作线程有一个循环，不停的处理事情，所以先来一个while循环
            //while循环所做的事情：
            //setp 1 select
            //step 2 processSelectKeys
            //setp 3 runAllTasks
            while (true) {
                try {

                    int nums = selector.select(10000);

                    if (nums > 0) {
                        Set<SelectionKey> keys = selector.selectedKeys();
                        //逐一线性处理
                        Iterator<SelectionKey> iterator = keys.iterator();
                        //线性处理的过程
                        while (iterator.hasNext()) {
                            //获得key
                            SelectionKey key = iterator.next();
                            //在迭代器中remove掉这个key，防止重复处理
                            iterator.remove();
                            if (key.isAcceptable()) {
                                //处理连接事件
                                //接收客户端的过程（接收之后，要注册，多线程下，新的客户端注册到哪里？）
                                acceptHandler(key);
                            } else if (key.isReadable()) {
                                //处理读事件
                                readHandler(key);
                            } else if (key.isWritable()) {
                                //处理写事件
                            }
                        }
                    }
                    //step 3 处理一些task
                    //这个队列：是堆里的对象，线程的栈是独立的，堆是共享的
                    if (!taskQueue.isEmpty()) {
                        //略
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void readHandler(SelectionKey key) {
            System.out.println(Thread.currentThread().getName() + " 进入readHandler...");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //得到客户端channel
            SocketChannel clientChannel = (SocketChannel) key.channel();
            //先处理简单的：客户端发什么，服务端就返回什么
            while (true) {
                try {
                    //read非阻塞，若内核缓冲区无数据立即返回
                    int num = clientChannel.read(buffer);
                    //如果读到内容了
                    if (num > 0) {
                        //将读到的内容翻转，然后直接写出
                        String msg = new String(buffer.array());
                        System.out.println(msg + " from " + clientChannel.hashCode());
                        buffer.flip();
                        while (buffer.hasRemaining()) {

                            clientChannel.write(buffer);
                        }
                        //全写完之后，清一下buffer
                        buffer.clear();
                    }
                    //没有读取到内容
                    else if (num == 0) {
                        //直接跳出，无需处理了
                        break;
                    }
                    //num < 0 的情况，比如客户端断开了
                    else {
                        System.out.println("client:" + clientChannel.getRemoteAddress() + " has been closed.");
                        //取消掉
                        key.cancel();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void acceptHandler(SelectionKey key) {
            System.out.println(Thread.currentThread().getName() + " 进入acceptHandler...");
            //接收，只需要拿到channel就可以
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            try {
                //可以accept得到客户端了
                SocketChannel client = serverChannel.accept();
                //设置成非阻塞
                client.configureBlocking(false);
                //选择一个多路复用器selector，并且注册
                SelectorThread selectorThread = ((BossSelectorThreadGroup) group).getWorkGroup().nextSelector(client);
                client.register(selectorThread.selector,SelectionKey.OP_READ);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
