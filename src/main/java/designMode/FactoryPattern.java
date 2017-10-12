package designMode;

import java.io.IOException;

/**
 * 工厂模式：定义了一个创建对象的接口，但由子类决定要实例化的类是哪个，工厂方法让类把实例化推迟到子类
 * 定义抽象工厂模式：提供一个接口，用于创建相关或依赖对象的家族，而不用明确指定具体类
 */
public class FactoryPattern {
    public static void main(String[] args) throws IOException {
        CarFactory bmwFactory = new BMWFactory();
        Car car = bmwFactory.createCar(Type.HIGE);
        CarFactory qiRuiFactory = new QiRuiFactory();
        Car car1 = qiRuiFactory .createCar(Type.LOW);
    }
}

/**
 * 枚举类：汽车的等级
 */
enum Type {
    HIGE, LOW
}
/**
 * 工厂抽象类，所有工厂继承此类
 */
abstract class CarFactory {
    public Car createCar(Type type) {
        Car car = create(type);
        car.prepare();
        car.pack();
        car.finish();
        return car;
    }

    abstract Car create(Type type);
}

class BMWFactory extends CarFactory {
    @Override
    Car create(Type type) {
        CarIngredientFactory bmwCarIngredientFactory = new ImplCarIngredientFactory(type);
        Car car = new BWMCar(bmwCarIngredientFactory);
        return car;
    }
}

class QiRuiFactory extends CarFactory {
    @Override
    Car create(Type type) {
        CarIngredientFactory bmwCarIngredientFactory = new ImplCarIngredientFactory(type);
        Car car = new QiRuiCar(bmwCarIngredientFactory);
        return car;
    }
}

/**
 * 产品抽象类，所有产品继承此类，封装公共属性与属性，待子类实现方法设置为抽象方法
 */
abstract class Car {
    private String name;
    private Energine energine;
    private AirCondition airCondition;

    abstract void prepare();

    void pack() {
        System.out.println("开始组装"+energine.getName()+",和,"+airCondition.getName());
    }
    void finish(){
        System.out.println(name+"生成完毕!");
    };


    public Energine getEnergine() {
        return energine;
    }

    public void setEnergine(Energine energine) {
        this.energine = energine;
    }

    public AirCondition getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(AirCondition airCondition) {
        this.airCondition = airCondition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class BWMCar extends Car {
    private CarIngredientFactory carIngredientFactory;

    public BWMCar(CarIngredientFactory carIngredientFactory) {
        super.setName("宝马轿车");
        this.carIngredientFactory = carIngredientFactory;
    }

    @Override
    void prepare() {
        super.setAirCondition(carIngredientFactory.createAirCondition());
        super.setEnergine(carIngredientFactory.createEnergine());
        System.out.println("构建准备完毕");
    }


}

class QiRuiCar extends Car {
    private CarIngredientFactory carIngredientFactory;

    public QiRuiCar(CarIngredientFactory carIngredientFactory) {
        super.setName("奇瑞轿车");
        this.carIngredientFactory = carIngredientFactory;
    }

    @Override
    void prepare() {
        super.setAirCondition(carIngredientFactory.createAirCondition());
        super.setEnergine(carIngredientFactory.createEnergine());
        System.out.println("构建准备完毕");
    }

}

/**
 * 原料工厂接口，所有原料工厂实现此类，封装公共属性与属性，待子类实现方法设置为抽象方法
 */
interface CarIngredientFactory {
    Energine createEnergine();

    AirCondition createAirCondition();
}

class ImplCarIngredientFactory implements CarIngredientFactory {
    private Type type;

    public ImplCarIngredientFactory(Type type) {
        this.type = type;
    }

    @Override
    public Energine createEnergine() {
        if (type == Type.HIGE) {
            return new HighEnergine();
        } else {
            return new LowEnergine();
        }
    }

    @Override
    public AirCondition createAirCondition() {

        if (type == Type.HIGE) {
            return new HighAirCondition();
        } else {
            return new LowAirCondition();
        }

    }
}

/**
 * 原料类
 */
abstract class Energine {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class HighEnergine extends Energine {

    public HighEnergine() {
       super.setName("高性能发动机");
    }
}

class LowEnergine extends Energine {

    public LowEnergine() {
        super.setName("低性能发动机");
    }
}

abstract class AirCondition {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class HighAirCondition extends AirCondition {

    public HighAirCondition() {
        super.setName("高性能空调");
    }
}

class LowAirCondition extends AirCondition {

    public LowAirCondition() {
        super.setName("低性能空调");
    }
}