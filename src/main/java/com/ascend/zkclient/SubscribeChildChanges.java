package com.ascend.zkclient;

import com.ascend.util.PropertiesUtil;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class SubscribeChildChanges {
    private static Logger logger = LoggerFactory.getLogger(SubscribeChildChanges.class);

    public static void main(String[] args) throws IOException {
        ZkClient zkClient = new ZkClient(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), PropertiesUtil.getIntValue("connectionTimeout"), new SerializableSerializer());
        logger.info("connect ok！");

        zkClient.subscribeChildChanges("/node_1", new ZkChildListener());

        System.in.read();
    }

    private static class ZkChildListener implements IZkChildListener {

        public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
            logger.info("parentPath：" + parentPath);
            logger.info("currentChilds：" + currentChilds);
        }
    }
}
