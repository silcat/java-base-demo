package designMode.actionObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.RedisClient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 备忘录模板：不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。
 */
public class MementoPattern {
    public static void main(String[] args)  {
        Originator originator = new Originator("小白");
        originator.playGame();
        MementoManager.getInstance().saveState(originator.exitGame());
        originator.restoretMemento( MementoManager.instance.getState(originator.getName()));



    }
    public static   abstract class AbstractSetting {
        public final String getSetting(String key) {
            String value = lookupCache(key);
            if (value == null) {
                System.out.println("数据库查数据");
                putIntoCache(key, value);
            }
            return value;
        }

        protected abstract String lookupCache(String key);

        protected abstract void putIntoCache(String key, String value);
    }

    /**
     * 发起者
     */
    @Data
    public static class Originator {
        public int time;
        public int level;
        public boolean isExit;
        public String name;

        public Originator(String name) {
            time = 0;
            level = 0;
            isExit = false;
            this.name = name;
        }

        /**
         * 改变状态
         */
        public void playGame() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isExit){
                        time++;
                        level++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        public State exitGame(){
            isExit = true;
            return saveMemento();
        }
        public State saveMemento(){
           return new State(time,level,name);
        }
        public void restoretMemento(State state){
            time = state.getTime();
            level = state.getLevel();
            isExit = false;
        }


    }

    /**
     * 备忘录
     */
    @Data
    @AllArgsConstructor
    public static class State {
        public int time;
        public int level;
        public String name;

    }

    /**
     * 备忘录管理
     */
    public static class MementoManager {
        public final Map<String,State> map = new LinkedHashMap<>();
        public  static volatile MementoManager instance;
        public static Object lock = new Object();

        public static MementoManager  getInstance() {
            if (instance == null){
                synchronized (lock){
                    if (instance == null){
                        instance = new MementoManager();
                    }
                }
            }
            return instance;
        }
        public  void saveState(State state){
            map.put(state.getName(),state);
        }
        public  State getState(String name){
            return map.get(name);
        }
    }

}