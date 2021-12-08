#Spring AOP概念
* Aspect : 切面由切入点和通知组成
* JoinPoint :切入点
* PointCut ：简单来说，连接点就是被拦截到的程序执行点，因为Spring只支持方法类型的连接点，所以在Spring中连接点就是被拦截到的方法。
* advice ：通知是指拦截到连接点之后要执行的代码，包括了“around”、“before”和“after”等不同类型的通知
* advisor :Advisor由切入点和Advice组成。

#Spring AOP通知顺序
##单个切面
* 正常情况：环绕前置=====@Before=====目标方法执行=====@AfterReturning=====@After=====环绕返回=====环绕最终
* 异常情况：环绕前置=====@Before=====目标方法执行=====@AfterThrowing=====@After=====环绕异常=====环绕最终
````
@Around(value = "myPointCut()")
    public Object myAround(ProceedingJoinPoint proceedingJoinPoint)
    {
        Object[] args = proceedingJoinPoint.getArgs();
        Object result=null;
        try {
            //前置通知@Before
            System.out.println("环绕前置通知");
            //目标方法执行
            result = proceedingJoinPoint.proceed(args);
            //环绕返回通知@AfterReturning
            System.out.println("环绕返回通知");
        } catch (Throwable throwable) {
            //环绕异常通知@AfterThrowing
            System.out.println("环绕异常通知");
            throw new RuntimeException(throwable);
        } finally {
            //最终通知@After
            System.out.println("环绕最终通知");
        }
        return result;
    }
````
##多个切面
* 正常情况：切面1环绕前置===切面1@Before===切面2环绕前置===切面2@Before===目标方法执行===切面2@AfterReturning===切面2@After===切面2环绕返回===切面2环绕最终===切面1@AfterReturning===切面1@After===切面1环绕返回===切面1环绕最终
* 异常情况：切面1环绕前置===切面1@Before===切面2环绕前置===切面2@Before===目标方法执行===切面2@AfterThrowing===切面2@After===切面2环绕异常===切面2环绕最终===切面1@AfterThrowing===切面1@After===切面1环绕异常===切面1环绕最终
````
@Order(value = 1)
@Aspect
@Component
public class BookServiceProxy {}
 
@Order(value = 0)
@Aspect
@Component
public class BookServiceProxy2 {}
 
切面二：环绕前置通知
切面二：@Before
切面一：环绕前置通知
切面一：@Before
目标方法执行
切面一：@AfterReturning
切面一：@After
切面一：环绕返回通知
切面一：环绕最终通知
切面二：@AfterReturning
切面二：@After
切面二：环绕返回通知
切面二：环绕最终通知
````
##spring aop实现
* https://blog.csdn.net/litianxiang_kaola/article/details/85335700
https://blog.csdn.net/f641385712/article/details/88925478
https://www.cnblogs.com/dennyzhangdd/p/9602673.html