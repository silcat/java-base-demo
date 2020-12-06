package zookeeper.example;


/**
 * Created by yangtianfeng on 2020/12/6.
 */
public class Test {
    public static void main(String[] args)  {
        IApi api = new Api();
        api.createNode();
        api.createNode();
    }
}
