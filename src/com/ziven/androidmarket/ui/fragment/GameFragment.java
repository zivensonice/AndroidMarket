package com.ziven.androidmarket.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.ziven.androidmarket.protocol.GameProtocol;
import com.ziven.androidmarket.ui.adapter.ListBaseAdapter;
import com.ziven.androidmarket.ui.view.LoadingPage.LoadResult;
import com.ziven.androidmarket.ui.widget.BaseListView;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.AppInfo;

public class GameFragment extends BaseFragment {
	private BaseListView mListView;
	private GameAdapter mAdapter;
	private List<AppInfo> mDatas;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 可见时,需要启动监听,以便随时根据下载状态刷新界面
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) {
			mAdapter.startObserver();
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 不可见时,需要关闭监听
	 */
	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.stopObserver();
		}
	}

	@Override
	protected View createSucceedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mAdapter = new GameAdapter(mListView, mDatas);
		mAdapter.startObserver();
		mListView.setAdapter(mAdapter);
		return mListView;
	}

	@Override
	protected LoadResult load() {
		GameProtocol protocol = new GameProtocol();
		mDatas = protocol.load(0);
		return check(mDatas);
	}

	class GameAdapter extends ListBaseAdapter {

		private GameAdapter(AbsListView listView, List<AppInfo> datas) {
			super(listView, datas);
		}

		/* 加载更多 */
		@Override
		public List<AppInfo> onLoadMore() {
			GameProtocol protocol = new GameProtocol();
			return protocol.load(getData().size());
		}

	}
}
