package com.syz.zookeeper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;


/**
 * @author shiyizhen
 * 会话权限访问
 */
public class AuthSample_Get2 {

    final static String PATH = "/zk-book-auth_test";
    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper1 = new ZooKeeper("127.0.0.1:2181",5000,null);
        zookeeper1.addAuthInfo("digest", "foo:true".getBytes());
        zookeeper1.create( PATH, "init".getBytes(), //
        		           Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL );
        
        ZooKeeper zookeeper2 = new ZooKeeper("127.0.0.1:2181",50000,null);
        zookeeper2.addAuthInfo("digest", "foo:true".getBytes());
        System.out.println(zookeeper2.getData( PATH, false, null ));
        
        ZooKeeper zookeeper3 = new ZooKeeper("127.0.0.1:2181",50000,null);
        zookeeper3.addAuthInfo("digest", "foo:false".getBytes());
        zookeeper3.getData( PATH, false, null );
    }
}