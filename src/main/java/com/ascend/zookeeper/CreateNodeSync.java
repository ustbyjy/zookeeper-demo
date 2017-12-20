package com.ascend.zookeeper;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CreateNodeSync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(CreateNodeSync.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper("10.236.40.159:2181", 5000, new CreateNodeSync());
        // 阻碍主线程退出
        System.in.read();
    }

    public void process(WatchedEvent event) {
        logger.info("收到事件：" + event);
        if (event.getState() == Event.KeeperState.SyncConnected) {
            doSomething(zooKeeper);
        }
    }

    private void doSomething(ZooKeeper zooKeeper) {
        logger.info("doSomething");
        try {
            String path = zooKeeper.create("/node_4", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("return path：" + path);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
