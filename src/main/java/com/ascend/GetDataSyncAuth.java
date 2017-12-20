package com.ascend;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GetDataSyncAuth implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(GetDataSyncAuth.class);
    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper("10.236.40.159:2181", 5000, new GetDataSyncAuth());
        // 阻碍主线程退出
        System.in.read();
    }

    private void doSomething(ZooKeeper zooKeeper) {
        zooKeeper.addAuthInfo("digest", "jike:123456".getBytes());
        try {
            logger.info("data：" + new String(zooKeeper.getData("/node_4", true, stat)));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(zooKeeper);
            } else {
                if (event.getType() == EventType.NodeDataChanged) {
                    try {
                        logger.info("data：" + new String(zooKeeper.getData(event.getPath(), true, stat)));
                        logger.info("stat：" + stat);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
