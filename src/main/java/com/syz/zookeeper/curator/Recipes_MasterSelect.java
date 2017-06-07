package com.syz.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author shiyizhen
 * master选举：一个复杂的任务，从集群中选举出一台处理
 *
 */
public class Recipes_MasterSelect {

	static String master_path = "/curator_recipes_master_path";
	
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
    public static void main( String[] args ) throws Exception {
    	client.start();
        LeaderSelector selector = new LeaderSelector(client, 
        		master_path, 
        		new LeaderSelectorListenerAdapter() {
		            public void takeLeadership(CuratorFramework client) throws Exception {
		                System.out.println("成为master");
		                Thread.sleep( 3000 );
		                System.out.println( "释放master" );
		            }
		        });
        selector.autoRequeue();
        selector.start();
        //Thread.sleep(10000);
        System.out.println(client.getChildren().forPath(master_path));
        Thread.sleep( Integer.MAX_VALUE );
	}
}