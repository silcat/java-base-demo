package designMode.provider;


import static designMode.provider.ProviderManager.DEFAULT_PROVIDER;

/**
 * 通过静态块注册
 */
public class DefaultProvider implements ProviderInterface {
    @Override
    public void doSomethting() {
        System.out.println("default");
    }
    static {
        try {
            ProviderManager.registerProvider(DEFAULT_PROVIDER,new DefaultProvider());
        } catch (Exception var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
}
