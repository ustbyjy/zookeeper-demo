package com.ascend.zookeeper;

import com.ascend.util.PropertiesUtil;
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

public class UpdateNodeAsync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(UpdateNodeAsync.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), new UpdateNodeAsync());
        // 阻碍主线程退出
        System.in.read();
    }

    private void doSomething(ZooKeeper zooKeeper) {
        logger.info("doSomething");
        zooKeeper.setData("/node_4", "234".getBytes(), -1, new IStatCallback(), null);
    }

    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(zooKeeper);
            }
        }
    }

    static class IStatCallback implements AsyncCallback.StatCallback {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            logger.info("rc：" + rc);
            logger.info("path：" + path);
            logger.info("ctx：" + ctx);
            logger.info("stat：" + stat);
        }
    }

}
