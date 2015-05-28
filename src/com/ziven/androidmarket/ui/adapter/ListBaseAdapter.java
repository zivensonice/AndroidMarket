package com.ziven.androidmarket.ui.adapter;

import java.util.List;

import android.content.Intent;
import android.widget.ListView;

import com.ziven.androidmarket.ui.activity.DetailActivity;
import com.ziven.androidmarket.ui.holder.BaseHolder;
import com.ziven.androidmarket.ui.holder.ListBaseHolder;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.AppInfo;
import com.ziven.bean.DownloadInfo;
import com.ziven.manager.DownloadManager;
import com.ziven.manager.DownloadManager.DownloadObserver;

public abstract class ListBaseAdapter extends DefaultAdapter<AppInfo> implements DownloadObserver {

  public ListBaseAdapter(ListView listView, List<AppInfo> datas) {
    super(listView, datas);
  }

  @Override
  public void onDownloadStateChanged(DownloadInfo info) {
    refreshHolder(info);
  }

  @Override
  public void onDownloadProgressed(final DownloadInfo info) {
    refreshHolder(info);
  }

  @Override
  protected BaseHolder getHolder() {
    return new ListBaseHolder();
  }

  public void startObserver() {
    DownloadManager.getInstance().registerObserver(this);
  }

  public void stopObserver() {
    DownloadManager.getInstance().unregisterObserver(this);
  }

  @Override
  public void onItemClickInner(int position) {
    List<AppInfo> data = getData();
    if (position < data.size()) {
      Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
      intent.putExtra(DetailActivity.PACKAGENAME, data.get(position).getPackageName());
      UIUtils.startActivity(intent);
    }
  }

  @SuppressWarnings("rawtypes")
  private void refreshHolder(final DownloadInfo info) {
    L.d(info.toString());
    L.d("进度:" + info.getProgress());
    List<BaseHolder> displayHolders = getDisplayedHolders();
    for (int i = 0; i < displayHolders.size(); i++) {
      BaseHolder baseHolder = displayHolders.get(i);
      if (baseHolder instanceof ListBaseHolder) {
        final ListBaseHolder holder = (ListBaseHolder) baseHolder;
        AppInfo appInfo = holder.getData();
        if (appInfo.getId() == info.getId()) {
          UIUtils.post(new Runnable() {
            @Override
            public void run() {
              holder.refreshState(info.getDownloadState(), info.getProgress());
            }
          });
        }
      }
    }
  }
}
