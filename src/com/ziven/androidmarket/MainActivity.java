package com.ziven.androidmarket;

import ziven.ui.widget.PagerTab;

import com.ziven.androidmarket.fragment.BaseFragment;
import com.ziven.androidmarket.fragment.FragmentFactory;
import com.ziven.androidmarket.utils.UIUtils;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * @author Ziven
 * 
 */
public class MainActivity extends BaseActivity implements OnPageChangeListener {

	private ActionBar mActionBar;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mActionBarDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void init() {

	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_main);
		// 找到侧滑菜单的ID
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// 设置侧滑菜单的监听器
		mDrawerLayout.setDrawerListener(new MyDrawerListener());
		PagerTab tabs = (PagerTab) findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		// 设置ViewPager适配器
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		// 把ViewPager和他的指针进行绑定
		tabs.setViewPager(pager);
		// 因为MainActivity实现了OnPageChangeListener监听接口,所以可以使用this
		tabs.setOnPageChangeListener(this);
	}

	@Override
	protected void initActionBar() {
		// 获取到包里面的actionbar
		mActionBar = getSupportActionBar();
		mActionBar.setTitle(R.string.app_name);
		// 设置Actionbar指示部分能否点击
		mActionBar.setHomeButtonEnabled(true);
		// 显示图标
		mActionBar.setDisplayHomeAsUpEnabled(true);
		// 显示APP名称
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_am,
				R.string.drawer_open, R.string.drawer_close);
		// 关联同步
		mActionBarDrawerToggle.syncState();
	}

	private class MyDrawerListener implements DrawerListener {

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mActionBarDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			mActionBarDrawerToggle.onDrawerOpened(drawerView);
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mActionBarDrawerToggle.onDrawerClosed(drawerView);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mActionBarDrawerToggle.onDrawerStateChanged(newState);
		}

	}

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {

		private String[] tab_names;

		private ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// 初始化指示控件标题名称
			tab_names = UIUtils.getsStringArray(R.array.tab_names);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tab_names[position];
		}

		@Override
		public Fragment getItem(int position) {
			// TODO:添加item实例
			return null;
		}

		@Override
		public int getCount() {
			return tab_names.length;
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		BaseFragment fragment = FragmentFactory.createFragment(state);
	}

}
