package com.syz.zookeeper.curator;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

//使用curator创建zookepper客户端
public class Create_Session_Sample {
    public static void main(String[] args) throws Exception{
    	//重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);//初始sleep时间，最大重试次数，最大sleep时间
        CuratorFramework client =
        CuratorFrameworkFactory.newClient("127.0.0.1:2181",
        		5000,//会话超时时间
        		3000,//连接创建超时时间
        		retryPolicy);
        client.start();
        System.out.println(1);
        Thread.sleep(Integer.MAX_VALUE);
    }
}