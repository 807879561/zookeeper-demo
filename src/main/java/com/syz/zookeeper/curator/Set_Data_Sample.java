package com.syz.zookeeper.curator;
import java.util.HashMap;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.google.gson.Gson;

//更新数据
public class Set_Data_Sample {

    static String path = "/zk-bookggg";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
        client.start();
        //client.delete().deletingChildrenIfNeeded().forPath( path );
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("jdbl.url", "1.1.1.1");
        Gson gson=new Gson();
        client.create()
              .creatingParentsIfNeeded()
              .withMode(CreateMode.EPHEMERAL)
              .forPath(path, gson.toJson(map).getBytes());
        Stat stat = new Stat();
        map=gson.fromJson(new String(client.getData().storingStatIn(stat).forPath(path)), Map.class);
        System.out.println("Success set node for : " + path + ", new version: "
                + client.setData().withVersion(stat.getVersion()).forPath(path).getVersion());
        try {
        	System.out.println("stat version:"+stat.getVersion());
            client.setData().withVersion(stat.getVersion()).forPath(path);
        } catch (Exception e) {
            System.out.println("Fail set node due to " + e.getMessage());
        }
    }
}