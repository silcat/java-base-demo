package base;

import juc.Test;
import oracle.jrockit.jfr.events.Bits;
import sun.security.ec.CurveDB;

import java.util.List;

/**
 * Created by admin on 2018/2/2.
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        System.out.println("-------------启动类加载器------------");
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println("-------------拓展类加载器------------");
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println("-------------系统加载器------------");
        System.out.println(System.getProperty("java.class.path"));
        System.out.println("【rt.jar】Bits.类的加载器的名称:"+ Bits.class.getClassLoader()+"【有的虚拟机实现会用null 来代替bootstrap这个classloader】");
        System.out.println("【sun.jar】CurveDB类的加载器的名称:"+ CurveDB.class.getClassLoader());
        System.out.println("【项目类】Test类的加载器的名称:"+ Test.class.getClassLoader().getClass().getName());
        System.out.println("BootsTrapClassLoader 写在JVM中，jdk无此类 ，AppClassLoader和 ExtClassLoader 在 Launcher 中完成初始化，默认加载器是AppClassLoader");
    }

}
