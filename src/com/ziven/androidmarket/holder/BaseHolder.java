package com.ziven.androidmarket.holder;

import android.view.View;
import android.widget.ImageView;

public abstract class BaseHolder<T> {
	protected View mRootView;
	protected int mPosition;
	protected T t;

	private BaseHolder(View mRootView, int mPosition, T t) {
		mRootView = initView();
		// 设置数据绑定
		mRootView.setTag(this);
	}

	public int getmPosition() {
		return mPosition;
	}

	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public View getmRootView() {
		return mRootView;
	}

	public void recycleImageView(ImageView view) {
		Object tag = view.getTag();
		if (tag != null && tag instanceof String) {
			String key = (String) tag;
			ImageLoader.cancel(key);
		}
	}

	/* 子类必须覆盖用于实现UI初始化 */
	protected abstract View initView();

	/* 子类必须覆盖实现UI刷新 */
	public abstract void refreshView();

	/* 用于回收 */
	public void recycle() {

	}
}
