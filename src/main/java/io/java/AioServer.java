package io.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AioServer {
    private static final int BUF_SIZE=1024;
    private static final int PORT = 8080;
    private static final int TIMEOUT = 3000;
    public static void main(String[] args) {
       new AioServer(8080);
    }
    private ExecutorService service;

    private AsynchronousServerSocketChannel serverChannel;

    public ExecutorService getService() {
        return service;
    }

    public AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public AioServer(int port) {
        init(port);
    }

    private void init(int port) {
        System.out.println("server starting at port "+port+ "src/test");
        // 初始化定长线程池
        service = Executors.newFixedThreadPool(4);
        try {
            // 初始化 AsyncronousServersocketChannel
            serverChannel = AsynchronousServerSocketChannel.open();
            // 监听端口
            serverChannel.bind(new InetSocketAddress(port));
            // 监听客户端连接,但在AIO，每次accept只能接收一个client，所以需要
            // 在处理逻辑种再次调用accept用于开启下一次的监听
            // 类似于链式调用
            serverChannel.accept(this, new AioHandler());

            try {
                // 阻塞程序，防止被GC回收
                TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public class AioHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {
        @Override
        public void completed(AsynchronousSocketChannel result, AioServer attachment) {
            // 处理下一次的client连接。类似链式调用
            attachment.getServerChannel().accept(attachment, this);

            // 执行业务逻辑
            doRead(result);
        }

        /**
         * 读取client发送的消息打印到控制台
         *
         * AIO中OS已经帮助我们完成了read的IO操作，所以我们直接拿到了读取的结果
         *
         *
         * @param clientChannel 服务端于客户端通信的 channel
         */
        private void doRead(AsynchronousSocketChannel clientChannel) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // 从client读取数据,在我们调用clientChannel.read()之前OS，已经帮我们完成了IO操作
            // 我们只需要用一个缓冲区来存放读取的内容即可
            clientChannel.read(
                    buffer,   // 用于数据中转缓冲区
                    buffer,   // 用于存储client发送的数据的缓冲区
                    new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            System.out.println("receive client data length：" + attachment.capacity() + " byte");
                            attachment.flip(); // 移动 limit位置
                            // 读取client发送的数据
                            System.out.println("from client : "+new String(attachment.array(), StandardCharsets.UTF_8));

                            // 向client写入数据
                            doWrite(clientChannel);
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {

                        }
                    }
            );
        }

        private void doWrite(AsynchronousSocketChannel clientChannel) {

            // 向client发送数据，clientChannel.write()是一个异步调用，该方法执行后会通知
            // OS执行写的IO操作，会立即返回
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Scanner s = new Scanner(System.in);
            String line = s.nextLine();
            buffer.put(line.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            clientChannel.write(buffer);
            // clientChannel.write(buffer).get(); // 会进行阻塞，直到OS写操作完成
        }

        /**
         * 异常处理逻辑
         *
         * @param exc
         * @param attachment
         */
        @Override
        public void failed(Throwable exc, AioServer attachment) {
            exc.printStackTrace();
        }
    }
}
