package com.ascend.zookeeper;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CreateNodeAsync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(CreateNodeAsync.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper("10.236.40.159:2181", 5000, new CreateNodeAsync());
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
        zooKeeper.create("/node_6", "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new IStringCallback(), "create");
    }

    static class IStringCallback implements AsyncCallback, AsyncCallback.StringCallback {
        public void processResult(int rc, String path, Object ctx, String name) {
            logger.info("rc：" + rc);
            logger.info("path：" + path);
            logger.info("ctx：" + ctx);
            logger.info("name：" + name);
        }
    }
}
