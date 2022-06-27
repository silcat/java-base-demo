package juc.test;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by admin on 2018/2/2.
 */

public class TSynchronized implements Runnable {
     static int i = 0;
     static int j = 0;
    public synchronized void increase(){
        i++;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static synchronized void statincrease(){
        i++;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void set(int value){
        i =value;
    }
    public int get(){
        return i;
    }

    @Override
    public void run() {
        for(int i = 0;i < 100;i++) {
           increase();
        }
        System.out.println(i);
    }

    public static void main(String[] args) throws InterruptedException {

        TSynchronized tSynchronized = new TSynchronized();
        TSynchronized tSynchronized1 = new TSynchronized();
        Thread aThread = new Thread(tSynchronized,"a");
        Thread bThread = new Thread(tSynchronized1,"b");
        aThread.start();
        bThread.start();
        aThread.join();
        bThread.join();
        System.out.println(tSynchronized.get());

    }

}
