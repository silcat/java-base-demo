package designMode.objectStructural;

import java.io.IOException;

/**
 * 装饰器模式：它实际上把核心功能和附加功能给分开了
 */
public class ConcretePattern {
    public static void main(String[] args) throws IOException {
        Milk milk = new Milk("牛奶");
        String drink = new TypeConcrete(milk).getDrink();

    }

    //核心功能类
    public static class Milk extends AbstractDrink{
        private String name ;
        public Milk(String name) {
            this.name = name;
        }

       @Override
        public String getDrink() {
            return name;
        }
    }
    public static abstract class AbstractDrink  {
        public abstract String getDrink();
    }
    //辅助功能装饰类
    public static class TypeConcrete extends AbstractDrink{
        private AbstractDrink drink;
        public TypeConcrete(AbstractDrink drink) {
            this.drink = drink;
        }

        @Override
        public String getDrink() {
            return "加冰"+ drink.getDrink();
        }
    }


}
