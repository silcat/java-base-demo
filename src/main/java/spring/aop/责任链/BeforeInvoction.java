package spring.aop.责任链;
import spring.aop.BaseAdvice;

public class BeforeInvoction implements Invoction {
   private final BaseAdvice advice = new BaseAdvice();

   @Override
   public String invoke(ChainInvoction invoction){
      advice.before();
      return invoction.process();
   }


}

