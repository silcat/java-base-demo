package spring.lifeCycle.designMode;


import java.io.IOException;


public class Test {

    public static void main(String[] args) throws IOException {
        AFactory aFactory = new AFactory();
        BFactory bFactory = new BFactory();
        aFactory.getProduct("A");
        bFactory.getProduct("B");

    }


}
