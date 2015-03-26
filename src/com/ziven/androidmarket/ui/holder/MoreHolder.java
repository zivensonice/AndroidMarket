package com.ziven.androidmarket.ui.holder;

import com.ziven.androidmarket.R;
import com.ziven.androidmarket.ui.adapter.DefaultAdapter;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.UIUtils;

import android.R.integer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MoreHolder extends BaseHolder<Integer> implements OnClickListener {

	public static final int HAS_MORE = 0;
	public static final int NO_MORE = 1;
	public static final int ERROR = 2;

	private RelativeLayout mLoading, mError;
	private DefaultAdapter<?> mAdapter;

	public MoreHolder(DefaultAdapter<?> adapter, boolean hasMore) {
		setT(hasMore ? HAS_MORE : NO_MORE);
		mAdapter = adapter;
	}

	@Override
	public void onClick(View v) {
		loadMore();
	}

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.list_more_load);
		mLoading = (RelativeLayout) view.findViewById(R.id.rl_more_loading);
		mError = (RelativeLayout) view.findViewById(R.id.rl_more_error);
		mError.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshView() {
		Integer data = getT();
		mLoading.setVisibility(data == HAS_MORE ? View.VISIBLE : View.GONE);
		mError.setVisibility(data == ERROR ? View.VISIBLE : View.GONE);
	}

	@Override
	public View getRootView() {
		if (getT() == HAS_MORE) {
			L.d("getT() == HAS_MORE:" + (getT() == HAS_MORE));
			loadMore();
		}
		return super.getRootView();
	}

	public void loadMore() {
		mAdapter.loadMore();
	}

}
