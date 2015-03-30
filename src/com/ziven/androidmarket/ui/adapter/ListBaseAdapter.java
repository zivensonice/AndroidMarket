package com.ziven.androidmarket.ui.adapter;

import java.util.List;

import android.widget.AbsListView;

import com.ziven.androidmarket.ui.holder.BaseHolder;
import com.ziven.bean.AppInfo;
import com.ziven.bean.DownloadInfo;
import com.ziven.manager.DownloadManager.DownloadObserver;

public class ListBaseAdapter extends DefaultAdapter<AppInfo> implements
		DownloadObserver {

	public ListBaseAdapter(AbsListView listView, List<AppInfo> datas) {
		super(listView, datas);
	}

	@Override
	public void onDownloadStateChanged(DownloadInfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownloadProgressed(DownloadInfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	protected BaseHolder getHolder() {
		// TODO Auto-generated method stub
		return null;
	}

}
