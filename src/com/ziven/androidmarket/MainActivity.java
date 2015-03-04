package com.ziven.androidmarket;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_main);
		// 找到侧滑菜单的ID
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(new MyDrawerListener());

	}

	@Override
	protected void initActionBar() {
		// 获取到包里面的actionbar
		mActionBar = getSupportActionBar();
		mActionBar.setTitle(R.string.app_name);
		mActionBar.setHomeButtonEnabled(true);
		// TODO试验这两个函数的用处
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_am,
				R.string.drawer_open, R.string.drawer_close);
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

		private ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return super.getPageTitle(position);
		}

		@Override
		public Fragment getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return 0;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

	}
}
