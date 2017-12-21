/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ascend.zkclient;


import com.ascend.util.PropertiesUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LeaderSelectorZkClient {
    private static Logger logger = LoggerFactory.getLogger(LeaderSelectorZkClient.class);

    // 启动的服务个数
    private static final int CLIENT_QTY = 10;

    public static void main(String[] args) throws Exception {
        // 保存所有zkClient的列表
        List<ZkClient> clients = new ArrayList<ZkClient>();
        // 保存所有服务的列表
        List<WorkServer> workServers = new ArrayList<WorkServer>();

        try {
            for (int i = 0; i < CLIENT_QTY; ++i) {
                // 创建zkClient
                ZkClient client = new ZkClient(PropertiesUtil.getStringValue("connectString"), PropertiesUtil.getIntValue("sessionTimeout"), PropertiesUtil.getIntValue("connectionTimeout"), new SerializableSerializer());
                clients.add(client);
                // 创建serverData
                RunningData runningData = new RunningData();
                runningData.setCid(Long.valueOf(i));
                runningData.setName("Client #" + i);
                // 创建服务
                WorkServer workServer = new WorkServer(runningData);
                workServer.setZkClient(client);

                workServers.add(workServer);
                workServer.start();
            }

            logger.info("敲回车键退出！");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            logger.info("shutting down...");

            for (WorkServer workServer : workServers) {
                try {
                    workServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (ZkClient client : clients) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
