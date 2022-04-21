package base;

import java.io.*;
import java.util.HashMap;

public class Stream {
    public final HashMap map = new HashMap();
    public  void main(String[] args)  {
        String path = getPath("E:\\java-base-demo\\1.txt");
        String replace = path.replace("a", "a1");
        saveFile("E:\\java-base-demo\\1.txt",replace);
        map.put("","");
    }
    public static String getPath(String path){
        File file = new File(path);
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            StringBuffer sbuffer = new StringBuffer();
            bf.lines().forEach(line ->sbuffer.append(line+"\n"));
            System.out.println(sbuffer.toString());
            return sbuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getPath(String path,ReadFunction function){
        File file = new File(path);
        FileReader fr = null;
        try {
            if (!file.exists()){
                return null;
            }
            fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            StringBuffer sbuffer = new StringBuffer();
            bf.lines().forEach(line ->function.doSometint(line,path));
            System.out.println(sbuffer.toString());
            return sbuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public interface ReadFunction{
        void doSometint(String line,String path);
    }
    public static void saveFile(String path,String content){
        BufferedWriter bw =null;
        System.out.println(content);
        try {
            bw = new BufferedWriter(new FileWriter(path,true)) ;
            bw.write(content);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
