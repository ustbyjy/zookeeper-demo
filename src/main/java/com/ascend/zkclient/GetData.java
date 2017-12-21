package com.ascend.zkclient;

import com.ascend.util.PropertiesUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetData {
    private static Logger logger = LoggerFactory.getLogger(GetData.class);

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), PropertiesUtil.getIntValue("connectionTimeout"), new SerializableSerializer());
        logger.info("connect ok！");

        Stat stat = new Stat();
        User user = zkClient.readData("/user1", stat);
        logger.info("user：" + user);
        logger.info("stat：" + stat);
    }
}
