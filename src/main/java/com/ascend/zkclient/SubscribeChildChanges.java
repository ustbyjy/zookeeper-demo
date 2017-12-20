package com.ascend.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class SubscribeChildChanges {
    private static Logger logger = LoggerFactory.getLogger(SubscribeChildChanges.class);

    private static class ZkChildListener implements IZkChildListener {

        public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
            logger.info("parentPath：" + parentPath);
            logger.info("currentChilds：" + currentChilds);
        }
    }

    public static void main(String[] args) throws IOException {
        ZkClient zkClient = new ZkClient("10.236.40.159:2181", 10000, 10000, new SerializableSerializer());
        logger.info("connect ok！");

        zkClient.subscribeChildChanges("/node_1", new ZkChildListener());

        System.in.read();
    }
}
