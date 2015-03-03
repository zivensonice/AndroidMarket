package com.ziven.androidmarket.utils;

import android.app.ActivityManager;
import android.content.Context;

public class SystemUtils {

	public static long getAvailableMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.availMem;
	}
}
