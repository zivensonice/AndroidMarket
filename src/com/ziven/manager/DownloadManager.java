package com.ziven.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ziven.androidmarket.base.Constant;
import com.ziven.androidmarket.http.HttpHelper;
import com.ziven.androidmarket.http.HttpHelper.HttpResult;
import com.ziven.androidmarket.utils.IOUtils;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.AppInfo;
import com.ziven.bean.DownloadInfo;

public class DownloadManager {
	/** 初始化状态,预留状态 */
	public static final int STATE_NONE = 0;
	/** 等待中 */
	public static final int STATE_WAITING = 1;
	/** 下载中 */
	public static final int STATE_DOWNLOADING = 2;
	/** 暂停 */
	public static final int STATE_PAUSED = 3;
	/** 下载完毕 */
	public static final int STATE_DOWNLOADED = 4;
	/** 下载失败 */
	public static final int STATE_ERROR = 5;
	private static DownloadManager instance;

	private DownloadManager() {
	}

	/** 用于记录下载信息,如果是真是羡慕,需要持久化保存 */
	private Map<Long, DownloadInfo> mDownLoadMap = new ConcurrentHashMap<Long, DownloadInfo>();
	/** 用于记录观察者,当信息发生了改变,需要通知他们 */
	private List<DownloadObserver> mObservers = new ArrayList<DownloadManager.DownloadObserver>();
	/** 用于记录所有下载的任务,方便在取消下载时,通过id找到该任务进行删除 */
	private Map<Long, DownloadTask> mTaskMap = new ConcurrentHashMap<Long, DownloadManager.DownloadTask>();

	/**
	 * 单例对象
	 */
	public static DownloadManager getInstance() {
		if (instance == null) {
			synchronized (DownloadManager.class) {
				if (instance == null) {
					instance = new DownloadManager();
				}
			}
		}
		return instance;
	}

	/** 注册观测者 */
	public void registerObserver(DownloadObserver observer) {
		synchronized (mObservers) {
			if (!mObservers.contains(observer)) {
				mObservers.add(observer);
			}
		}
	}

	/**
	 * 反注册观测者
	 */
	public void unregisterObserver(DownloadObserver observer) {
		synchronized (mObservers) {
			if (mObservers.contains(observer)) {
				mObservers.remove(observer);
			}
		}
	}

	public synchronized void download(AppInfo appInfo) {
		// 先判断是否有这个app的下载信息
		DownloadInfo info = mDownLoadMap.get(appInfo.getId());
		if (info == null) {
			// 如果没有,就根据appInfo创建一个新的下载信息
			info = DownloadInfo.clone(appInfo);
			mDownLoadMap.put(appInfo.getId(), info);
		}
		// 判断状态是否为NONE/PAUSED/ERROR.只有这三种状态才能进行下载其他状态不予处理
		if (info.getDownloadState() == STATE_NONE
				|| info.getDownloadState() == STATE_PAUSED
				|| info.getDownloadState() == STATE_ERROR) {
			// 下载前,把状态设置为STATE_WAITING,因为此时并没有产生开始下载,只有把任务放入线程池,当任务真正开始执行时,才会改为STATE_DOWNLOADING
			info.setDownloadState(STATE_WAITING);
			notifyDownloadStateChanged(info);
			// 创建一个下载任务
			DownloadTask task = new DownloadTask(info);
			mTaskMap.put(info.getId(), task);
			ThreadManager.getDownLoadPool().execute(task);
		}
	}

	/** 暂停下载任务 */
	public synchronized void pause(AppInfo appInfo) {
		stopDownload(appInfo);
		DownloadInfo info = mDownLoadMap.get(appInfo.getId());
		if (info != null) {
			info.setDownloadState(STATE_PAUSED);
			notifyDownloadStateChanged(info);
		}
	}

	/** 取消下载,逻辑和暂停类似只是需要删除下载文件 */
	public synchronized void cancle(AppInfo appInfo) {
		stopDownload(appInfo);
		DownloadInfo info = mDownLoadMap.get(appInfo.getId());
		if (info != null) {
			info.setDownloadState(STATE_NONE);
			notifyDownloadStateChanged(info);
			info.setCurrentSize(0);
			File file = new File(info.getPath());
			file.delete();
		}
	}

	/**
	 * 安装应用
	 */
	public synchronized void install(AppInfo appInfo) {
		stopDownload(appInfo);
		DownloadInfo info = mDownLoadMap.get(appInfo.getId());
		if (info != null) {
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			installIntent.setDataAndType(Uri.parse("file://" + info.getPath()),
					"application/vnd.android.package-archive");
			UIUtils.getContext().startActivity(installIntent);
		}
		notifyDownloadStateChanged(info);
	}

