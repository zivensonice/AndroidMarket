package com.ziven.androidmarket.http.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ziven.androidmarket.utils.DrawableUtils;
import com.ziven.androidmarket.utils.FileUtils;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.StringUtils;
import com.ziven.androidmarket.utils.SystemUtils;
import com.ziven.androidmarket.utils.UIUtils;
import com.ziven.manager.ThreadManager;
import com.ziven.manager.ThreadManager.ThreadPoolProxy;

import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.widget.ImageView;

public class ImageLoader {
	/* 图片下载线程池名称 */
	public static final String THREAD_POOL_NAME = "IMAGE_THREAD_POOL";
	/* 图片缓存最大数量 */
	public static final int MAX_DRAWABLE_COUNT = 100;
	/* 图片的KEY缓存 */
	private static ConcurrentLinkedQueue<String> mKeyCache = new ConcurrentLinkedQueue<String>();
	/* 图片的缓存 */
	private static Map<String, Drawable> mDrawableCache = new ConcurrentHashMap<String, Drawable>();

	private static BitmapFactory.Options mOptions = new BitmapFactory.Options();
	/* 图片下载线程池 */
	private static ThreadPoolProxy mThreadPool = ThreadManager
			.getSinglePool(THREAD_POOL_NAME);
	/* 用于记录图片下载任务,以便取消 */
	private static ConcurrentHashMap<String, Runnable> mMapRunnable = new ConcurrentHashMap<String, Runnable>();
	/* 图片的总大小 */
	private static long mTotalSize;

	static {
		mOptions.inDither = false;// 设置为false，将不考虑图片的抖动值，这会减少图片的内存占用
		mOptions.inPurgeable = true;// 设置为ture，表示允许系统在内存不足时，删除bitmap的数组。
		mOptions.inInputShareable = true;// 和inPurgeable配合使用，如果inPurgeable是false，那么该参数将被忽略，表示是否对bitmap的数组进行共享
	}

	public static void load(ImageView view, String url) {
		if (null == view || StringUtils.isEmpty(url)) {
			return;
		}
		// 把控件和图片url进行绑定,因为加载是一个耗时操作,等加载完成需要对url和控件进行匹配
		view.setTag(url);
		// 从内存中加载
		Drawable drawable = loadFromMemory(url);
		if (drawable != null) {
			// 如果内存中加载到了就直接设置图片
			setImageSafe(view, url, drawable);
		} else {
			// 如果没有加载到,就设置一张默认图片,进行异步加载
			view.setImageResource(com.ziven.androidmarket.R.drawable.ic_default);
			asyncLoad(view, url);
		}
	}

	private static void asyncLoad(final ImageView view, final String url) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				mMapRunnable.remove(url);
				Drawable drawable = loadFromLocal(url);
				if (null == drawable) {
					drawable = loadFromNet(url);
				}
				if (null != drawable) {
					setImageSafe(view, url, drawable);
				}
			}
		};
		// 取消下载
		cancel(url);
		// 放入缓存
		mMapRunnable.put(url, runnable);
		// 执行任务
		mThreadPool.execute(runnable);
	}

	/* 从网络加载 */
	public static Drawable loadFromNet(String url) {
		return null;
	}

	/* 本地文件加载 */
	protected static Drawable loadFromLocal(String url) {
		Bitmap bitmap = null;
		Drawable drawable = null;
		String path = FileUtils.getIconDir();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(new File(path + url));
			if (fis != null) {
				// BitmapFactory.decodeByteArray(data, offset, length);
				// BitmapFactory.decodeFile(pathName)
				// BitmapFactory.decodeStream(is)
				// 对比下面的处理方式,分析代码可知,他们都是在java层创建的byte数组,然后把数据作为数组传递给本地代码
				// 下面这个是把文件描述符传递给本地代码,通过本地代码去创建图片
				// 优点,由于本地代码创建的,那么byte数组的内存占用不会算到应用内存中,并且内存一旦不足,只会回收掉bitmap数组,不会影响bitmap
				// 当显示的时候,发现bitmap数组为空,将在此根据文件描述符去加载图片,此时可能会卡顿,但是胜于OOM
				// 而且图片一定要进行完整性校验

				// 可以避免OOM 比起decodeStream和decodeFile
				bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(), null,
						mOptions);
			}
			if (null != bitmap) {
				drawable = new BitmapDrawable(UIUtils.getResources(), bitmap);
			}
			if (drawable != null) {
				addDrawableToMemory(url, drawable);
			}
		} catch (Exception e) {
			L.e(e);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				L.e(e);
			}
		}
		return drawable;
	}

	/* 取消下载 */
	public static void cancel(String url) {
		Runnable runnable = mMapRunnable.remove(url);
		if (null != runnable) {
			mThreadPool.cancel(runnable);
		}
	}

	/**
	 * 给控件设置图片
	 * 
	 * @param view
	 * @param url
	 *            用来校验设置图片是否安全
	 * @param drawable
	 */
	private static void setImageSafe(final ImageView view, final String url,
			final Drawable drawable) {
		if (null == drawable && null == view.getTag()) {
			return;
		}
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				// 在主线程中判断,可以做到同步
				Object tag;
				if ((tag = view.getTag()) != null) {
					String str = (String) tag;
					if (StringUtils.isEquals(str, url)) {
						view.setImageDrawable(drawable);
					}
				}
			}
		});
	}

	/* 从内存中加载 */
	private static Drawable loadFromMemory(String url) {
		Drawable drawable = mDrawableCache.get(url);
		if (drawable != null) {
			// 在内存中获取到了,需要重新放到内存队列的最后
			addDrawableToMemory(url, drawable);
		}
		return drawable;
	}

	/**
	 * 图片添加到内存
	 * 
	 * @param url
	 * @param drawable
	 */
	private static void addDrawableToMemory(String url, Drawable drawable) {
		mKeyCache.remove(url);
		mDrawableCache.remove(url);
		// 如果大于等于100张,或者图片的总数大于应用总内存的1/4,先删除最前面的
		while (mKeyCache.size() >= MAX_DRAWABLE_COUNT
				|| mTotalSize >= SystemUtils.getOneAppMaxMemory() / 4) {
			String firstUrl = mKeyCache.remove();
			Drawable remove = mDrawableCache.remove(firstUrl);
			mTotalSize -= DrawableUtils.getDrawableSize(remove);
		}
		mKeyCache.add(url);
		mDrawableCache.put(url, drawable);
		mTotalSize += DrawableUtils.getDrawableSize(drawable);
	}

}
