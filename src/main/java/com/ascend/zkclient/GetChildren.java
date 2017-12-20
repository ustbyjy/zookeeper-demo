package com.ascend.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetChildren {
    private static Logger logger = LoggerFactory.getLogger(GetChildren.class);

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("10.236.40.159:2181", 10000, 10000, new SerializableSerializer());
        logger.info("connect ok！");

        List<String> children = zkClient.getChildren("/node_1");
        logger.info("children：" + children);
    }
}
