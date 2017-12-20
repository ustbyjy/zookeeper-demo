package com.ascend.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DeleteNodeAsync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(DeleteNodeAsync.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper("10.236.40.159:2181", 5000, new DeleteNodeAsync());
        // 阻碍主线程退出
        System.in.read();
    }

    private void doSomething(WatchedEvent event) {
        logger.info("doSomething");
        zooKeeper.delete("/node_5", -1, new IVoidCallback(), null);
    }

    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(event);
            }
        }
    }

    static class IVoidCallback implements AsyncCallback.VoidCallback {
        public void processResult(int rc, String path, Object ctx) {
            logger.info("rc：" + rc);
            logger.info("path：" + path);
            logger.info("ctx：" + ctx);
        }
    }

}
