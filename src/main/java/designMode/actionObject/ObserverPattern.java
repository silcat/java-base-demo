package designMode.actionObject;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 观察者模式：
 */
public class ObserverPattern {
    public static void main(String[] args)  {

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class Subject{
        public List<Observer> observers;

        public  void attach(Observer observer){
            observers.add(observer);
        }
        public  void remove(Observer observer){
                observers.remove(observer);
        }
        public  void notify(String msg){
            observers.stream().forEach(observer -> observer.update(msg));
        }

    }
    //观察者
    public interface   Observer{
        void update(String msg);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class ObserverOne implements Observer{
         public String msg;

        @Override
        public void update(String msg) {
            msg = msg;
        }
    }



}