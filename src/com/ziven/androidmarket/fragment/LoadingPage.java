package com.ziven.androidmarket.fragment;

import com.ziven.androidmarket.R;
import com.ziven.androidmarket.utils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public abstract class LoadingPage extends FrameLayout {

	/* 未加载状态 */
	private static final int STATE_UNLOADED = 0;
	/* 加载中状态 */
	private static final int STATE_LOADING = 1;
	/* 加载完毕,但是出错状态 */
	private static final int STATE_ERROR = 2;
	/* 加载完毕,但是数据为空状态 */
	private static final int STATE_EMPTY = 3;
	/* 加载成功 */
	private static final int STATE_SUCCEED = 4;
	/* 加载中显示View */
	private View mLoadingView;
	/* 加载出错显示View */
	private View mErrorView;
	/* 加载数据为空显示的View */
	private View mEmptyView;
	/* 加载成功显示的View */
	private View mSucceedView;
	/* 显示当前状态View的标记值 */
	private int mState;

	public LoadingPage(Context context) {
		super(context);
		init();
	}

	private void init() {
		/* 设置加载Pager背景 */
		setBackgroundColor(UIUtils.getColor(R.color.bg_page));
		/* 初始化加载状态 */
		mState = STATE_UNLOADED;
		/* 创建初始化加载状态视图 */
		mLoadingView = createLoadingView();
	}

	protected View createLoadingView() {
		return UIUtils.inflate(R.layout);
	}

}
