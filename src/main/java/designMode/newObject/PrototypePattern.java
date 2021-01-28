package designMode.newObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 原型模式:用原型实例指定创建对象的种类，并通过拷贝这些原型创建新的对象
 */
public class PrototypePattern {
    public static void main(String[] args)  {
        Prototype prototype = new Prototype("test",new FactoryPattern.AFactory().getProduct());
        Prototype clone = prototype.clone();
        clone.product.show("test1");

    }


    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class Prototype implements Cloneable {
        private String name;
        private FactoryPattern.Product product;

        @Override
        public Prototype clone(){
            Prototype prototype = null;
            try{
                prototype = (Prototype)super.clone();
                prototype.product = ((FactoryPattern.AProduct)this.product).clone();
            }catch(CloneNotSupportedException e){
                e.printStackTrace();
            }
            return prototype;
        }
    }


}