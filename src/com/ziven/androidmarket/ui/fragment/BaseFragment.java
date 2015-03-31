package com.ziven.androidmarket.ui.fragment;

import java.util.List;

import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.androidmarket.utils.ViewUtils;
import com.ziven.androidmarket.ui.view.LoadingPage;
import com.ziven.androidmarket.ui.view.LoadingPage.LoadResult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	private LoadingPage mContentPager;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentPager == null) {
			mContentPager = new LoadingPage(UIUtils.getContext()) {
				@Override
				public LoadResult load() {
					return BaseFragment.this.load();
				}

				@Override
				public View createLoadedView() {
					return BaseFragment.this.createSucceedView();
				}
			};
		} else {
			ViewUtils.removeSelfFromParent(mContentPager);
		}
		return mContentPager;
	}

	public void show() {
		if (null != mContentPager) {
			mContentPager.show();
		}
	}

	public LoadResult check(Object obj) {
		if (null == obj) {
			return LoadResult.ERROR;
		}
		if (obj instanceof List) {
			List<?> list = (List<?>) obj;
			if (list.size() == 0) {
				return LoadResult.EMPTY;
			}
		}
		return LoadResult.SUCCEED;
	}

	/* 都取决于网络获取数据,数据返回情况而定 */
	/* 初始化数据 */
	protected abstract View createSucceedView();

	/* 初始化界面 */
	protected abstract LoadResult load();

}
