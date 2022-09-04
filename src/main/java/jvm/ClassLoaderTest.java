package jvm;

import juc.test.TSynchronized;
import oracle.jrockit.jfr.events.Bits;
import sun.security.ec.CurveDB;

/**
 * Created by admin on 2018/2/2.
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        classLoader();
    }
    public static void classLoader(){
        System.out.println("-------------启动类加载器------------");
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println("-------------拓展类加载器------------");
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println("-------------系统加载器------------");
        System.out.println(System.getProperty("java.class.path"));
        System.out.println("【rt.jar】Bits.类的加载器的名称:"+ Bits.class.getClassLoader()+"【有的虚拟机实现会用null 来代替bootstrap这个classloader】");
        System.out.println("【sun.jar】CurveDB类的加载器的名称:"+ CurveDB.class.getClassLoader());
        System.out.println("【项目类】Test类的加载器的名称:"+ TSynchronized.class.getClassLoader().getClass().getName());
        System.out.println("BootsTrapClassLoader 写在JVM中，jdk无此类 ，AppClassLoader和 ExtClassLoader 在 Launcher 中完成初始化，默认加载器是AppClassLoader");

    }

    /**
     * 自定义加载器
     */
    public static class MyClassLoader extends ClassLoader{
        private final static String fileSuffixExt = ".class";
        private String loadPath;
        //指定加载器为父类
        public MyClassLoader(ClassLoader parent,String path) {
            super(parent);
            loadPath = path;
        }
        //使用app加载器为父加载器
        public MyClassLoader(String path) {
            super();
            loadPath = path;
        }
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            //获取class文件的字节数组
            byte[] resultData = this.loadByteClassData(name);
            return super.defineClass(name, resultData, 0, resultData.length);
        }
        /**
         * 加载指定路径下面的class文件的字节数组
         * @param name 二进制文件名称
         * @return 二进制字节数组
         */
        private byte[] loadByteClassData(String name) throws ClassNotFoundException {

            return null;
        }


    }

}
