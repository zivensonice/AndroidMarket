package com.ziven.bean;

import com.ziven.androidmarket.utils.FileUtils;
import com.ziven.manager.DownloadManager;

public class DownloadInfo {
	private long id;// app的id，和appInfo中的id对应
	private String appName;// app的软件名称
	private long appSize = 0;// app的size
	private long currentSize = 0;// 当前的size
	private int downloadState = 0;// 下载的状态
	private String url;// 下载地址
	private String path;// 保存路径

	public static DownloadInfo clone(AppInfo appInfo) {
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.id = appInfo.getId();
		downloadInfo.appName = appInfo.getName();
		downloadInfo.appSize = appInfo.getSize();
		downloadInfo.currentSize = 0;
		downloadInfo.downloadState = DownloadManager.STATE_NONE;
		downloadInfo.url = appInfo.getDownloadUrl();
		downloadInfo.path = FileUtils.getDownloadDir() + downloadInfo.appName + ".apk";
		return downloadInfo;
	}

	public synchronized long getId() {
		return id;
	}

	public synchronized void setId(long id) {
		this.id = id;
	}

	public synchronized String getAppName() {
		return appName;
	}

	public synchronized void setAppName(String appName) {
		this.appName = appName;
	}

	public long getAppSize() {
		return appSize;
	}

	public synchronized void setAppSize(long appSize) {
		this.appSize = appSize;
	}

	public long getCurrentSize() {
		return currentSize;
	}

	public synchronized void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
	}

	public synchronized int getDownloadState() {
		return downloadState;
	}

	public synchronized void setDownloadState(int downloadState) {
		this.downloadState = downloadState;
	}

	public synchronized String getUrl() {
		return url;
	}

	public synchronized void setUrl(String url) {
		this.url = url;
	}

	public synchronized String getPath() {
		return path;
	}

	public synchronized void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "DownloadInfo [id=" + id + ", appName=" + appName + ", appSize=" + appSize + ", currentSize="
				+ currentSize + ", downloadState=" + downloadState + ", url=" + url + ", path=" + path + "]";
	}

	public float getProgress() {
		/* 除0判断 */
		if (getAppSize() == 0) {
			return 0;
		}
		// +0.0f,是为了把弄类型转换成浮点型,这样运算的时候不会出现int型而导致进度丢失
		return (getCurrentSize() + 0.0f) / getAppSize();
	}
}
