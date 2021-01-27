package designMode.newObject;


/**
 * 建造者模式：
 */
public class BuilderPattern {
    public static void main(String[] args)  {

        Car build = Car.builder()
                .name("A")
                .wheel("B")
                .body("C")
                .energen("D")
                .build();
    }

    /**
     * 简单建造者模式
     */
    public static  class Car  {
        private String name;
        private String wheel;
        private String energen;
        private String body;

        public Car(Builder builder) {
            name = builder.name;
            wheel = builder.wheel;
            energen = builder.energen;
            body = builder.body;
        }
        public static Builder builder(){
            return new Car.Builder();
        }
        public static  class Builder{
            private String name;
            private String wheel;
            private String energen;
            private String body;

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder wheel(String wheel) {
                this.wheel = wheel;
                return this;
            }

            public Builder energen(String energen) {
                this.energen = energen;
                return this;
            }

            public Builder body(String body) {
                this.body = body;
                return this;
            }
            public Car build() {
                return new Car(this);
            }
            public Car buildB() {
                this.energen("C");
                return new Car(this);
            }
        }

    }


}