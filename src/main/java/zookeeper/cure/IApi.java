package zookeeper.cure;

/**
 * Created by yangtianfeng on 2020/12/6.
 */
public interface IApi {
    public void createNode();
    public void readNode(String path);
    public void updateNode(String path);
    public void updateNodeAsync(String path);
    public void deleteNode(String path);
}
