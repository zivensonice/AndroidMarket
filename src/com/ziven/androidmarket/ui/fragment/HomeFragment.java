package com.ziven.androidmarket.ui.fragment;

import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.ziven.androidmarket.protocol.HomeProtocol;
import com.ziven.androidmarket.ui.adapter.ListBaseAdapter;
import com.ziven.androidmarket.ui.holder.HomePictureHolder;
import com.ziven.androidmarket.ui.view.LoadingPage.LoadResult;
import com.ziven.androidmarket.ui.widget.BaseListView;
import com.ziven.androidmarket.ui.widget.InterceptorFrame;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.AppInfo;

public class HomeFragment extends BaseFragment {
  private HomePictureHolder mHolder;
  private List<String> mPicture;
  private BaseListView mListView;
  private List<AppInfo> mDatas;
  private HomeAdapter mAdapter;

  @Override
  protected View createSucceedView() {
    mListView = new BaseListView(UIUtils.getContext());
    if (mPicture != null && mPicture.size() > 0) {
      mHolder = new HomePictureHolder();
      mHolder.setData(mPicture);
      mListView.addHeaderView(mHolder.getRootView());
    }
    mAdapter = new HomeAdapter(mListView, mDatas);

    mListView.setAdapter(mAdapter);
    if (mHolder != null) {
      InterceptorFrame frame = new InterceptorFrame(UIUtils.getContext());
      frame.addInterceptorView(mHolder.getRootView(), InterceptorFrame.ORIENTATION_LEFT
          | InterceptorFrame.ORIENTATION_RIGHT);
      frame.addView(mListView);
      return frame;
    } else {
      return mListView;
    }
  }

  @Override
  protected LoadResult load() {
    HomeProtocol protocol = new HomeProtocol();
    mDatas = protocol.load(0);
    mPicture = protocol.getPictureUrl();
    return check(mDatas);
  }

  /** 可见时,启动监听,以便随时根据下载状态刷新页面 */
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

  class HomeAdapter extends ListBaseAdapter {

    public HomeAdapter(ListView listView, List<AppInfo> datas) {
      super(listView, datas);
    }

    @Override
    public List<AppInfo> onLoadMore() {
      HomeProtocol protocol = new HomeProtocol();
      return protocol.load(getData().size());
    }

  }
}
