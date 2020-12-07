package zookeeper.cure;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by yangtianfeng on 2020/12/6.
 */

public class Api implements IApi {
    private static final Logger log = LoggerFactory.getLogger(Api.class);
    private final static String ZK_ADDRESS="127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    @Override
    public void createNode() {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            // 创建一个 ZNode 节点
            // 节点的数据为 payload

            String data = "hello";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/CRUD/node-1";
            client.create()
                    .creatingParentsIfNeeded()
                    .forPath(zkPath, payload);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Override
    public void readNode(String path) {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            String zkPath = "/test/CRUD/node-1";


            Stat stat = client.checkExists().forPath(zkPath);
            if (null != stat) {
                //读取节点的数据
                byte[] payload = client.getData().forPath(zkPath);
                String data = new String(payload, "UTF-8");
                log.info("read data:", data);

                String parentPath = "/test";
                List<String> children = client.getChildren().forPath(parentPath);

                for (String child : children) {
                    log.info("child:", child);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }

    }

    @Override
    public void updateNode(String path) {
//创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();


            String data = "hello world";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/node-1";
            client.setData()
                    .forPath(zkPath, payload);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Override
    public void updateNodeAsync(String path) {

    }

    @Override
    public void deleteNode(String path) {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            //删除节点
            String zkPath = "/test/CRUD/node-1";
            client.delete().forPath(zkPath);


            //删除后查看结果
            String parentPath = "/test";
            List<String> children = client.getChildren().forPath(parentPath);

            for (String child : children) {
                log.info("child:", child);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
