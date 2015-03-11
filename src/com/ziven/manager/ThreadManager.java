package com.ziven.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 一个简易的线程池管理类,提供三个线程池
 * 项目使用的时候最好全部通过连接池来实现,因为线程在创建和销毁的时候开销成本特别大,所以我们通过采用线程池的方式来维护整个项目所需线程
 * 
 * @author Ziven
 * 
 */
public class ThreadManager {
	public static final String DEFAULT_SINGLE_POOL_NAME = "DEFAULT_SINGLE_POOL_NAME";

	private static ThreadPoolProxy mLongPool = null;
	private static Object mLongLock = new Object();

	private static ThreadPoolProxy mShortPool = null;
	private static Object mShortLock = new Object();

	private static ThreadPoolProxy mDownloadPool = null;
	private static Object mDownLoadLock = new Object();

	private static Map<String, ThreadPoolProxy> mMap = new HashMap<String, ThreadManager.ThreadPoolProxy>();
	private static Object mSingleLock = new Object();

	public static ThreadPoolProxy getDownLoadPool() {
		synchronized (mDownLoadLock) {
			if (null == mDownloadPool) {
				mDownloadPool = new ThreadPoolProxy(3, 3, 5L);
			}
			return mDownloadPool;
		}
	}

	/** 获取一个用于执行长耗时任务的线程池，避免和短耗时任务处在同一个队列而阻塞了重要的短耗时任务，通常用来联网操作 */
	public static ThreadPoolProxy getLongPool() {
		synchronized (mLongLock) {
			if (mLongPool == null) {
				mLongPool = new ThreadPoolProxy(5, 5, 5L);
			}
			return mLongPool;
		}
	}

	/** 获取一个用于执行短耗时任务的线程池，避免因为和耗时长的任务处在同一个队列而长时间得不到执行，通常用来执行本地的IO/SQL */
	public static ThreadPoolProxy getShortPool() {
		synchronized (mShortLock) {
			if (mShortPool == null) {
				mShortPool = new ThreadPoolProxy(2, 2, 5L);
			}
			return mShortPool;
		}
	}

	/** 获取一个单线程池，所有任务将会被按照加入的顺序执行，免除了同步开销的问题 */
	public static ThreadPoolProxy getSinglePool() {
		return getSinglePool(DEFAULT_SINGLE_POOL_NAME);
	}

	/** 获取一个单线程池，所有任务将会被按照加入的顺序执行，免除了同步开销的问题 */
	public static ThreadPoolProxy getSinglePool(String name) {
		synchronized (mSingleLock) {
			ThreadPoolProxy singlePool = mMap.get(name);
			if (singlePool == null) {
				singlePool = new ThreadPoolProxy(1, 1, 5L);
				mMap.put(name, singlePool);
			}
			return singlePool;
		}
	}

	/**
	 * @author Ziven 
	 * <li>线程池简单封装类
	 */
	public static class ThreadPoolProxy {
		/* 任务管理池 */
		private ThreadPoolExecutor mPool;
		/* 当前任务数 */
		private int mCorePoolSize;
		/* 最大任务数 */
		private int mMaxPoolSize;
		/* 最大存活时间 */
		private long mKeepAliveTime;

		private ThreadPoolProxy(int mCorePoolSize, int mMaxPoolSize,
				long mKeepAliveTime) {
			super();
			this.mCorePoolSize = mCorePoolSize;
			this.mMaxPoolSize = mMaxPoolSize;
			this.mKeepAliveTime = mKeepAliveTime;
		}

		public synchronized void execute(Runnable run) {
			if (null == run) {
				return;
			}
			if (null == mPool || mPool.isShutdown()) {
				// 当线程池中的线程小鱼mCorePoolSize的时候,直接创建新的线程
				// 当线程池中的线程数目等于mCorePoolSize,将会把任务放到BlockingQueue队列中等待执行
				// BlockingQueue中的任务满了,将会创建新的线程去执行任务
				// 但是当总线程数目大于mMaxPoolSize时,将会抛出异常交给最后一个RejectedExecutionHandler执行
				// mKeepAliveTime是线程执行完后,且队列中没有可执行任务的时候,最大的存活时间,单位在下一个参数中
				// ThreadFactory是每次创建新的线程工厂
				mPool = new ThreadPoolExecutor(mCorePoolSize, mMaxPoolSize,
						mKeepAliveTime, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(),
						Executors.defaultThreadFactory(), new AbortPolicy());
			}
			mPool.execute(run);
		}

		/**
		 * 取消队列中未执行的任务
		 * 
		 * @param run
		 */
		public synchronized void cancel(Runnable run) {
			if (null != mPool && (!mPool.isShutdown() || mPool.isTerminating())) {
				mPool.getQueue().remove(run);
			}
		}

		/**
		 * 查看任务是否在未执行的队列中
		 * 
		 * @param run
		 * @return
		 */
		public synchronized boolean contains(Runnable run) {
			if (null != mPool && (!mPool.isShutdown() || mPool.isTerminating())) {
				return mPool.getQueue().contains(run);
			} else {
				return false;
			}
		}

		/**
		 * 不管任务是否执行,停止线程池,正在执行的任务会被打断
		 */
		public void stop() {
			if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
				mPool.shutdownNow();
			}
		}

		/**
		 * 平缓的停止线程池,执行已经加入的任务,
		 */
		public synchronized void shutdown() {
			if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
				mPool.shutdown();
			}
		}
	}
}
