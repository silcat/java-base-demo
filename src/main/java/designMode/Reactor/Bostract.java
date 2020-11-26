package designMode.Reactor;

import java.io.IOException;

public class Bostract {
    public static void main(String[] args) throws IOException {
        new Thread(new Reactor(9097)).start();
    }
}
