package 服务器.socket.test;


import org.springframework.beans.propertyeditors.InputStreamEditor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    public static void main(String[] args) throws Exception {
        server();
    }
    public static void server() throws Exception {
        ExecutorService executor =  Executors.newFixedThreadPool(2);//线程池
        ServerSocket serverSocket = new ServerSocket();
        InetSocketAddress socketAddress = new InetSocketAddress(9002);
        serverSocket.bind(socketAddress);
        while(!Thread.interrupted()){
            //线程阻塞等待数据到来
            Socket socket = serverSocket.accept();
            executor.submit(new ConnectIOnHandler(socket));//为新的连接创建新的线程
    }}

    static class ConnectIOnHandler extends Thread{
        private Socket socket;
        public ConnectIOnHandler(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run(){
            while(!Thread.interrupted()&&!socket.isClosed()){
                try( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
                     BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")))) {

                    //判断客户端连接断开,客户端断开，线程退出
                    try {
                        socket.sendUrgentData(0);
                    } catch (IOException e) {
                        break;
                    }
                    String message = null;
                    StringBuilder sb = new StringBuilder();
                    int i = 0;
                    char[] buf = new char[4096];
                    while ((i = bufferedReader.read(buf)) != -1) {
                        sb.append(buf, 0, i);
                        message = sb.toString();
                        if (message.contains("\n")||message.contains("eof")) {
                            break;
                        }
                    }
                    System.out.println("message:" + message);
                    //2、数据以\r或者\n结尾，使用readLine读取
//                    String line = null;
//                    while((line = bufferedReader.readLine()) != null) {
//                        sb.append(line);
//                    }
                    System.out.println("message:" + sb.toString());
                    bufferedWriter.write("发送成功\n");
                    bufferedWriter.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
