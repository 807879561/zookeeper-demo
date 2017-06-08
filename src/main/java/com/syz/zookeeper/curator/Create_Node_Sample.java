package com.syz.zookeeper.curator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

//使用curator创建节点,临时节点会话结束后就消失
public class Create_Node_Sample {
    static String path = "/zk-book/c11";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    public static void main(String[] args) throws Exception {
        client.start();
        client.create()
              .creatingParentsIfNeeded()
              .withMode(CreateMode.PERSISTENT)
              .forPath(path, "init".getBytes());
        System.out.println(new String(client.getData().forPath(path)));
    }
}