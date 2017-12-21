package com.ascend.curator;

import com.ascend.util.PropertiesUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CreateSession {
    private static Logger logger = LoggerFactory.getLogger(CreateSession.class);

    public static void main(String[] args) throws IOException {
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        RetryPolicy retryPolicy = new RetryNTimes(5, 1000);
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

        // 1、构造器创建客户端
//        CuratorFramework client = CuratorFrameworkFactory.newClient(PropertiesUtil.getStringValue("connectString"), Integer.parseInt(PropertiesUtil.getStringValue("sessionTimeout")), Integer.parseInt(PropertiesUtil.getStringValue("connectionTimeout")), retryPolicy);
        // 2、builder模式创建客户端
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(PropertiesUtil.getStringValue("connectString"))
                .connectionTimeoutMs(Integer.parseInt(PropertiesUtil.getStringValue("sessionTimeout")))
                .sessionTimeoutMs(Integer.parseInt(PropertiesUtil.getStringValue("connectionTimeout")))
                .retryPolicy(retryPolicy)
                .build();

        // 建立连接
        client.start();

        System.in.read();
    }

}
