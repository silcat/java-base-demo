package designMode.actionObject;

import org.redisson.client.RedisClient;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板方法：父类定义骨架，子类实现方法
 */
public class TempletePattern {
    public static void main(String[] args)  {
        String setting = new LocalSetting().getSetting("");
        String string = new RedisSetting().getSetting("");
    }
    public static   abstract class AbstractSetting {
        public final String getSetting(String key) {
            String value = lookupCache(key);
            if (value == null) {
                System.out.println("数据库查数据");
                putIntoCache(key, value);
            }
            return value;
        }

        protected abstract String lookupCache(String key);

        protected abstract void putIntoCache(String key, String value);
    }
    public static class LocalSetting extends AbstractSetting {
        private Map<String, String> cache = new HashMap<>();

        @Override
        protected String lookupCache(String key) {
            return cache.get(key);
        }
        @Override
        protected void putIntoCache(String key, String value) {
            cache.put(key, value);
        }
    }
    public static class RedisSetting extends AbstractSetting {
        private RedisClient client = RedisClient.create(null);
        @Override
        protected String lookupCache(String key) {
            System.out.println("redis查缓存");
            return "";
        }
        @Override
        protected void putIntoCache(String key, String value) {
            System.out.println("redis放缓存");

        }
    }
}