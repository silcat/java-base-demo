package designMode;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理设计模式：为其他对象提供一种代理以控制对这个对象的访问。
 * 动态代理：代理类在程序运行时创建的代理方式被成为 动态代理。代理类并不是在Java代码中定义的，而是在运行时根据我们在Java代码中的“指示”动态生成的
 */
public class ProxyPattern {
    public static void main(String[] args) throws IOException {
        Aop aop1 = new Aop();
        Subject subject = (Subject) new AopHandler().bind(aop1);
        subject.doSometing();

    }
    /**
     * 接口Subject
     */
    interface Subject {
        void doSometing();
    }
    /**
     * 要代理的对象（RealSubject）
     */
    static class Aop implements Subject {
        @Override
        public void doSometing() {
            System.out.println("保存数据....");
        }
    }
    /**
     * 代理类（InvocationHandler ）
     */
    public static class AopHandler implements InvocationHandler {
        private Object delegate;

        //运行时创建代理类
        public Object bind(Object delegate) {
            this.delegate = delegate;
            return Proxy.newProxyInstance(
                    this.delegate.getClass().getClassLoader(), this.delegate.getClass().getInterfaces(), this
            );
        }
        //动态代理可以很方便的面向切面编程
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("开启事务...");
            method.invoke(delegate);
            System.out.println("提交事务...");
            return null;

        }
    }
}
