package com.ascend.curator;

import com.ascend.util.PropertiesUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheNode会缓存本地数据，若连续对节点进行操作，通知事件会比较新旧节点的数据，有可能会丢失事件，因此连续操作的中间可以休眠一定的时间避免丢失事件。
 */
public class CreateNode {
    private static Logger logger = LoggerFactory.getLogger(CreateNode.class);

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(PropertiesUtil.getStringValue("connectString"))
                .connectionTimeoutMs(Integer.parseInt(PropertiesUtil.getStringValue("sessionTimeout")))
                .sessionTimeoutMs(Integer.parseInt(PropertiesUtil.getStringValue("connectionTimeout")))
                .retryPolicy(retryPolicy)
                .build();

        // 建立连接
        client.start();

        final NodeCache nodeCache = new NodeCache(client, "/jike/1/11/111");
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                if (nodeCache.getCurrentData() != null) {
                    logger.info("node changed，data={}", new String(nodeCache.getCurrentData().getData()));
                } else {
                    logger.info("node deleted");
                }
            }
        });

        client.create()
                .creatingParentsIfNeeded() // 如果没有父节点，则创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/jike/1/11/111", "init".getBytes());

//        Thread.sleep(100);
        client.setData().forPath("/jike/1/11/111", "update1".getBytes());

//        Thread.sleep(100);
        client.setData().forPath("/jike/1/11/111", "update2".getBytes());

//        Thread.sleep(100);
        client.delete().forPath("/jike/1/11/111");

        System.in.read();
    }

}
