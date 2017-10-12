package designMode;

import java.io.IOException;

/**
 * <p>
 * 装饰者模式:动态的将责任附加到对象上，若要拓展功能，装饰者提供比继承更有弹性的替代方案
 * 类应该对拓展开发，对修改关闭，想想观察者模式
 */
public class DecoratorPattern {
    public static void main(String[] args) throws IOException {
        Potato potato = new Potato();
        Hamburger hamburger = new SausageDecorator(new EggDecorator(potato));
        hamburger.create();
        hamburger.price();
    }
    //抽象构件(Component)
    static abstract class Hamburger{
        private Integer price=1;
        abstract void create();
        abstract Integer price();
    }

    //具体的构件(ConcreteComponent)
    static class Potato extends Hamburger {
        @Override
        void create() {
            System.out.println("这是一个普通的汉堡包,价格为："+super.price);
        }
        @Override
        Integer price() {
            Integer price = super.price;
            return price;
        }
    }

    //抽象装饰类(Decorator)
    static abstract class Decorator extends Hamburger {
        private Hamburger hamburger;

        protected Decorator(Hamburger hamburger) {
            this.hamburger = hamburger;
        }

        @Override
        public void create() {
            hamburger.create();
        }
        @Override
        public Integer price()  {
           return hamburger.price();
        }
    }

    //具体装饰类（ConcreteDecorator）
    static class EggDecorator extends Decorator {
        public EggDecorator(Hamburger hamburger) {
            super(hamburger);
        }

        @Override
        public void create() {
            super.create();
            System.out.print("加了一个蛋");
        }
        @Override
        public Integer price()  {
            int i = super.price() + 1;
            System.out.println("现在价格为:"+i);
            return i;
        }

    }
    //具体装饰类（ConcreteDecorator）
    static class SausageDecorator extends Decorator {
        public SausageDecorator(Hamburger hamburger) {
            super(hamburger);
        }

        @Override
        public void create() {
            super.create();
            System.out.print("加了一个香肠");
        }
        @Override
        public Integer price()  {
            int i = super.price() + 1;
            System.out.println("现在价格为:"+i);
            return i;
        }

    }


}
