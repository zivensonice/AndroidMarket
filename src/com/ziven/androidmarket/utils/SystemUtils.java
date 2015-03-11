package com.ziven.androidmarket.utils;

import android.app.ActivityManager;
import android.content.Context;

public class SystemUtils {

	/* 获取应用最大可用内存 */
	public static long getAvailableMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.availMem;
	}

	 /*获取单个引用最大分配内存,单位为byte*/
	public static long getOneAppMaxMemory() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getMemoryClass() * 1024 * 1024;
	}
}
