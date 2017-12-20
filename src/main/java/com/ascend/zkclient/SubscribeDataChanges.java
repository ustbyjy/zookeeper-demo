package com.ascend.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SubscribeDataChanges {
    private static Logger logger = LoggerFactory.getLogger(SubscribeDataChanges.class);

    private static class ZkDataListener implements IZkDataListener {

        public void handleDataChange(String dataPath, Object data) throws Exception {
            logger.info("dataPath：" + dataPath);
            logger.info("data：" + data.toString());
        }

        public void handleDataDeleted(String dataPath) throws Exception {
            logger.info("dataPath：" + dataPath);
        }
    }

    public static void main(String[] args) throws IOException {
        ZkClient zkClient = new ZkClient("10.236.40.159:2181", 10000, 10000, new BytesPushThroughSerializer());
        logger.info("connect ok！");

        zkClient.subscribeDataChanges("/node_1", new ZkDataListener());

        System.in.read();
    }
}
