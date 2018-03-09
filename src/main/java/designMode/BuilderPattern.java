package designMode;

import java.io.IOException;

/**
 * Created by admin on 2017/10/19.
 */
public class BuilderPattern {
    public static void main(String[] args) throws IOException {
        QiRuiBuilder qiRuiBuilder = new QiRuiBuilder();
        Director director = new Director(qiRuiBuilder);
        director.construct();
        System.out.println("输出到文本文件的内容：\n"+qiRuiBuilder.getCar().getName());
        BMWBuilder bmwBuilder = new BMWBuilder();
        Director director1 = new Director(bmwBuilder);
        director1.construct();
        System.out.println("输出到文本文件的内容：\n"+bmwBuilder.getCar().getName());

    }
}

interface Builder{
    void prepare();
    void pack();
    void finish();
    Car1 getCar();
}


class BMWBuilder implements Builder{


    @Override
    public void prepare() {

    }

    @Override
    public void pack() {

    }

    @Override
    public void finish() {

    }

    @Override
    public Car1 getCar() {
        return new BMWCar1("宝马");
    }
}

class QiRuiBuilder implements Builder{

    @Override
    public void prepare() {
        //子类定义
    }

    @Override
    public void pack() {

    }

    @Override
    public void finish() {

    }

    @Override
    public Car1 getCar() {
        return new QiRuiCar1("奇瑞");
    }
}

class Director{
   private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }
    public void construct(){
        builder.prepare();
        builder.pack();
        builder.finish();
    }
}

abstract class Car1{
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
class  BMWCar1 extends Car1{
    public BMWCar1(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
class QiRuiCar1 extends Car1{

    public QiRuiCar1(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
