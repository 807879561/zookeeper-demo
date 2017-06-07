package com.syz.zookeeper.curator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

//实现分布式锁
public class Recipes_Lock {

	static String lock_path = "/curator_recipes_lock_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
	public static void main(String[] args) throws Exception {
		client.start();
		final InterProcessMutex lock = new InterProcessMutex(client,lock_path);
		final CountDownLatch down = new CountDownLatch(1);
		for(int i = 0; i < 30; i++){
			new Thread(new Runnable() {
				public void run() {
					try {
						down.await();
						lock.acquire();
					} catch ( Exception e ) {System.out.println(1);}
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
					String orderNo = sdf.format(new Date());
					System.out.println("生成订单号 : "+orderNo);
					try {
						lock.release();
					} catch ( Exception e ) {}
				}
			}).start();
		}
		down.countDown();//所有线程启动后，使原来阻塞的线程开始工作
	}
}