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

public class AppFragment extends BaseFragment {
	private List<AppInfo> mDatas;
	private BaseListView mListView;
	private AppAdapter mAdapter;

	@Override
	protected View createSucceedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mAdapter = new AppAdapter(mListView, mDatas);
		
	}

	@Override
	protected LoadResult load() {
		AppProtocol protocol = new AppProtocol();
		mDatas = protocol.load(0);
		return check(mDatas);
	}

	class AppAdapter extends ListBaseAdapter {

		private AppAdapter(AbsListView listView, List<AppInfo> datas) {
			super(listView, datas);
		}

		@Override
		public List<AppInfo> onLoadMore() {
			// TODO Auto-generated method stub
			return super.onLoadMore();
		}

	}
}
