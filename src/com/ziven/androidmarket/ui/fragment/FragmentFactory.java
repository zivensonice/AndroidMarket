package com.ziven.androidmarket.ui.fragment;

import java.util.HashMap;

import android.support.v4.app.Fragment;

public class FragmentFactory {
	// 主页
	private final static int TAB_HOME = 0;
	// 应用
	private final static int TAB_APP = 1;
	// 游戏
	private final static int TAB_GAME = 2;
	// 专项
	private final static int TAB_SUBJECT = 3;
	// 推荐
	private final static int TAB_RECOMMENT = 4;
	// 分类
	private final static int TAB_CATEGORY = 5;
	// 排行榜
	private final static int TAB_TOP = 6;
	private static HashMap<Integer, BaseFragment> mFragments = new HashMap<Integer, BaseFragment>();

	public static BaseFragment createFragment(int position) {
		// 查看缓存中是否存在实例
		BaseFragment fragment = mFragments.get(position);
		if (null == fragment) {
			switch (position) {
			case TAB_HOME:
				fragment = new HomeFragment();
				// fragment = new SubjectFragment();
				break;
			case TAB_APP:
				fragment = new AppFragment();
				break;
			case TAB_GAME:
				fragment = new GameFragment();
				break;
			case TAB_SUBJECT:
				fragment = new SubjectFragment();
				break;
			case TAB_RECOMMENT:
				fragment = new RecommentFragment();
				break;
			case TAB_CATEGORY:
				fragment = new CategoryFragment();
				break;
			case TAB_TOP:
				fragment = new TopFragment();
				break;
			default:
				break;
			}
		}
		// 加入缓存
		mFragments.put(position, fragment);
		return fragment;
	}

}
