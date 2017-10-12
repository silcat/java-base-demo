package designMode;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 观察者模式：定义对象之间的一对多依赖，当一个对象改变，它所有依赖者都会收到通知
 */
public class ObserverPattern {
    public static void main(String[] args) throws IOException {
        DuiaAppNoteCenter duiaAppNoteCenter = new DuiaAppNoteCenter();
        ObserverA observerA = new ObserverA();
        duiaAppNoteCenter.registerObserver(observerA);
        duiaAppNoteCenter.registerObserver(new ObserverB());
        duiaAppNoteCenter.registerObserver(new ObserverC());
        duiaAppNoteCenter.setNote(2);
        duiaAppNoteCenter.removeObserver(observerA);
        duiaAppNoteCenter.setNote(3);
    }
    //主题
    interface Subject{
       void registerObserver(Observer o);
       void removeObserver(Observer o);
       void notifyObservers();
    }
    //观察者
    interface Observer {
        void update(Integer note);
    }
    //APP设备
    interface APP {
        void display(Integer note);
    }
    //消息中心
    public static class  DuiaAppNoteCenter implements Subject{
        private ArrayList<Observer> observers;
        Integer note=1;
        public DuiaAppNoteCenter() {
            this.observers = new ArrayList();
        }

        @Override
       public void registerObserver(Observer o) {
            observers.add(o);
       }

       @Override
       public void removeObserver(Observer o) {
           observers.remove(o);
       }

       @Override
       public void notifyObservers() {
           for (Observer o:observers) {
               o.update(this.note);
           }
       }
       public void setNote(Integer note){
            this.note=note;
             changed();
       }
        public void changed(){
            notifyObservers();
        }
   }
    //观察者A
    public static class ObserverA implements Observer,APP{
        @Override
        public void update(Integer note) {
            display(note);
        }
        @Override
        public void display(Integer note) {
            System.out.println("观察者A:"+note);
        }
    }
    //观察者B
    public static class ObserverB implements Observer,APP{
        @Override
        public void update(Integer note) {
            display(note);
        }
        @Override
        public void display(Integer note) {
            System.out.println("观察者B:"+note);
        }
    }
    //观察者C
    public static class ObserverC implements Observer,APP{
        @Override
        public void update(Integer note) {
            display(note);
        }
        @Override
        public void display(Integer note) {
            System.out.println("观察者C:"+note);
        }
    }
}
