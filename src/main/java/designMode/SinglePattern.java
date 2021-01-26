package designMode;

import java.io.IOException;

/**
 * 单例模式：确保一些类只存在一个实例，并提供全局访问点。如线程池等
 */
public class SinglePattern {
    public static void main(String[] args) throws IOException, InterruptedException {

    }
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

}