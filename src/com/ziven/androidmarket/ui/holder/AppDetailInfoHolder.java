package com.ziven.androidmarket.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ziven.androidmarket.R;
import com.ziven.androidmarket.http.image.ImageLoader;
import com.ziven.androidmarket.utils.StringUtils;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.AppInfo;

public class AppDetailInfoHolder extends BaseHolder<AppInfo> {

  private ImageView mIcon;
  private TextView mTitleTv, mDownloadTv, mVersionTv, mDateTv, mSizeTv;
  private RatingBar rateRb;

  @Override
  protected View initView() {
    View view = UIUtils.inflate(R.layout.app_detail_info);
    mIcon = (ImageView) view.findViewById(R.id.item_icon);
    mTitleTv = (TextView) view.findViewById(R.id.item_title);
    mDownloadTv = (TextView) view.findViewById(R.id.item_download);
    mVersionTv = (TextView) view.findViewById(R.id.item_version);
    mDateTv = (TextView) view.findViewById(R.id.item_date);
    mSizeTv = (TextView) view.findViewById(R.id.item_size);
    rateRb = (RatingBar) view.findViewById(R.id.item_rating);
    return view;
  }

  @Override
  public void refreshView() {
    AppInfo appInfo = getData();
    String url = appInfo.getIconUrl();
    mIcon.setTag(url);
    ImageLoader.load(mIcon, url);

    rateRb.setRating(appInfo.getStars());
    mTitleTv.setText(appInfo.getName());
    mDownloadTv.setText(UIUtils.getString(R.string.app_detail_download) + appInfo.getDownloadNum());
    mVersionTv.setText(UIUtils.getString(R.string.app_detail_version) + appInfo.getVersion());
    mDateTv.setText(UIUtils.getString(R.string.app_detail_date) + appInfo.getDate());
    mSizeTv.setText(UIUtils.getString(R.string.app_detail_size)
        + StringUtils.formatFileSize(appInfo.getSize()));
  }

}
