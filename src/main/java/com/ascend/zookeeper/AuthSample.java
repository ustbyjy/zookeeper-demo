package com.ascend.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthSample implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(AuthSample.class);

    final static String PATH = "/zk-book-auth_test";

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper1 = new ZooKeeper("vhost1:2181", 50000, new AuthSample());
        zooKeeper1.addAuthInfo("digest", "foo:true".getBytes());
        zooKeeper1.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        try {
            logger.info("==========================================================================");
            ZooKeeper zooKeeper2 = new ZooKeeper("vhost1:2181", 50000, new AuthSample());
            zooKeeper2.addAuthInfo("digest", "foo:true".getBytes());
            logger.info(new String(zooKeeper2.getData(PATH, true, new Stat())));
        } catch (Exception e) {
            logger.error("zooKeeper2", e);
        }

        try {
            logger.info("==========================================================================");
            ZooKeeper zooKeeper3 = new ZooKeeper("vhost1:2181", 50000, new AuthSample());
            logger.info(new String(zooKeeper3.getData(PATH, true, new Stat())));
        } catch (Exception e) {
            logger.error("zooKeeper3", e);
        }

        try {
            logger.info("==========================================================================");
            ZooKeeper zooKeeper4 = new ZooKeeper("vhost1:2181", 50000, new AuthSample());
            zooKeeper4.addAuthInfo("digest", "foo:false".getBytes());
            logger.info(new String(zooKeeper4.getData(PATH, true, new Stat())));
        } catch (Exception e) {
            logger.error("zooKeeper4", e);
        }

    }

    public void process(WatchedEvent event) {
        logger.info(event.toString());
    }
}
