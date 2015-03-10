package com.ziven.androidmarket.fragment;

import com.ziven.androidmarket.R;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.manager.ThreadManager;

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
		if (null != mLoadingView) {
			addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}
		mErrorView = createErrorView();
		if (null != mErrorView) {
			addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}
		mEmptyView = createEmptyView();
		if (null != mEmptyView) {
			addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}
		showPageSafe();
	}

	protected View createLoadingView() {
		// TODO 返回加载布局
		return null;
	}

	protected View createEmptyView() {
		// TODO
		return null;
	}

	protected View createErrorView() {
		// TODO
		return null;
	}

	class LoadingTask implements Runnable {
		@Override
		public void run() {
			final LoadResult loadResult = load();
			// 在主线程中更新UI
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					// 状态改变和界面信息息息相关,所以需要放到主线程来赋值,保障同步
					mState = loadResult.getValue();
					showPage();
				}

			});
		}
	}

	/* 显示对应的View */
	private void showPage() {
		if (null != mLoadingView) {
			mLoadingView.setVisibility(mState == STATE_UNLOADED
					| mState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
		}
		if (null != mErrorView) {
			mErrorView.setVisibility(mState == STATE_ERROR ? View.VISIBLE
					: View.INVISIBLE);
		}
		if (null != mEmptyView) {
			mErrorView.setVisibility(mState == STATE_EMPTY ? View.VISIBLE
					: View.INVISIBLE);
		}
		// 只有数据成功返回了,才知道成功的View该如何显示,因为该View的显示依赖加载完毕的数据
		if (mState == STATE_SUCCEED && mSucceedView == null) {
			mSucceedView = createLoadedView();
			addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}
		if (null != mSucceedView) {
			mSucceedView.setVisibility(mState == STATE_SUCCEED ? View.VISIBLE
					: View.INVISIBLE);
		}
	}

	/**
	 * 线程安全方法
	 */
	private void showPageSafe() {
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				showPage();
			}
		});
	}

	public abstract View createLoadedView();

	public abstract LoadResult load();

	public enum LoadResult {
		ERROR(2), EMPTY(3), SUCCEED(4);
		int value;

		LoadResult(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public boolean needReset() {
		return mState == STATE_ERROR || mState == STATE_EMPTY;
	}

	public void reset() {
		mState = STATE_UNLOADED;
		showPageSafe();
	}

	public synchronized void show() {
		if (needReset()) {
			mState = STATE_UNLOADED;
		}
		if (mState == STATE_UNLOADED) {
			mState = STATE_LOADING;
			LoadingTask task = new LoadingTask();
			ThreadManager.getLongPool().execute(task);
		}
		showPageSafe();
	}
}
