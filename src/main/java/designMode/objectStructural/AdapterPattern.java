package designMode.objectStructural;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * 适配器模式
 */
public class AdapterPattern {
    public static void main(String[] args) throws IOException {
//        Callable<Long> callable = new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                return null;
//            }
//        };
//        new Thread(new RunnbleAdapter(callable));

        String speakEnglish = new Adapeter().speakEnglish();
    }
    //对象适配
    public static class RunnbleAdapter implements Runnable {

        // 引用待转换接口:
        private Callable<?> callable;

        public RunnbleAdapter(Callable<?> callable) {
            this.callable = callable;
        }
        @Override
        public void run() {
            try {
                Object call = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    //类适配
    public static class Targe {
        public  String speakLanguage(){
            return "中文";
        }
    }
    public interface AdapeterLanguage {
        String speakEnglish();
        String speakJanpanese();
    }
    public static class DafautAdapeter extends Targe implements AdapeterLanguage {

        @Override
        public String speakEnglish() {
            return "";
        }

        @Override
        public String speakJanpanese() {
            super.speakLanguage();
            return "";
        }
    }
    public static class Adapeter extends DafautAdapeter  {
        @Override
        public String speakEnglish() {
            super.speakLanguage();
            return "english";
        }
    }


}
