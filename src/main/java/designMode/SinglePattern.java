package designMode;

import java.io.IOException;

/**
 * 单例模式：确保一些类只存在一个实例，并提供全局访问点。如线程池等
 */
public class SinglePattern {
    public static void main(String[] args) throws IOException, InterruptedException {
        //由于构造器为private，外部无法调用
//        Singleton singleton = new Singleton();

        for (int i = 0; i < 3; i++) {
            Singleton instance = Singleton.getInstance();
        }
        System.out.println("");
        System.out.println("多线程单例模式");

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    SingletonCunrent instance = SingletonCunrent.getInstance();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    SingletonCunrent instance = SingletonCunrent.getInstance();
                }
            }
        }).start();
    }
}

class Singleton {
    private static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        Integer i = 1;
        if (uniqueInstance == null) {
            uniqueInstance = new Singleton();
            i++;
        }
        System.out.print(i);
        return uniqueInstance;
    }
}

class SingletonCunrent {
    private volatile static SingletonCunrent uniqueInstance;

    private SingletonCunrent() {
    }

    public static SingletonCunrent getInstance() {
        Integer i = 1;
        if (uniqueInstance == null) {
            synchronized (SingletonCunrent.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SingletonCunrent();
                }
            }
            i++;
        }
        System.out.print(i);
        return uniqueInstance;
    }
}