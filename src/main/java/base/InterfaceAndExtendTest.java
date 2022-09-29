package base;


import redis.clients.jedis.Jedis;

/**
 * 接口，继承 与 多态
 */
public class InterfaceAndExtendTest {
    public static void main(String[] args) {
 new RedisTe
        Car aCar = new ACar();
        Car bCar = new BCar();
        Car cCar = new CCar();
        CarType cCarType = new CCar();

        if ( cCarType instanceof  CCar){
            System.out.println("cCarType  = CCar");
        }
        if ( cCarType instanceof  Car){
            System.out.println("cCarType  = Car");
        }
        if (cCarType instanceof  CarType){
            System.out.println("cCarType  = CarType");
        }
        System.out.println("bCar："+ bCar.funtion());
        //多态问题：无法获取子类的属性

    }
    interface CarType{
       void  lable(String lable);
    }
    public static abstract class Car {
        String name;
        protected abstract String funtion();
    }
     static class ACar extends Car{
        @Override
        protected String  funtion() {
            System.out.print("ACar");
            return "ACar";
        }
    }
    static class BCar extends ACar{
        @Override
        protected String funtion() {
            System.out.print("BCar");
            return "BCar";
        }
    }
    static class CCar extends Car implements CarType{
        public String lable;
        @Override
        protected String funtion() {
            System.out.print("CCar");
            return "CCar";
        }
        @Override
        public void lable(String lable) {
            System.out.print("labler:"+lable);
            this.lable =lable;
        }
    }

}
