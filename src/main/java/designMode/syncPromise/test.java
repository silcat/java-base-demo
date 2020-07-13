package designMode.syncPromise;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;




public class test {

    public static void main(String[] args)  {
        ApplicationContext factory = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        System.out.println("容器初始化成功");
        //得到Preson，并使用
        IDemoAsyncExecutor demoAsyncExecutor = factory.getBean("demoAsyncExecutor",IDemoAsyncExecutor.class);
        IPromise<String> test = demoAsyncExecutor.computeAsync("test");
        String s = demoAsyncExecutor.get(test);
        System.out.print(s);

    }
}
