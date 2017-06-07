package com.syz.zookeeper.curator;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

//使用fluent风格创建zooke客户端
public class Create_Session_Sample_fluent {
    public static void main(String[] args) throws Exception{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
        CuratorFrameworkFactory.builder()
                             .connectString("127.0.0.1:2181")
                             .sessionTimeoutMs(5000)
                             .retryPolicy(retryPolicy)
                             .namespace("test")//所有的操作都是基于／test目录
                             .build();
        client.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}