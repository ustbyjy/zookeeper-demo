package com.ascend;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NodeExistsAsync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(NodeExistsAsync.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper("10.236.40.159:2181", 5000, new NodeExistsAsync());
        // 阻碍主线程退出
        System.in.read();
    }

    private void doSomething(ZooKeeper zooKeeper) {
        logger.info("doSomething");
        NodeExistsAsync.zooKeeper.exists("/node_1", true, new IStateCallback(), null);
    }

    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(zooKeeper);
            } else {
                try {
                    if (event.getType() == EventType.NodeCreated) {
                        logger.info(event.getPath() + " created");
                        zooKeeper.exists(event.getPath(), true, new IStateCallback(), null);
                    } else if (event.getType() == EventType.NodeDataChanged) {
                        logger.info(event.getPath() + " updated");
                        zooKeeper.exists(event.getPath(), true, new IStateCallback(), null);
                    } else if (event.getType() == EventType.NodeDeleted) {
                        logger.info(event.getPath() + " deleted");
                        zooKeeper.exists(event.getPath(), true, new IStateCallback(), null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class IStateCallback implements AsyncCallback.StatCallback {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            logger.info("rc：" + rc);
            logger.info("path：" + path);
            logger.info("ctx：" + ctx);
        }
    }

}
