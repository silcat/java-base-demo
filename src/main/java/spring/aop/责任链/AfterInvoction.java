package spring.aop.责任链;
import spring.aop.BaseAdvice;

public class AfterInvoction implements Invoction {
   private final BaseAdvice advice = new BaseAdvice();

   @Override
   public String invoke(ChainInvoction invoction){
      String process = invoction.process();
      advice.afer();
      return process;
   }


}

