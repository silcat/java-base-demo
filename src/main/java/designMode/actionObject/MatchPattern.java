package designMode.actionObject;

import lombok.AllArgsConstructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;


/**
 *
 */
public class MatchPattern {
    public static void main(String[] args)  {
        ClosedIntervalPredicate closedIntervalPredicate = new ClosedIntervalPredicate();
        boolean close = BasePattern.matches("[5,9]", "4", "close");
    }
    public static class ClosedIntervalPredicate implements Predicate<PredicateConfig>{
        static {
            try {
                BasePattern.regist("close",new ClosedIntervalPredicate());
            } catch (Exception var1) {
                throw var1;
            }
        }
        @Override
        public boolean test(PredicateConfig predicateConfig) {
            String expression = predicateConfig.expression;
            String targe = predicateConfig.targe;
            //这个可以单独抽出做完解释器处理
            String[] split = expression.replace("[", "").replace("]", "").split(",");
               if (Integer.valueOf(split[0])> Integer.valueOf(targe)){
                   return true;
               }
            return false;
        }
    }
    @AllArgsConstructor
    public static class PredicateConfig {
        private String expression;
        private String targe;
    }

    public static class BasePattern{
        public static Map<String,Predicate<PredicateConfig>> predicateMap =new ConcurrentHashMap<>();
        public static boolean matches(String expression, String value, String type) {
            PredicateConfig predicateConfig = new PredicateConfig(expression, value);
            Predicate predicate = predicateMap.get(type);
            return predicate.test(predicateConfig);
        }

        public static void regist(String name, Predicate<PredicateConfig> predicate){
            predicateMap.put(name, predicate);
        }
    }

}