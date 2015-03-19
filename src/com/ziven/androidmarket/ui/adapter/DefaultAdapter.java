package com.ziven.androidmarket.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ziven.androidmarket.ui.holder.BaseHolder;
import com.ziven.androidmarket.ui.holder.MoreHolder;
import com.ziven.manager.ThreadManager;

import android.database.DataSetObservable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public abstract class DefaultAdapter<T> extends BaseAdapter implements
		RecyclerListener, OnItemClickListener {
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
			return mT.size() + 1;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder<T> holder;
		// 第一次加载否
		if (convertView != null
				&& convertView.getTag() instanceof BaseHolder<?>) {
			holder = (BaseHolder<T>) convertView.getTag();
		} else {
			// 加载的类型
			if (getItemViewType(position) == MORE_VIEW_TYPE) {
				holder = getMoreHolder();
			} else {
				holder = getHolder();
			}
		}
		// 如果是普通类型的item就把它设置到mT中
		if (getItemViewType(position) == ITEM_VIEW_TYPE) {
			holder.setT(mT.get(position));
		}
		// 加入当前视窗,维护当前视窗的视图队列
		mDispalyHolders.add(holder);
		return holder.getRootView();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 此时的position是加上了header的
		position = position - getHeaderViewCount();
		onItemClickInner(position);
	}

	/** 根据position的位置返回那种item展示类型 */
	@Override
	public int getItemViewType(int position) {
		if (position == getCount() - 1) {
			return MORE_VIEW_TYPE;
		} else {
			return getItemViewTypeInnter(position);
		}
	}

	private int getItemViewTypeInnter(int position) {
		return ITEM_VIEW_TYPE;
	}

	/** 获取item有几种类型,默认是一种,加一是为了加载更多 */
	@Override
	public int getViewTypeCount() {
		// 加1是为了,最后加载更多的布局
		return super.getViewTypeCount() + 1;
	}

	public int getHeaderViewCount() {
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
					if (mT == null) {
						getMoreHolder().setT(MoreHolder.ERROR);
					} else if (mT.size() < 20) {
						getMoreHolder().setT(MoreHolder.NO_MORE);
					} else {
						getMoreHolder().setT(MoreHolder.HAS_MORE);
					}
					if (mT != null) {
						if (getT() != null) {
							// 如果adapter数据集不为空,就添加到后面
							getT().addAll(mT);
						} else {
							// 如果adapter数据集为空,就设置数据到他的数据集
							setT(mT);
						}
					}
					// 数据改变通知界面更新
					notifyDataSetChanged();
					mIsLoading = false;
				}
			});
		}
	}

	public BaseHolder getMoreHolder() {
		if (mMoreHolder == null) {
			mMoreHolder = new MoreHolder(this, hasMore());
		}
		return mMoreHolder;
	}

	public boolean hasMore() {
		return true;
	}

	public abstract List<T> onLoadMore();

	public abstract void onItemClickInner(int position);

	protected abstract BaseHolder getHolder();

}
