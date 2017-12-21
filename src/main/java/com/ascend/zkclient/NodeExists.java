package com.ascend.zkclient;

import com.ascend.util.PropertiesUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeExists {
    private static Logger logger = LoggerFactory.getLogger(NodeExists.class);

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), PropertiesUtil.getIntValue("connectionTimeout"), new SerializableSerializer());
        logger.info("connect ok！");

        boolean result = zkClient.exists("/user1");
        logger.info("/user1 exists：" + result);
    }
}
