package com.ziven.androidmarket.ui.fragment;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.ziven.androidmarket.ui.adapter.DefaultAdapter;
import com.ziven.androidmarket.ui.holder.BaseHolder;
import com.ziven.androidmarket.ui.view.LoadingPage.LoadResult;
import com.ziven.androidmarket.ui.widget.BaseListView;
import com.ziven.bean.CategoryInfo;

public class CategoryFragment extends BaseFragment {

	private BaseListView mListView = null;
	private CategoryAdapter mAdapter = null;
	private List<CategoryInfo> mDatas = null;

	@Override
	protected View createSucceedView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LoadResult load() {
		// TODO Auto-generated method stub
		return null;
	}

	public class CategoryAdapter extends DefaultAdapter<CategoryInfo> {
		private int mCurrentPosition;

		public CategoryAdapter(AbsListView listView, List<CategoryInfo> datas) {
			super(listView, datas);
		}

		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1;
		}

		@Override
		public boolean hasMore() {
			return false;
		}

		@Override
		public void onItemClickInner(int position) {

		}

		@Override
		public int getItemViewTypeInner(int position) {
			CategoryInfo groupInfo = getData().get(position);
			if (groupInfo.isTitle()) {
				return super.getItemViewTypeInner(position) + 1;
			} else {
				return super.getItemViewTypeInner(position);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			mCurrentPosition = position;
			return super.getView(position, convertView, parent);
		}

		@Override
		protected BaseHolder getHolder() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
