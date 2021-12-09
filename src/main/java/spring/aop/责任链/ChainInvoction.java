package spring.aop.责任链;

import com.google.common.collect.Lists;
import java.util.List;


public class ChainInvoction {
    private List<Invoction> invocations= Lists.newArrayList(new AfterInvoction(),new BeforeInvoction());
    private int currentInterceptorIndex = -1;


    public String process() {
        if (this.currentInterceptorIndex == this.invocations.size() - 1) {
            System.out.println("执行目标方法");
            return "执行目标方法";
        }
        Invoction invocation = (Invoction)this.invocations.get(++this.currentInterceptorIndex);
        return invocation.invoke(this);
    }


}

