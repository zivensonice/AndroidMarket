package com.ziven.androidmarket.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.ActionBarActivity;

/**
 * @author Ziven 基类 所有类的基类
 */
public abstract class BaseActivity extends ActionBarActivity {
	/* 记录处于前台的Activity */
	private static BaseActivity mForegroundActivity = null;
	/* 记录所有活动的Activity */
	private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivities.add(this);
		init();
		initView();
		initActionBar();
	}

	@Override
	protected void onPause() {
		mForegroundActivity = null;
		super.onPause();
	}

	@Override
	protected void onResume() {
		mForegroundActivity = this;
		super.onResume();
	}

	/**
	 * 初始化数据
	 */
	protected abstract void init();

	/**
	 * 初始化界面
	 */
	protected abstract void initView();

	/**
	 * 初始化导航栏
	 */
	protected abstract void initActionBar();

	// 杀死除了指定Activity以外的所有Activity
	public static void finishAll(BaseActivity except) {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			if (activity != except) {
				activity.finish();
			}
		}
	}

	/* 杀死所有Activity */
	public static void finishAll() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			activity.finish();
		}
	}

	/* 判断是否还有存活的Activity */
	public static boolean hasActivity() {
		return mActivities.size() > 0;
	}

	/* 获取前端Activity */
	public static Activity getForegroundActivity() {
		return mForegroundActivity;
	}

	/* 获取栈顶Activity */
	public static BaseActivity getStackTopActivity() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		if (copy.size() > 0) {
			return copy.get(copy.size() - 1);
		}
		return null;
	}

	/* 退出APP */
	public void exitApp() {
		finishAll();
		// 杀死本身应用
		Process.killProcess(Process.myPid());
	}
}
