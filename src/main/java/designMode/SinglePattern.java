package designMode;

import java.io.IOException;
import java.io.Serializable;

/**
 * 单例模式：确保一些类只存在一个实例，并提供全局访问点。如线程池等
 */
public class SinglePattern {
    public static void main(String[] args) throws IOException, InterruptedException {

    }

    /**
     * 懒汉式
     * volatile修饰的原因：
     *  1.new 分为3步：创建堆内存，对象赋值，将对象内存地址赋予引用
     *  2.volatile语义为先写后读
     *  3.若不修饰，则对象刚刚创建堆内存还未赋值，此时判断对象不为空，则获取的是空对象
     */
    public static  class PartrernOne  {
        private static volatile PartrernOne  instance;
        public static PartrernOne getInstance(){
            if (instance == null){
                synchronized (instance){
                    if (instance == null){
                        instance = new PartrernOne();
                    }
                }
            }
            return instance;
        }

    }


    /**
     * 饿汉式
     * 类加载到内存后，就实例化一个单例，JVM保证线程安全。唯一缺点：不管用到与否，类装载时就完成实例化。
     */
    public static class Singleton01 {
        private static final Singleton01 INSTANCE = new Singleton01();
        private Singleton01() { };
        public static final Singleton01 getInstance() {
            return INSTANCE;
        }
    }

    /**
     * 静态内部类方式
     * 线程安全(枚举实例的创建默认就是线程安全的)
     * 不会因为序列化而产生新实例
     * 防止反射攻击
     */
    public static class Singleton02{
        private Singleton02() {
        }

        private static class SingletonHolder {
            private static final Singleton02 INSTANCE = new Singleton02();
        }

        public static Singleton02 getInstance() {
            return SingletonHolder.INSTANCE;
        }

    }
    /**
     * 枚举类
     * 不仅可以解决线程同步，还可以防止反序列化。
     */
    public static class Singleton03 {
        //私有化构造函数
        private Singleton03(){ }

        //定义一个静态枚举类
        enum SingletonEnum{
            //创建一个枚举对象，该对象天生为单例
            INSTANCE;
            private Singleton03 user;
            //私有化枚举的构造函数
            private SingletonEnum(){
                user=new Singleton03();
            }
            public Singleton03 getInstnce(){
                return user;
            }
        }

        //对外暴露一个获取User对象的静态方法
        public static Singleton03 getInstance(){
            return SingletonEnum.INSTANCE.getInstnce();
        }
    }

    /**
     * 破环单例模式的三种方式及优化：
     * 反射：定义一个全局变量开关,当第一次加载时将其状态更改为关闭状态
     * 序列化：添加readResolve()返回单例对象
     * 克隆：重写clone方法返回单例对象
     */
    public static class Singleton04 implements Serializable,Cloneable {
        private static volatile Singleton04  instance;
        private static boolean isFristCreate = true;
        public Singleton04() {
            if (isFristCreate) {
                synchronized (Singleton04.class) {
                    if (isFristCreate){
                        isFristCreate = false;
                    }
                }
            }else{
                throw new RuntimeException("已然被实例化一次，不能在实例化");
            }

        }

        public static Singleton04 getInstance(){
            if (instance == null){
                synchronized (instance){
                    if (instance == null){
                        instance = new Singleton04();
                    }
                }
            }
            return instance;
        }

        @Override
        protected Singleton04 clone()  {
            return instance;
        }

        private Object readResolve() {
            return instance;
        }

    }
}