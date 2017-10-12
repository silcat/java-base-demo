package IO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by admin on 2017/9/21.
 */
public class NioSocketServet {
    public static void main(String[] args) throws IOException {
        //1. 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //2. 切换非阻塞模式
        ssChannel.configureBlocking(false);

        //3. 绑定端口号
        ssChannel.bind(new InetSocketAddress(9898));

        //4. 获取选择器
        Selector selector = Selector.open();

        //5. 将通道注册到选择器上, 并且指定“监听事件”
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        //6. 轮询监听选择器上的“准备就绪”的事件
        while(selector.select() > 0){

            //7. 获取当前选择器上所有“准备就绪”的选择键（监听事件）
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while(it.hasNext()){
                //8. 获取当前准备就绪的选择键
                SelectionKey sk = it.next();

                //9. 判断具体是哪个事件“准备就绪”
                if(sk.isAcceptable()){
                    //10.若接收状态就绪，获取当前客户端的连接
                    SocketChannel sChannel = ssChannel.accept();

                    //11.切换非阻塞式
                    sChannel.configureBlocking(false);

                    //12.将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                }else if(sk.isReadable()){
                    //13.若“读就绪”，获取当前选择器上就绪状态的通道
                    SocketChannel sChannel = (SocketChannel) sk.channel();

                    //14.读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    while((len = sChannel.read(buf)) > 0){
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }

                //15.取消选择键
                it.remove();
            }
        }


    }
}
