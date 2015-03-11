package com.ziven.androidmarket.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

/**
 * @类名:BaseApplication
 * @功能描述:主线程基础类
 * @作者:ZhouRui
 * @时间:2015-3-2 下午3:22:31
 * @Copyright 2014
 */
public class BaseApplication extends Application {
	// 主线程上下文
	private static BaseApplication mContext;
	// 主线程句柄
	private static Handler mMainThreadHandler;
	// 主线程线程ID
	private static int mMainThreadId;
	// 获取主线程轮询器
	private static Looper mMainThreadLooper;

	@Override
	public void onCreate() {
		super.onCreate();
		BaseApplication.mContext = this;
		mMainThreadHandler = new Handler();
		mMainThreadId = Process.myTid();
		mMainThreadLooper = getMainLooper();
	}

	public static BaseApplication getApplication() {
		return mContext;
	}

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static long getMainThreadId() {
		return mMainThreadId;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}
}
