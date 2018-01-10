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


public class GetDataAsync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(GetDataAsync.class);
    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), new GetDataAsync());
        // 阻碍主线程退出
        System.in.read();
    }

    private void doSomething(ZooKeeper zooKeeper) {
        logger.info("doSomething");
        zooKeeper.getData("/node_4", true, new IDataCallback(), null);
    }

    public void process(WatchedEvent event) {
        logger.info("收到事件：" + event);
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(zooKeeper);
            } else {
                if (event.getType() == EventType.NodeDataChanged) {
                    zooKeeper.getData(event.getPath(), true, new IDataCallback(), null);
                }
            }
        }
    }

    static class IDataCallback implements AsyncCallback.DataCallback {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            logger.info("rc：" + rc);
            logger.info("path：" + path);
            logger.info("ctx：" + ctx);
            logger.info("data：" + new String(data));
            logger.info("stat：" + stat);
        }
    }

}
