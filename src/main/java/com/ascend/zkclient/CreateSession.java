package com.ascend.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateSession {
    private static Logger logger = LoggerFactory.getLogger(CreateSession.class);

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("10.236.40.159:2181", 10000, 10000, new SerializableSerializer());
        logger.info("connect ok！");

        User user = new User(1, "test");
        String path = zkClient.create("/user1", user, CreateMode.PERSISTENT);
        logger.info("return path：" + path);
    }
}
