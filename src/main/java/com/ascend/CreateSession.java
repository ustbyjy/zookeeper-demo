package com.ascend;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CreateSession implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(CreateSession.class);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zooKeeper = new ZooKeeper("10.236.40.159:2181", 5000, new CreateSession());
        // 异步建立连接，所以状态为CONNECTING
        logger.info("state：" + zooKeeper.getState());
        Thread.sleep(3000);
        // 等待建立连接后，状态为CONNECTED
        logger.info("next state：" + zooKeeper.getState());
    }

    public void process(WatchedEvent event) {
        logger.info("收到事件：" + event);
    }
}
