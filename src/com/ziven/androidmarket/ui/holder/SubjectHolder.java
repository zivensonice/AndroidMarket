package com.ziven.androidmarket.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziven.androidmarket.R;
import com.ziven.androidmarket.http.image.ImageLoader;
import com.ziven.androidmarket.utils.UIUtils;
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
		SubjectInfo data = getT();
		String des = data.getDes();
		String url = data.getUrl();
		tv.setText(des);
		iv.setTag(url);
		ImageLoader.load(iv, url);
	}

	@Override
	public void recycle() {
		recycleImageView(iv);
	}

}
