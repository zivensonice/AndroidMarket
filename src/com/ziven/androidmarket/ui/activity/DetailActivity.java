package com.ziven.androidmarket.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;

import com.ziven.androidmarket.R;
import com.ziven.androidmarket.protocol.DetailProtocol;
import com.ziven.androidmarket.ui.holder.AppDetailInfoHolder;
import com.ziven.androidmarket.ui.view.LoadingPage;
import com.ziven.androidmarket.ui.view.LoadingPage.LoadResult;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.StringUtils;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.AppInfo;


public class DetailActivity extends BaseActivity {

  public static final String PACKAGENAME = "PACKAGENAME";

  private AppInfo mAppInfo;
  private String mPackageName;
  private ActionBar mActionBar;

  private FrameLayout mInfoLayout;
  private AppDetailInfoHolder mInfoHolder;

  @Override
  protected void init() {
    Intent intent = getIntent();
    if (intent != null) {
      mPackageName = intent.getStringExtra(PACKAGENAME);
      L.d(mPackageName);
    }
  }

  @Override
  protected void initView() {
    LoadingPage page = new LoadingPage(UIUtils.getContext()) {

      @Override
      public LoadResult load() {
        return DetailActivity.this.load();
      }

      @Override
      public View createLoadedView() {
        return DetailActivity.this.createLoadedView();
      }
    };
  }

  private LoadResult load() {
    DetailProtocol protocol = new DetailProtocol();
    protocol.setPackageName(mPackageName);
    mAppInfo = protocol.load(0);
    if (mAppInfo == null || StringUtils.isEmpty(mAppInfo.getPackageName())) {
      return LoadResult.ERROR;
    }
    return LoadResult.SUCCEED;
  }

  private View createLoadedView() {
    View view = UIUtils.inflate(R.layout.activity_detail);

    mInfoLayout = (FrameLayout) view.findViewById(R.id.detail_info_layout);
    mInfoHolder = new AppDetailInfoHolder();
    mInfoHolder.setData(mAppInfo);
    mInfoLayout.addView(mInfoHolder.getRootView());

    return view;
  }

  @Override
  protected void initActionBar() {
    mActionBar = getSupportActionBar();
    mActionBar.setTitle(R.string.app_detail);

    mActionBar.setDisplayHomeAsUpEnabled(true);
    mActionBar.setHomeButtonEnabled(true);
    mActionBar.setDisplayHomeAsUpEnabled(true);
  }



}
