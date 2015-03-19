package com.ziven.androidmarket.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ziven.androidmarket.ui.holder.BaseHolder;
import com.ziven.androidmarket.ui.holder.MoreHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

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

}
