package zookeeper;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import javax.annotation.Resource;


@Slf4j
@Data
public class ZKclient {

    @Resource(name = "zkClient")
    private CuratorFramework client;

    //Zk集群地址
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    public static ZKclient instance = null;


    public CuratorFramework getClient() {
        return client;
    }
    public void destroy() {
        CloseableUtils.closeQuietly(client);
    }


    /**
     * 创建节点
     */
    public void createNode(String zkPath, String data) {
        try {
            // 创建一个 ZNode 节点
            // 节点的数据为 payload
            byte[] payload = "to set content".getBytes("UTF-8");
            if (data != null) {
                payload = data.getBytes("UTF-8");
            }
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除节点
     */
    public void deleteNode(String zkPath) {
        try {
            if (!isNodeExist(zkPath)) {
                return;
            }
            client.delete()
                    .forPath(zkPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查节点
     */
    public boolean isNodeExist(String zkPath) {
        try {

            Stat stat = client.checkExists().forPath(zkPath);
            if (null == stat) {
                return false;
            } else {

                return true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建 临时 顺序 节点
     */
    public String createEphemeralSeqNode(String srcpath) {
        try {

            // 创建一个 ZNode 节点
            String path = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(srcpath);

            return path;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}