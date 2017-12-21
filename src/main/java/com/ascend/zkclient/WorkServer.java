package com.ascend.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkServer {
    private static Logger logger = LoggerFactory.getLogger(WorkServer.class);

    private static final String MASTER_PATH = "/master";

    private volatile boolean running = false;

    private ZkClient zkClient;

    private IZkDataListener dataListener;

    private RunningData serverData;

    private RunningData masterData;

    private ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(1);

    private int delayTime = 5;

    public WorkServer(RunningData rd) {
        this.serverData = rd;
        this.dataListener = new IZkDataListener() {
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            public void handleDataDeleted(String dataPath) throws Exception {
                if (masterData != null && masterData.getName().equals(serverData.getName())) {
                    takeMaster();
                } else {
                    delayExecutor.schedule(new Runnable() {
                        public void run() {
                            takeMaster();
                        }
                    }, delayTime, TimeUnit.SECONDS);
                }
            }
        };
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public void start() throws Exception {
        logger.info("start");
        if (running) {
            throw new Exception("server has startup...");
        }
        running = true;
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
        takeMaster();
    }

    public void stop() throws Exception {
        logger.info("stop");
        if (!running) {
            throw new Exception("server has stopped...");
        }
        running = false;
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
        releaseMaster();
    }

    private void takeMaster() {
        logger.info("takeMaster");
        if (!running) {
            return;
        }
        try {
            zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
            masterData = serverData;
            logger.info(serverData.getName() + " is master");
            delayExecutor.schedule(new Runnable() {
                public void run() {
                    if (checkMaster()) {
                        releaseMaster();
                    }
                }
            }, delayTime, TimeUnit.SECONDS);
        } catch (ZkNodeExistsException e) {
            RunningData runningData = zkClient.readData(MASTER_PATH, true);
            if (runningData == null) {
                takeMaster();
            } else {
                masterData = runningData;
            }
        }
    }

    private void releaseMaster() {
        logger.info("releaseMaster");
        if (checkMaster()) {
            zkClient.delete(MASTER_PATH);
        }
    }

    private boolean checkMaster() {
        logger.info("checkMaster");
        try {
            RunningData runningData = zkClient.readData(MASTER_PATH);
            masterData = runningData;
            if (masterData.getName().equals(serverData.getName())) {
                return true;
            }
            return false;
        } catch (ZkNodeExistsException e) {
            return false;
        } catch (ZkInterruptedException e) {
            return checkMaster();
        } catch (ZkException e) {
            return false;
        }
    }
}
