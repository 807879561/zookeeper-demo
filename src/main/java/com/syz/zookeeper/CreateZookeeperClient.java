package com.syz.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class CreateZookeeperClient implements Watcher {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zookeeper = null;
	private static Stat stat = new Stat();

	public static void main(String[] args) throws Exception {
		String path = "/dd7";
		zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new CreateZookeeperClient());
		connectedSemaphore.await();// 阻塞直到通知返回
		// long sessionId=zookeeper.getSessionId();
		// byte[] passwd=zookeeper.getSessionPasswd();
		//
		// //复用同一个会话id
		// Thread.sleep(2000);
		// zookeeper=new ZooKeeper("127.0.0.1:2181",5000,new
		// CreateZookeeperClient(),sessionId,passwd);
		// 同步创建节点
		// String path1=zookeeper.create("/test", "123".getBytes(),
		// Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//临时节点
		// System.out.println(path1);
		// String path2=zookeeper.create("/test", "123".getBytes(),
		// Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);//临时顺序节点
		// System.out.println(path2);

		// 异步创建节点
		// zookeeper.create("/test", "123".getBytes(), Ids.OPEN_ACL_UNSAFE,
		// CreateMode.EPHEMERAL_SEQUENTIAL, new CreateNodeCallback(), "i am
		// context");

		// 删除节点
		// zookeeper.delete("/test0000000003", 0, new DelCallback(), "delete
		// node");

		// 获取子节点列表
		// zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE,
		// CreateMode.PERSISTENT);
		// zookeeper.create(path+"/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE,
		// CreateMode.EPHEMERAL);
		// //同步获取
		// List<String> childrenList=zookeeper.getChildren(path, true);
		// System.out.println(childrenList);
		// //异步获取
		// zookeeper.getChildren(path, true, new Children2Callback(), null);
		// zookeeper.create(path+"/c4", "".getBytes(), Ids.OPEN_ACL_UNSAFE,
		// CreateMode.EPHEMERAL);

		// 获取节点数据
		zookeeper.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//		System.out.println(new String(zookeeper.getData(path, true, stat)));
//		System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
		//异步获取节点数据
		//zookeeper.getData(path, true, new IDataCallback(), null);
		//zookeeper.setData(path, "123".getBytes(), -1);//-1表示基于最新版本更新，数据版本从0开始计数
		zookeeper.setData(path, "456".getBytes(), -1, new IStatCallback(), null );
		
		
		
		Thread.sleep(Integer.MAX_VALUE);
	}

	public void process(WatchedEvent event) {// 连接成功处理通知
		if (KeeperState.SyncConnected == event.getState() && null == event.getPath()) {
			connectedSemaphore.countDown();
		} else if (event.getType() == EventType.NodeChildrenChanged) {
			try {
				System.out.println(zookeeper.getChildren(event.getPath(), true));
			} catch (Exception e) {

			}
		} else if (event.getType() == EventType.NodeDataChanged) {
			try {
				System.out.println(new String(zookeeper.getData(event.getPath(), true, stat)));
				System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
			} catch (Exception e) {
			}
		}
	}

}

class CreateNodeCallback implements AsyncCallback.StringCallback {
	public void processResult(int rc, String path, Object ctx, String name) {
		System.out.println(rc + "," + path + "," + ctx + "," + name);// rc表示服务端响应码，0表示接口调用成功
	}
}

class DelCallback implements AsyncCallback.VoidCallback {
	public void processResult(int rc, String path, Object ctx) {
		System.out.println(rc + "," + path + "," + ctx);
	}
}

class Children2Callback implements AsyncCallback.Children2Callback {
	@Override
	public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
		System.out.println("Get Children znode result: [response code: " + rc + ", param path: " + path + ", ctx: "
				+ ctx + ", children list: " + children + ", stat: " + stat);
	}
}
class IDataCallback implements AsyncCallback.DataCallback{
	public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println(rc + ", " + path + ", " + new String(data));
        System.out.println(stat.getCzxid()+","+
                  		   stat.getMzxid()+","+
		                   stat.getVersion());
    }
}
class IStatCallback implements AsyncCallback.StatCallback{
	public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (rc == 0) {
            System.out.println("SUCCESS");
        }
    }
}