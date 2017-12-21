package com.ascend.zkclient;

import com.ascend.util.PropertiesUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteNode {
    private static Logger logger = LoggerFactory.getLogger(DeleteNode.class);

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), PropertiesUtil.getIntValue("connectionTimeout"), new SerializableSerializer());
        logger.info("connect ok！");

        boolean result1 = zkClient.delete("/user1");
        boolean result2 = zkClient.deleteRecursive("/node_1");
        logger.info("/user1 delete result：" + result1);
        logger.info("/node_1 delete result：" + result2);
    }
}
