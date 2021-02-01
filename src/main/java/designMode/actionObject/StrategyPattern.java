package designMode.actionObject;

import java.io.IOException;

/**
 * 策略模式：定义了算法族，分别封装起来，让她们可以相互替换。让算法的变化独立于使用算法的客体
 */
public class StrategyPattern {
    public static void main(String[] args) throws IOException {
        Animal bird = new Bird(new BirdMove(),new BirdSound());
        Animal bird1 = new Bird();
        Animal fish = new Fish(new FishMove(),new FishSound());
        bird.performMove();
        bird.performSound();
        bird.commonbehavior();
        bird1.performMove();
        bird1.performSound();
        bird1.commonbehavior();
        fish.commonbehavior();
        fish.performSound();
        fish.performMove();
    }
    //抽出公共属性
    static class Animal{
        //提出变化部分，封装算法实现，针对接口编程
        private SoundBehavior soundBehavior;
        private MoveBehavior moveBehavior;
        public void performSound(){
            soundBehavior.sound();
        }
        public void performMove(){
            moveBehavior.move();
        }
        public void commonbehavior(){
            System.out.println("公共行为：吃饭睡觉打豆豆！");
        }
        public void setSoundBehavior(SoundBehavior soundBehavior) {
            this.soundBehavior = soundBehavior;
        }
        public void setMoveBehavior(MoveBehavior moveBehavior) {
            this.moveBehavior = moveBehavior;
        }
    }
    static class Bird extends Animal{
        //对具体实现进行编程不够灵活
        public Bird() {
            super.moveBehavior=new BirdMove();
            super.soundBehavior=new BirdSound();
        }

        //不对具体类编程,在运行时通过多态指定行为实现类
        public Bird(MoveBehavior moveBehavior,SoundBehavior soundBehavior) {
            super.setMoveBehavior(moveBehavior);
            super.setSoundBehavior(soundBehavior);
        }
    }
    static class Fish extends Animal{
        public Fish() {
            super.moveBehavior=new FishMove();
            super.soundBehavior=new FishSound();
        }
        public Fish(MoveBehavior moveBehavior,SoundBehavior soundBehavior){
            super.setMoveBehavior(moveBehavior);
            super.setSoundBehavior(soundBehavior);
        }
    }
    //针对行为的方法族
    interface SoundBehavior{
        void sound();
    }
    public static class BirdSound implements SoundBehavior{
        @Override
        public void sound() {
            System.out.println("bird sound：叽叽喳喳");
        }
    }
    public static  class FishSound implements SoundBehavior{
        @Override
        public void sound() {
            System.out.println("fish sound:咕噜咕噜");
        }
    }
    interface MoveBehavior{
        void move();
    }
    public static  class FishMove implements MoveBehavior{
        @Override
        public void move() {
            System.out.println("fish move:swim");
        }
    }
    public static class BirdMove implements MoveBehavior{
        @Override
        public void move() {
            System.out.println("bird move：fly");
        }
    }
}
