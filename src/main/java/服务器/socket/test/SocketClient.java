package 服务器.socket.test;

import org.springframework.beans.propertyeditors.InputStreamEditor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) throws IOException {
//       bio();
       nio();
    }
    public static void bio() throws IOException {

            //1. 创建Socket对象并且指定服务器的地址和端口号
        Socket socket = new Socket("127.0.0.1", 9002);

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));) {
            Scanner scanner = new Scanner(System.in);

            //控制台
            String scmsg;
            while ((scmsg = scanner.nextLine())!=null){
                if (scmsg.contains("end")){
                    break;
                }
                bufferedWriter.write(scmsg);
                bufferedWriter.flush();
                char[] buf = new char[4096];
                String resp = read(buf, bufferedReader);
                System.out.println("respone:"+resp);
            }
        }catch (Exception e){}

    }
    public static String read(char[] buf,BufferedReader bufferedReader) throws IOException {
        String line;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while ((i = bufferedReader.read(buf)) != -1) {
            sb.append(buf, 0, i);
            line = sb.toString();
            if (line.contains("\n")) {
                break;
            }
        }
        return sb.toString();
    }
    public static void nio() throws IOException {
        //1. 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9001));

        //2. 切换非阻塞模式
        sChannel.configureBlocking(false);

        //3. 分配缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //4. 发送数据给服务端
        Scanner scan = new Scanner(System.in);

        while(scan.hasNext()){
            String str = scan.next();

            buf.put((new Date().toString() + "\n" + str).getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        //5. 关闭通道
        sChannel.close();
    }

}
