package com.ascend.zookeeper;

import com.ascend.util.PropertiesUtil;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class GetChildrenSync implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(GetChildrenSync.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), new CreateNodeAsync());
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
        try {
            List<String> children = zooKeeper.getChildren("/", true);
            logger.info("path：/，children：" + children);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
