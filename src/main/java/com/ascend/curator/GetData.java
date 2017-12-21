package com.ascend.curator;

import com.ascend.util.PropertiesUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetChildren {
    private static Logger logger = LoggerFactory.getLogger(GetChildren.class);

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

        List<String> children = client.getChildren()
                .forPath("/jike");

        logger.info("children：" + children);

        System.in.read();
    }

}
