package zookeeper.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class zookeeperConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(zookeeperConfiguration.class);

    @Value("${zookeeper.address}")
    private  String connectString;

    //根目录
    @Value("${zookeeper.namepace}")
    private  String namespace;


    @Bean(name = "zkClient")
    public CuratorFramework zkClient(){
        // 重试策略:第一次重试等待1s，第二次重试等待2s，第三次重试等待4s
        // 第一个参数：等待时间的基础单位，单位为毫秒
        // 第二个参数：最大重试次数
        ExponentialBackoffRetry retryPolicy =
                new ExponentialBackoffRetry(1000, 3);
        //创建权限管理器
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl;
            @Override public List<ACL> getDefaultAcl() {
                if (acl == null) {
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL; //初始化
                    acl.clear();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", "username" + ":" + "password")));//添加
                    this.acl = acl;
                }
                return acl;
            }
            @Override public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };

        CuratorFramework curatorFramework =
                CuratorFrameworkFactory.builder()
                        .aclProvider(aclProvider)
                        .connectString(connectString)
                        .authorization("digest", ("username" + ":" + "password").getBytes()) //使用用户名/密码进行连接
                        .retryPolicy(new ExponentialBackoffRetry(100, 6))  //重试策略
                        .build();
        curatorFramework.start();

        try {
            curatorFramework.blockUntilConnected(3, TimeUnit.SECONDS); //阻塞判断连接成功，超时认为失败。
            if (curatorFramework.getZookeeperClient().isConnected()) {
                return curatorFramework.usingNamespace(namespace); //返回连接，起始根目录为namespace。
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        // fail situation
        curatorFramework.close();
        throw new RuntimeException("failed to connect to zookeeper service : " + connectString);

    }


}