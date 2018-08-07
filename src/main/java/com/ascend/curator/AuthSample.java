package com.ascend.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * CacheNode会缓存本地数据，若连续对节点进行操作，通知事件会比较新旧节点的数据，有可能会丢失事件，因此连续操作的中间可以休眠一定的时间避免丢失事件。
 */
public class AuthSample {
    private static Logger logger = LoggerFactory.getLogger(AuthSample.class);

    private static String path = "/curator-auth/2/3";

    public static void main(String[] args) throws Exception {
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> aclList;

            public List<ACL> getDefaultAcl() {
                if (aclList == null) {
                    aclList = new ArrayList<ACL>();
                    aclList.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", "foo:true")));
                }
                return aclList;
            }

            public List<ACL> getAclForPath(String path) {
                return aclList;
            }
        };
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .aclProvider(aclProvider)
                .authorization("digest", "foo:true".getBytes())
                .connectString("vhost1:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        // 建立连接
        client.start();
        client.create()
                .creatingParentsIfNeeded() // 如果没有父节点，则创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .withACL(aclProvider.getDefaultAcl(), true)
                .forPath(path, "init".getBytes());
        String data = new String(client.getData().forPath(path));
        logger.info("data={}", data);

        logger.info("============================================================================================");
        Thread.sleep(3000);

        CuratorFramework client2 = CuratorFrameworkFactory
                .builder()
                .authorization("digest", "foo:true".getBytes())
                .connectString("vhost1:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        // 建立连接
        client2.start();
        data = new String(client2.getData().forPath(path));
        logger.info("data={}", data);

        logger.info("============================================================================================");
        Thread.sleep(3000);

        CuratorFramework client3 = CuratorFrameworkFactory
                .builder()
                .connectString("vhost1:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        // 建立连接
        client3.start();
        data = new String(client3.getData().forPath(path));
        logger.info("data={}", data);

        System.in.read();
    }

}
