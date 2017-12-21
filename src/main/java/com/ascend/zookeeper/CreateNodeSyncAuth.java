package com.ascend.zookeeper;

import com.ascend.util.PropertiesUtil;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class CreateNodeSyncAuth implements Watcher {
    private static Logger logger = LoggerFactory.getLogger(CreateNodeSyncAuth.class);
    private static ZooKeeper zooKeeper;
    private static boolean somethingDone = false;

    public static void main(String[] args) throws IOException {
        zooKeeper = new ZooKeeper(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), new CreateNodeAsync());
        // 阻碍主线程退出
        System.in.read();
    }

    /**
     * 可以在命令行执行：addauth digest jeke:123456，授权访问
     */
    private void doSomething() {
        logger.info("doSomething");
        try {
            ACL aclIp = new ACL(Perms.READ, new Id("ip", "10.236.40.119"));
            ACL aclDigest = new ACL(Perms.READ | Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("jike:123456")));
            ArrayList<ACL> aclList = new ArrayList<ACL>();
            aclList.add(aclDigest);
            aclList.add(aclIp);
            String path = zooKeeper.create("/node_9", "123".getBytes(), aclList, CreateMode.PERSISTENT);
            logger.info("return path:" + path);
            somethingDone = true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent event) {
        System.out.println("event：" + event);
        if (event.getState() == KeeperState.SyncConnected) {
            if (!somethingDone && event.getType() == EventType.None && null == event.getPath()) {
                doSomething();
            }
        }
    }

}
