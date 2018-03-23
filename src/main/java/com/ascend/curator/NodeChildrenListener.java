package com.ascend.curator;

import com.ascend.util.PropertiesUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeChildrenListener {
    private static Logger logger = LoggerFactory.getLogger(NodeChildrenListener.class);

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

        final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/jike", true);
        pathChildrenCache.start();
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData currentData = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        logger.info("CHILD_ADDED");
                        logger.info("path：" + currentData.getPath());
                        logger.info("data：" + new String(currentData.getData()));
                        logger.info("stat：" + currentData.getStat());
                        break;
                    }
                    case CHILD_REMOVED: {
                        logger.info("CHILD_REMOVED");
                        logger.info("path：" + currentData.getPath());
                        logger.info("data：" + new String(currentData.getData()));
                        logger.info("stat：" + currentData.getStat());
                        break;
                    }
                    case CHILD_UPDATED: {
                        logger.info("CHILD_UPDATED");
                        logger.info("path：" + currentData.getPath());
                        logger.info("data：" + new String(currentData.getData()));
                        logger.info("stat：" + currentData.getStat());
                        break;
                    }
                    case CONNECTION_RECONNECTED: {
                        logger.info("CONNECTION_RECONNECTED");
                        break;
                    }
                    case CONNECTION_SUSPENDED: {
                        logger.info("CONNECTION_SUSPENDED");
                        break;
                    }
                    case CONNECTION_LOST: {
                        logger.info("CONNECTION_LOST");
                        break;
                    }
                }
            }
        });

//        client.create()
//                .creatingParentsIfNeeded()
//                .withMode(CreateMode.EPHEMERAL)
//                .forPath("/jike/1", "init".getBytes());

        System.in.read();
    }

}
