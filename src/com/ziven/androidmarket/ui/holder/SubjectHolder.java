package com.ziven.androidmarket.ui.holder;

import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.UIUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziven.androidmarket.R;
import com.ziven.androidmarket.http.image.ImageLoader;
import com.ziven.bean.SubjectInfo;

public class SubjectHolder extends BaseHolder<SubjectInfo> {
	private ImageView iv;
	private TextView tv;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.subject_item);
		iv = (ImageView) view.findViewById(R.id.item_icon);
		tv = (TextView) view.findViewById(R.id.item_txt);
		return view;
	}

	@Override
	public void refreshView() {
		SubjectInfo data = getData();
		String des = data.getDes();
		String url = data.getUrl();
		tv.setText(des);
		iv.setTag(url);
		L.d("refreshView url:" + url);
		ImageLoader.load(iv, url);
	}

	@Override
	public void recycle() {
		recycleImageView(iv);
	}

}
