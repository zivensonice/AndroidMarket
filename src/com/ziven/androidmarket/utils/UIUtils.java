package com.ziven.androidmarket.utils;

import com.ziven.androidmarket.BaseApplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @类名:UIUtils
 * @功能描述:界面操作基础类
 * @作者:ZhouRui
 * @时间:2015-3-2 下午3:38:07
 * @Copyright 2014
 */
public class UIUtils {

	// 获取上下文
	public static Context getContext() {
		return BaseApplication.getApplication();
	}

	/* 获取主线程id */
	public static long getMainThreadID() {
		return BaseApplication.getMainThreadId();
	}

	// 获取句柄
	public static Handler getHandler() {
		return BaseApplication.getMainThreadHandler();
	}

	// 在主线程中执行runnable
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	// 在主线程中延迟执行runnable
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	// 在主线程looper里面移除runnable
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	/* 填充布局到上下文 */
	public static View inflate(int resID) {
		return LayoutInflater.from(getContext()).inflate(resID, null);
	}

	// dip->px
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	// px->dip
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	// 获取资源
	public static Resources getResources() {
		return getContext().getResources();
	}

	// 获取String
	public static String getString(int resID) {
		return getResources().getString(resID);
	}

	// 获取字符串数组
	public static String[] getsStringArray(int resID) {
		return getResources().getStringArray(resID);
	}

	// 获取dimens
	public static int getDimens(int resID) {
		// getDimensionPixelSize获取的是int类型像素值,如果是px单位,返回的是px值本身
		return getResources().getDimensionPixelSize(resID);
	}

	// 获取drawable
	public static Drawable getDrawable(int resID) {
		return getResources().getDrawable(resID);
	}

	// 获取颜色
	public static int getColor(int resID) {
		return getResources().getColor(resID);
	}

	// 获取颜色选择器
	public static ColorStateList getcolorColorStateList(int resID) {
		return getResources().getColorStateList(resID);
	}

	// 判断是否运行在主线程
	public static boolean isRunInMainThread() {
		return Process.myTid() == getMainThreadID();
	}

	// 在主线程中执行任务
	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	public static void showToastSafe(final int resID) {

	}

	public static void showToastSafe(final String str) {
		if (isRunInMainThread()) {
		}
	}

}
