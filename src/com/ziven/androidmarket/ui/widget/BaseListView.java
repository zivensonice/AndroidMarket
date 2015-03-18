package com.ziven.androidmarket.ui.widget;

import com.ziven.androidmarket.R;
import com.ziven.androidmarket.utils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class BaseListView extends ListView {

	private BaseListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private BaseListView(Context context) {
		super(context);
	}

	private void init() {
		// 分割线
		setDivider(UIUtils.getResources().getDrawable(R.drawable.nothing));
		// 快速拖动缓存样式背景
		setCacheColorHint(UIUtils.getColor(R.color.bg_page));
		// 选择器/选择背景
		setSelector(UIUtils.getResources().getDrawable(R.drawable.nothing));
	}
}
