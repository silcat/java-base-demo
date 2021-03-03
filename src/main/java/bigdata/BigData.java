package bigdata;


import base.Stream;
import com.google.common.collect.Sets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;
import java.util.HashSet;

public class BigData {
    public final static String basePath = "E:\\java-base-demo\\" ;
    public static HashSet<String> sets = Sets.newHashSet();


    public static void main(String[] args)  {
        compareBloom(basePath+"url.txt",basePath+"url_b.txt");
//        compare(basePath+"url.txt",basePath+"url_b.txt");
    }

    /**
     *  判断两文件是否有相同的url
     * 1. a，b两文件根据hashcode保存为小文件
     * 2. 分别对比小文件
     *
     */
    public static boolean compare(String aPath,String bPath){
        Stream.ReadFunction saveForkJoinFunction = new Stream.ReadFunction() {
            @Override
            public void doSometint(String line,String path) {

                int hashCode = line.hashCode();
                if (hashCode <0){
                   hashCode = ~hashCode;
                }
                int index = hashCode % 10;
                String type = "a_";
                if (path.contains("url_b")){
                    type ="b_";
                }
                String fileName = type + index +".txt";
                Stream.saveFile( basePath + fileName,line+"\n");
            }
        };
        Stream.getPath(aPath ,saveForkJoinFunction);
        Stream.getPath(bPath ,saveForkJoinFunction);
        for (int index = 0;index<10;index++){
            compareIndex(index);
            sets.clear();
        }
        return true;
    }
    public static void compareIndex(int index){
        Stream.ReadFunction compareFunction = new Stream.ReadFunction() {
            @Override
            public void doSometint(String line,String path) {
                if (path.contains("b_")){
                    if (sets.contains(line)){
                        Stream.saveFile( basePath + "union.txt",line+"\n");
                    }
                }else {
                    sets.add(line);
                }
            }
        };
        Stream.getPath(basePath+"a_"+index+".txt",compareFunction);
        Stream.getPath(basePath+"b_"+index+".txt",compareFunction);
    }
    public static boolean compareBloom(String aPath,String bPath){
        BloomFilter<String> bloomFilters = BloomFilter
                .create(Funnels.stringFunnel(Charset.defaultCharset()), 1000, 0.01);
        Stream.ReadFunction saveForkJoinFunction = new Stream.ReadFunction() {
            @Override
            public void doSometint(String line,String path) {
                if (path.contains("url_b")){
                    if (bloomFilters.mightContain(line)){
                        Stream.saveFile( basePath + "union.text",line+"\n");
                    }
                }else {
                    bloomFilters.put(line);
                }
            }
        };
        Stream.getPath(aPath ,saveForkJoinFunction);
        Stream.getPath(bPath ,saveForkJoinFunction);
        return true;
    }
}
