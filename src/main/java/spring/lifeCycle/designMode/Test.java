package spring.lifeCycle.designMode;


import java.io.IOException;


public class Test {

    public static void main(String[] args) throws IOException {
        Factory aFactory = new AFactory();
        Factory bFactory = new BFactory();
        aFactory.getProduct("A");
        bFactory.getProduct("B");

    }


}
