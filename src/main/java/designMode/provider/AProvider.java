package designMode.provider;

/**
 * 通过SPI注册
 */
public class AProvider implements ProviderInterface {
    @Override
    public void doSomethting() {
        System.out.println("A");
    }
}
