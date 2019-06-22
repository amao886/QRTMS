package com.ycg.ksh.common.extend.lock.impl;

import com.ycg.ksh.common.extend.lock.DistributedSynchronize;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.ZookeeperUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZookeeperSynchronizeManager implements DistributedSynchronize, ConnectionStateListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private CountDownLatch countDownLatch = new CountDownLatch(1);

	private CuratorFramework curator;

	/**连接字符串*/
	private String connectString;
	/**根节点*/
    private String root = "/LOCKS";
    
    private Map<Serializable, String> currentCache = new ConcurrentHashMap<Serializable, String>();
 
    public ZookeeperSynchronizeManager(String connectString) throws Exception {
		super();
        this.connectString = connectString;
		initialize(false);
	}
    
	public ZookeeperSynchronizeManager(String connectString, String root) throws Exception {
		super();
		this.connectString = connectString;
		this.root = ZookeeperUtil.normalize(root);
		initialize(false);
	}

	private synchronized void initialize(boolean enforce) throws Exception{
		if(curator == null || enforce){
			currentCache.clear();
			curator = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
			curator.start();
			curator.getConnectionStateListenable().addListener(this);
			if (curator.checkExists().forPath(root) == null){	// 如果根节点不存在,则创建根节点
				curator.create().withMode(CreateMode.PERSISTENT).forPath(root);
			}
		}
    }

	public void initialize(String subpath) throws Exception {
    	String path = ZookeeperUtil.contact(root, subpath);
		if (curator.checkExists().forPath(path) == null){	// 如果根节点不存在,则创建根节点
			curator.create().withMode(CreateMode.PERSISTENT).forPath(path);
		}
	}

	private String createLockNode(String lockPath) throws Exception {
    	if(countDownLatch != null){
			countDownLatch.await(1, TimeUnit.MINUTES);
		}
		return curator.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(lockPath);
	}

	@Override
	public boolean lock(String subpath, Serializable lockName) {
		boolean lock = false;
		String current = null,
				parent = ZookeeperUtil.contact(root, subpath),
				lockPath = ZookeeperUtil.contact(parent, lockName.toString());
		try {
			if(StringUtils.isNotBlank(current = createLockNode(lockPath))) {// 创建临时有序节点
				logger.debug("{} 创建临时有序节点 {}", Thread.currentThread().getName(), current);
				List<String> subNodes = curator.getChildren().forPath(parent);// 取所有子节点
				if(subNodes.size() == 1){
					logger.debug("{} 获得锁 {}", Thread.currentThread().getName(), current);
					lock = true;
				} else {
					//获取最小的节点
					String minNode = subNodes.parallelStream().min(Comparator.comparing(s -> s)).get();
					//若当前节点为最小节点，则获取锁成功
					if (ZookeeperUtil.last(current).equals(minNode)) {
						logger.debug("{} 获得锁 {}", Thread.currentThread().getName(), current);
						lock = true;
					}
				}
		        return lock;
			}
	        return false;
		}catch (Exception e) {
			logger.error("lock exception {} {}", subpath, lockName, e);
			return false;
		}finally {
			if(StringUtils.isNotBlank(current))  {
				if(lock) {
		        	currentCache.put(lockPath, current);
		        }else {
					unlock(current);
		        }
	        }
		}
	}

	@Override
	public void unlock(String subpath, Serializable lockName) {
		if(lockName != null){
			unlock(currentCache.get(ZookeeperUtil.contact(root, subpath, lockName.toString())));
		}
	}
	
	private void unlock(String current) {
		try {
			if(StringUtils.isNotBlank(current) && curator != null){
				if(curator.checkExists().forPath(current) != null) {
					logger.debug("{} 释放锁 {}", Thread.currentThread().getName(), current);
					curator.delete().forPath(current);
				}
			}
		} catch (Exception e) { }
	}

	/**
	 * Called when there is a state change in the connection
	 *
	 * @param client   the client
	 * @param newState the new state
	 */
	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		try {
			if(ConnectionState.CONNECTED == newState || ConnectionState.RECONNECTED == newState){
				logger.info("ZookeeperSynchronizeManager --> 链接成功");
				countDownLatch.countDown();
			}
			if(ConnectionState.LOST == newState || ConnectionState.SUSPENDED == newState){
				logger.info("ZookeeperSynchronizeManager --> 链接断开,准备开始重连...");
				countDownLatch = new CountDownLatch(1);
				initialize(true);
			}
		} catch (Exception e) { }
	}

	public String getConnectString() {
		return connectString;
	}
	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}

	public void close(){
    	if(curator != null){
			curator.close();
		}
	}
	/*
	 * 测试
	 * 
    */

	static int n = 500;
	public static void main(String[] args) throws Exception {
		final ZookeeperSynchronizeManager lock = new ZookeeperSynchronizeManager("172.16.35.70:2181,172.16.35.71:2181");
		lock.initialize("WAYBILL");
		Runnable runnable = new Runnable() {
            public void run() {
                try {
					Thread.sleep(500);
                    if(lock.lock("WAYBILL", "17557000021")) {
                        System.out.println(--n);
                        System.out.println(Thread.currentThread().getName() + "正在运行");
                    }
                } catch (Exception e) {
					e.printStackTrace();
				}finally {
                    if (lock != null) {
                        try {
							lock.unlock("WAYBILL", "17557000021");
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                }
            }
        };
        for (int i = 0; i < 10; i++) {
        	new Thread(runnable).start();
        }
        System.in.read();
	}
}
