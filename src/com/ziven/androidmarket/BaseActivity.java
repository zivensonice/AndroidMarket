package com.ziven.androidmarket;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * @author Ziven 
 * 基类 所有类的基类
 */
public abstract class BaseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initView();
		initActionBar();
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
}
