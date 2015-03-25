package com.ziven.androidmarket.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ziven.androidmarket.ui.holder.BaseHolder;
import com.ziven.androidmarket.ui.holder.MoreHolder;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.bean.SubjectInfo;
import com.ziven.manager.ThreadManager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public abstract class DefaultAdapter<T> extends BaseAdapter implements RecyclerListener, OnItemClickListener {

	public static final int MORE_VIEW_TYPE = 0;
	public static final int ITEM_VIEW_TYPE = 1;
	// 和该adapter关联的listview
	protected AbsListView mListView;
	// 用于记录所有显示的Holder
	private List<BaseHolder> mDispalyHolders;
	private List<T> mT;// adapter数据集
	// 用于线程同步,展示是否被加载
	private volatile boolean mIsLoading;
	private MoreHolder mMoreHolder;

	public DefaultAdapter(AbsListView listView, List<T> t) {
		mDispalyHolders = new ArrayList<BaseHolder>();
		if (null != listView) {
			mListView = listView;
			listView.setRecyclerListener(this);
			listView.setOnItemClickListener(this);
		}
		setT(t);
	}

	public void setT(List<T> t) {
		mT = t;
	}

	public List<T> getT() {
		return mT;
	}

	@Override
	public int getCount() {
		if (mT != null) {
			return mT.size() + 1;// 为了最后加载更多的布局
		}
		return 0;
	}

	@Override
	public T getItem(int position) {
		if (mT != null && position < mT.size()) {
			return mT.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取item有几种类型,默认是1中,+1表示加载更多的类型
	 */
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;// 加载更多的布局
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder<T> holder;
		if (convertView != null && convertView.getTag() instanceof BaseHolder) {
			holder = (BaseHolder<T>) convertView.getTag();
		} else {
			if (getItemViewType(position) == MORE_VIEW_TYPE) {
				holder = getMoreHolder();
			} else {
				holder = getHolder();
			}
		}
		if (getItemViewType(position) == ITEM_VIEW_TYPE) {
			holder.setT(mT.get(position));
		}
		mDispalyHolders.add(holder);
		return holder.getRootView();
	}

	public BaseHolder getMoreHolder() {
		if (mMoreHolder == null) {
			mMoreHolder = new MoreHolder(this, hasMore());
		}
		return mMoreHolder;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position = position - getHeadItemCount();
		onItemtInner(position);
	}

	public int getHeadItemCount() {
		int count = 0;
		if (mListView != null && mListView instanceof ListView) {
			ListView listView = (ListView) mListView;
			count = listView.getHeaderViewsCount();
		}
		return count;
	}

	@Override
	public void onMovedToScrapHeap(View view) {
		if (null != view) {
			Object tag = view.getTag();
			if (tag instanceof BaseHolder<?>) {
				BaseHolder<?> holder = (BaseHolder<?>) tag;
				synchronized (mDispalyHolders) {
					mDispalyHolders.remove(holder);
				}
				holder.recycle();
			}
		}
	}

	public List<BaseHolder> getDisplayedHolders() {
		synchronized (mDispalyHolders) {
			return new ArrayList<BaseHolder>(mDispalyHolders);
		}
	}

	public void loadMore() {
		if (!mIsLoading) {
			mIsLoading = true;
			ThreadManager.getLongPool().execute(new Runnable() {

				@Override
				public void run() {
					final List<T> t = onLoadMore();
					UIUtils.post(new Runnable() {

						@Override
						public void run() {
							if (t == null) {
								getMoreHolder().setT(MoreHolder.ERROR);
							} else if (mT.size() < 20) {
								getMoreHolder().setT(MoreHolder.NO_MORE);
							} else {
								getMoreHolder().setT(MoreHolder.HAS_MORE);
							}
							if (t != null) {
								if (getT() != null) {
									getT().addAll(t);
								} else {
									setT(t);
								}
							}
							notifyDataSetChanged();
							mIsLoading = false;
						}
					});
				}
			});
		}
	}

	/* 需要重写的方法 */
	public boolean hasMore() {
		return true;
	}

	public List<T> onLoadMore() {
		return null;
	}

	public void onItemtInner(int position) {
	}

	public abstract BaseHolder getHolder();

}
