package designMode;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理设计模式：为其他对象提供一种代理以控制对这个对象的访问。
**/
public class ProxyPattern {
    public static void main(String[] args) throws IOException {
        //静态代理
        new StaticProxy(new RealSubject()).buyCar();
        //动态代理
        RealSubject realSubject = new RealSubject();
        Subject proxy = (Subject) new ProxyTwo().getProxy(realSubject);
        proxy.buyCar();
        //代理工厂
        Subject proxy1 = (Subject)ProxyFatory.getProxy(new RealSubject());
        //cglib动态代理
        Subject proxyCglib  =(Subject) new ProxyThree().getProxy(realSubject);
        proxyCglib.buyCar();

    }
    /**
     * 静态代理，需要手动创建代理类
     */
    interface Subject {
        void buyCar();
    }
    public static class RealSubject implements Subject {
        @Override
        public void buyCar() {
            System.out.println("用户买车");
        }
    }
    public static class StaticProxy implements Subject {
        private Subject targe;

        public StaticProxy(Subject subject ) {
            targe = subject;
        }

        @Override
        public void buyCar() {
            System.out.println("余额校验");
            targe.buyCar();
            System.out.println("记录日志");
        }
    }
    /**
     * 动态代理:被代理对象必须有接口
     */
    public static class ProxyTwo implements InvocationHandler {
        private Object delegate;

        public ProxyTwo() {
        }

        public ProxyTwo(Object delegate) {
            this.delegate = delegate;
        }

        public Object  getProxy(Object delegate) {
            this.delegate = delegate;
            return java.lang.reflect.Proxy.newProxyInstance(
                    this.delegate.getClass().getClassLoader(), this.delegate.getClass().getInterfaces(), this
            );
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("余额校验");
            method.invoke(delegate,args);
            System.out.println("记录日志");
            return null;

        }
    }
    /**
     * 代理工厂
     */
    public static class ProxyFatory {
        public static Object getProxy(Object target) {
            return Proxy.newProxyInstance(
                    target.getClass().getClassLoader(), // 目标类的类加载
                    target.getClass().getInterfaces(),  // 代理需要实现的接口，可指定多个
                    new ProxyTwo(target)   // 代理对象对应的自定义 InvocationHandler
            );
        }
    }
    /**
     * cglib代理:代理类被标记成final，无法通过CGLIB去创建动态代理。
     */
    public static class ProxyThree  {
        Object obj;
        public Object getProxy(final Object target)
        {
            this.obj = target;
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(obj.getClass());
            enhancer.setCallback(new MethodInterceptor() {
                                     @Override
                                     public Object intercept(Object obj, Method method, Object[] args,
                                                             MethodProxy proxy) throws Throwable
                                     {
                                         System.out.println("余额校验");
                                         Object object = proxy.invokeSuper(obj, args);
                                         System.out.println("记录日志");
                                         return object;
                                     }
                                 }
            );
            return enhancer.create();
        }
    }
}
