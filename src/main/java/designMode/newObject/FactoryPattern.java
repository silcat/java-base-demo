package designMode.newObject;

import lombok.AllArgsConstructor;


/**
 * 抽象工厂模式：将工厂与产品解耦
 */
public class FactoryPattern {
    public static void main(String[] args) {
        String showA = new AFactory().getProduct().show();
        String showB = new BFactory().getProduct().show();
    }

    /**
     * 工厂
     */
    public static abstract  class AbstractFactory {
        private String name;
        abstract Product getProduct();
        abstract Product getProductB();
    }
    public static  class AFactory extends AbstractFactory{
        @Override
        Product getProduct() {
            return new AProduct("A");
        }

        @Override
        Product getProductB() {
            return new AProduct("A1");
        }
    }
    public static  class BFactory extends AbstractFactory{
        @Override
        Product getProduct() {
            return new BProduct("B");
        }

        @Override
        Product getProductB() {
            return new BProduct("B1");
        }
    }
    /**
     * 产品
     */
    public  abstract  interface Product{
        String show();
    }
    public static abstract class AbstractProduct implements Product{
        private String name;
    }
    @AllArgsConstructor
    public static  class AProduct extends AbstractProduct{
        private String name;

        @Override
        public String show() {
            return "A";
        }
    }
    @AllArgsConstructor
    public static  class BProduct extends AbstractProduct{
        private String name;

        @Override
        public String show() {
            return "B";
        }
    }


}