package designMode.objectStructural;

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
        new Proxy(new RealSubject()).buyCar();
        //动态代理
        RealSubject realSubject = new RealSubject();
        Subject proxy = (Subject) new ProxyTwo().bind(realSubject);
        proxy.buyCar();
        //cglib动态代理
        Subject proxyCglib  =(Subject) new ProxyThree().bind(realSubject);
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
    public static class Proxy implements Subject {
        private Subject targe;

        public Proxy(Subject subject ) {
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

        public Object bind(Object delegate) {
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
     * cglib代理:代理类被标记成final，无法通过CGLIB去创建动态代理。
     */
    public static class ProxyThree  {
        Object obj;
        public Object bind(final Object target)
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
                                         Object res = method.invoke(target, args);
                                         System.out.println("记录日志");
                                         return res;
                                     }
                                 }
            );
            return enhancer.create();
        }
    }
}
