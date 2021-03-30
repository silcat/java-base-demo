package io;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    public static void main(String[] args) throws Exception {
        server();
    }
    public static void server() throws Exception {
        ExecutorService executor =  Executors.newFixedThreadPool(100);//线程池
        ServerSocket serverSocket = new ServerSocket();
        InetSocketAddress socketAddress = new InetSocketAddress(8088);
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
                try {
                    //io读写线程阻塞
                    socket.getChannel().read((ByteBuffer) null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