	/** 启动应用,启动应用是最后一个步骤 */
	public synchronized void open(AppInfo appInfo) {
		try {
			Context context = UIUtils.getContext();
			Intent intent = context.getPackageManager()
					.getLaunchIntentForPackage(appInfo.getPackageName());
			context.startActivity(intent);
		} catch (Exception e) {
			L.e(e);
		}
	}

	/**
	 * 获取下载信息
	 */
	public synchronized DownloadInfo getDownloadInfo(long id) {
		return mDownLoadMap.get(id);
	}

	/**
	 * 如果下载任务位于线程池,从线程池中移除该任务
	 */
	private void stopDownload(AppInfo appInfo) {
		DownloadTask task = mTaskMap.remove(appInfo.getId());
		if (task != null) {
			ThreadManager.getDownLoadPool().cancel(task);
		}
	}

	public class DownloadTask implements Runnable {
		private DownloadInfo info;

		public DownloadTask(DownloadInfo info) {
			this.info = info;
		}

		@Override
		public void run() {
			info.setDownloadState(STATE_DOWNLOADING);// 先改变下载状态
			notifyDownloadStateChanged(info);
			// 获取下载文件
			File file = new File(info.getPath());
			HttpResult httpResult = null;
			InputStream stream = null;
			if (info.getCurrentSize() == 0 || !file.exists()
					|| file.length() != info.getCurrentSize()) {
				// 如果文件不存在,或者进度为0,或者进度和文件当前长度不相符,就需要重新下载
				info.setCurrentSize(0);
				file.delete();
				httpResult = HttpHelper.download(Constant.IP + "download?name="
						+ info.getUrl());
			} else {
				// 文件存在且长度和进度相等,采用断点下载
				httpResult = HttpHelper.download(Constant.IP + "download?name="
						+ info.getUrl() + "&range=" + info.getCurrentSize());
			}
			if (httpResult == null
					|| (stream = httpResult.getInputStream()) == null) {
				/* 没有下载内容,修改为错误状态 */
				info.setDownloadState(STATE_ERROR);
				notifyDownloadStateChanged(info);
			} else {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file, true);
					byte[] buffer = new byte[1024 * 4];
					for (int count = -1; (count = stream.read(buffer)) != -1
							&& info.getDownloadState() == STATE_DOWNLOADING;) {
						// 每次读取到数据后,都需要判断是否为下载状态,如果不是,下载需要终止,如果是就刷新进度
						fos.write(buffer, 0, count);
						fos.flush();
						info.setCurrentSize(info.getCurrentSize() + count);
						notifyDownloadProgressed(info);// 刷新进度
					}
				} catch (Exception e) {
					L.e(e);
					info.setDownloadState(STATE_ERROR);
					notifyDownloadStateChanged(info);
					info.setCurrentSize(0);
					file.delete();
				} finally {
					IOUtils.close(fos);
					if (httpResult != null) {
						httpResult.close();
					}
				}

				// 判断进度是否和app总长度相等
				if (info.getCurrentSize() == info.getAppSize()) {
					info.setDownloadState(STATE_DOWNLOADED);
					notifyDownloadStateChanged(info);
				} else if (info.getDownloadState() == STATE_PAUSED) {
					notifyDownloadStateChanged(info);
				} else {
					info.setDownloadState(STATE_ERROR);
					notifyDownloadStateChanged(info);
					info.setCurrentSize(0);
					file.delete();
				}
			}
			mTaskMap.remove(info.getId());
		}
	}

	/* 当下载状态发生改变的时候回调 */
	public void notifyDownloadStateChanged(DownloadInfo info) {
		synchronized (mObservers) {
			for (DownloadObserver observer : mObservers) {
				observer.onDownloadStateChanged(info);
			}
		}
	}

	/** 当下载进度发送改变的时候回调 */
	public void notifyDownloadProgressed(DownloadInfo info) {
		synchronized (mObservers) {
			for (DownloadObserver observer : mObservers) {
				observer.onDownloadProgressed(info);
			}
		}
	}

	public interface DownloadObserver {
		/** 观测下载状态改变 */
		public void onDownloadStateChanged(DownloadInfo info);

		/** 观测下载进度改变 */
		public void onDownloadProgressed(DownloadInfo info);
	}

}
