package com.ascend.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

public class DistributedAtomicInt {
    static String counter_path = "/curator_recipes_counter_path";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("vhost1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();

        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, counter_path, new RetryNTimes(3, 1000));
        AtomicValue<Integer> rc = atomicInteger.add(8);
        System.out.println("Result：" + rc.succeeded());

        Thread.sleep(Integer.MAX_VALUE);
    }

}
