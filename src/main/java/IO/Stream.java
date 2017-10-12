package IO;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by admin on 2017/9/21.
 */
public class Stream {

    public static void main(String[] args) throws IOException {
        read(new FileReader("a.txt"));
        readByStream(new File("a.txt"));
        readByMap(new File("a.txt"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                readByNio(new File("a.txt"), 2);
            }
        }).start();

    }

    private static void readByStream(File file) {
        BufferedInputStream bs = null;
        BufferedOutputStream fs = null;
        try {
            bs = new BufferedInputStream(new FileInputStream(file));
            fs = new BufferedOutputStream(new FileOutputStream(new File("c.txt")));
            byte[] b = new byte[1024];
            while (bs.read(b) != -1) {
                fs.write(b);
                fs.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流，其实关闭的就是java调用的系统底层资源。在关闭前，会先刷新该流。
            if (fs != null && fs != null) {
                Integer a = 1;
                try {
                    bs.close();
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void read(FileReader file) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(file);
            //如果d文件中有数据,true表示继续往文件中追加数据
            bw = new BufferedWriter(new FileWriter("d.txt", false));
            String line = null;
            //高效字符输入流的特有方法readline(),每次读取一行数据
            while ((line = br.readLine()) != null) {
                bw.write(line);
                //高效字符输出流的特有方法newline()
                bw.newLine();
                //将缓冲区中的数据刷到目的地文件中
                bw.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流，其实关闭的就是java调用的系统底层资源。在关闭前，会先刷新该流。
            if (bw != null && br != null) {
                try {
                    br.close();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static void readByNio(File file, Integer type) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel channel = null;
        FileChannel channel1 = null;
        String name = "";
        if (type == 1) {
            name = "b.txt";
        } else {
            name = "c.txt";
        }
        try {
            fileInputStream = new FileInputStream(file);
            fileOutputStream = new FileOutputStream(new File(name));
            channel1 = fileOutputStream.getChannel();
            channel = fileInputStream.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int read = channel.read(buf);
            while (read != -1) {
                buf.flip();
                while (buf.hasRemaining()) {
                    channel1.write(buf);
                }
                buf.compact();
                read = channel.read(buf);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (channel1 != null) {
                try {
                    channel1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void readByMap(File file) {
        FileChannel a = null;
        FileChannel b=null;
        try {
            a = new RandomAccessFile("a.txt", "rw").getChannel();
            b = new RandomAccessFile("e.txt", "rw").getChannel();
            MappedByteBuffer in = a.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
            MappedByteBuffer out = b.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
            out.put(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (a!=null){
                    a.close();
                }
                if (b!=null){
                    a.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
