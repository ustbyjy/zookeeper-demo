package com.ascend.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteData {
    private static Logger logger = LoggerFactory.getLogger(WriteData.class);

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("10.236.40.159:2181", 10000, 10000, new SerializableSerializer());
        logger.info("connect ok！");

        User user = new User(1, "test");
        zkClient.writeData("/user1", user, -1);
    }
}
