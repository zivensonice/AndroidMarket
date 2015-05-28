package com.ziven.androidmarket.ui.fragment;

import java.util.List;

import com.ziven.androidmarket.protocol.AppProtocol;
import com.ziven.androidmarket.ui.adapter.ListBaseAdapter;
import com.ziven.androidmarket.ui.view.LoadingPage.LoadResult;
import com.ziven.androidmarket.ui.widget.BaseListView;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.AppInfo;

import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class AppFragment extends BaseFragment {
	private List<AppInfo> mDatas;
	private BaseListView mListView;
	private AppAdapter mAdapter;

	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.stopObserver();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) {
			mAdapter.startObserver();
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected View createSucceedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mAdapter = new AppAdapter(mListView, mDatas);
		mAdapter.startObserver();
		return mListView;
	}

	@Override
	protected LoadResult load() {
		AppProtocol protocol = new AppProtocol();
		mDatas = protocol.load(0);
		return check(mDatas);
	}

	class AppAdapter extends ListBaseAdapter {

		public AppAdapter(ListView listView, List<AppInfo> datas) {
			super(listView, datas);
		}

		@Override
		public List<AppInfo> onLoadMore() {
			AppProtocol protocol = new AppProtocol();
			return protocol.load(getData().size());
		}

	}
}
