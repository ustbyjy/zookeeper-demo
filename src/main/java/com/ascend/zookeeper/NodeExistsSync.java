package com.ascend.zookeeper;

import com.ascend.util.PropertiesUtil;
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

public class NodeExistsSync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(NodeExistsSync.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), new CreateNodeAsync());
        // 阻碍主线程退出
        System.in.read();
    }

    private void doSomething(ZooKeeper zooKeeper) {
        logger.info("doSomething");
        Stat stat = null;
        try {
            stat = zooKeeper.exists("/node_1", true);
            logger.info("stat：" + stat);
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
                try {
                    if (event.getType() == EventType.NodeCreated) {
                        logger.info(event.getPath() + " created");
                        logger.info("stat：" + zooKeeper.exists(event.getPath(), true));
                    } else if (event.getType() == EventType.NodeDataChanged) {
                        logger.info(event.getPath() + " updated");
                        logger.info("stat：" + zooKeeper.exists(event.getPath(), true));
                    } else if (event.getType() == EventType.NodeDeleted) {
                        logger.info(event.getPath() + " deleted");
                        logger.info("stat：" + zooKeeper.exists(event.getPath(), true));
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
