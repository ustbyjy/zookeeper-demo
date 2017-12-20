package com.ascend.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class GetChildrenAsync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(GetChildrenAsync.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper("10.236.40.159:2181", 5000, new GetChildrenAsync());
        // 阻碍主线程退出
        System.in.read();
    }

    public void process(WatchedEvent event) {
        logger.info("收到事件：" + event);
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && event.getPath() == null) {
                doSomething(zooKeeper);
            } else {
                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    try {
                        List<String> children = zooKeeper.getChildren(event.getPath(), true);
                        logger.info("path：" + event.getPath() + " children changed，" + "children：" + children);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void doSomething(ZooKeeper zooKeeper) {
        logger.info("doSomething");
        zooKeeper.getChildren("/", true, new IChildren2Callback(), null);
    }

    static class IChildren2Callback implements AsyncCallback.Children2Callback {
        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
            logger.info("rc：" + rc + "，path：" + path + "，ctx：" + ctx + "，children：" + children + "，stat：" + stat);
        }
    }
}
