package com.ascend.curator;

import com.ascend.util.PropertiesUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckExists {
    private static Logger logger = LoggerFactory.getLogger(CheckExists.class);

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(PropertiesUtil.getStringValue("connectString"))
                .connectionTimeoutMs(Integer.parseInt(PropertiesUtil.getStringValue("sessionTimeout")))
                .sessionTimeoutMs(Integer.parseInt(PropertiesUtil.getStringValue("connectionTimeout")))
                .retryPolicy(retryPolicy)
                .build();

        // 建立连接
        client.start();

        Stat stat = client.checkExists()
                .forPath("/jike");

        logger.info("stat：" + stat);

        System.in.read();
    }

}
