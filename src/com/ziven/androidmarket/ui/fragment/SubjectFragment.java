package com.ziven.androidmarket.ui.fragment;

import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.ziven.androidmarket.protocol.SubjectProtocol;
import com.ziven.androidmarket.ui.adapter.DefaultAdapter;
import com.ziven.androidmarket.ui.holder.BaseHolder;
import com.ziven.androidmarket.ui.holder.SubjectHolder;
import com.ziven.androidmarket.ui.view.LoadingPage.LoadResult;
import com.ziven.androidmarket.ui.widget.BaseListView;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.SubjectInfo;

public class SubjectFragment extends BaseFragment {
  private BaseListView mListView;
  private List<SubjectInfo> mDatas;
  private SubjectAdaptaer mAdapter;

  @Override
  protected View createSucceedView() {
    mListView = new BaseListView(UIUtils.getContext());
    mAdapter = new SubjectAdaptaer(mListView, mDatas);
    mListView.setAdapter(mAdapter);
    return mListView;
  }

  @Override
  protected LoadResult load() {
    SubjectProtocol protocol = new SubjectProtocol();
    mDatas = protocol.load(0);
    return check(mDatas);
  }

  class SubjectAdaptaer extends DefaultAdapter<SubjectInfo> {

    @Override
    public void onItemClickInner(int position) {
      UIUtils.showToastSafe(getItem(position).getDes());
    }

    public SubjectAdaptaer(ListView listView, List<SubjectInfo> t) {
      super(listView, t);
    }

    @Override
    public boolean hasMore() {
      return true;
    }

    @Override
    public List<SubjectInfo> onLoadMore() {
      SubjectProtocol protocol = new SubjectProtocol();
      return protocol.load(getData().size());
    }

    @Override
    public BaseHolder getHolder() {
      return new SubjectHolder();
    }

  }

}
