package designMode.provider;


import java.nio.channels.spi.SelectorProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProviderManager {
    private static final Map<String, ProviderInterface> registeredProviders = new ConcurrentHashMap<String, ProviderInterface>();
    public static final String DEFAULT_PROVIDER = "default";
    static {
        loadInitialProviders();
        System.out.println("ProvidersManager initialized");
    }

    private static void loadInitialProviders() {
        ServiceLoader<ProviderInterface> sl =
                ServiceLoader.load(ProviderInterface.class,
                        ClassLoader.getSystemClassLoader());
        Iterator<ProviderInterface> i = sl.iterator();
        for (; ; ) {
            try {
                if (!i.hasNext()) {
                    break;
                }
                ProviderInterface next = i.next();
                String name = next.getClass().getName();
                registerProvider(name, next);
            } catch (ServiceConfigurationError sce) {
                if (sce.getCause() instanceof SecurityException) {
                    // Ignore the security exception, try the next provider
                    continue;
                }
                throw sce;
            }
        }
        if (registeredProviders.isEmpty()){
            registerProvider(DEFAULT_PROVIDER, new DefaultProvider());
        }
    }


    public static void doSomething(String name) {
        ProviderInterface providerInterface = registeredProviders.get(name);
        if (!Optional.ofNullable(providerInterface).isPresent()){
            throw new RuntimeException("provider不存在");
        }
        providerInterface.doSomethting();
    }

    public static void registerProvider(String name, ProviderInterface p) {
        registeredProviders.put(name, p);
    }
}
