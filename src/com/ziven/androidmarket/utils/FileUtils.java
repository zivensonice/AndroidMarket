package com.ziven.androidmarket.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

public class FileUtils {
	public static final String ROOT_DIR = "zr";
	public static final String DOWNLOAD_DIR = "download";
	public static final String CACHE_DIR = "cache";
	public static final String ICON_DIR = "icon";

	/** 获取下载目录 */
	public static String getDownloadDir() {
		return getDir(DOWNLOAD_DIR);
	}

	/** 获取缓存目录 */
	public static String getCacheDir() {
		return getDir(CACHE_DIR);
	}

	/**
	 * 获取icon目录
	 * 
	 * @return
	 */
	public static String getIconDir() {
		return getDir(ICON_DIR);
	}

	private static String getDir(String name) {
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath());
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param dirPath
	 * @return
	 */
	public static boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}

	/**
	 * 获取应用的cache目录
	 * 
	 * @return
	 */
	public static String getCachePath() {
		File f = UIUtils.getContext().getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + File.separator;
		}
	}

	/**
	 * 获取SD卡下面的应用目录 结尾带separator
	 * 
	 * @return
	 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/**
	 * 判断SD卡是否挂载
	 * 
	 * @return
	 */
	public static boolean isSDCardAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 把数据写入文件
	 * 
	 * @param is
	 *            数据流
	 * @param path
	 *            文件路径
	 * @param recreate
	 *            如果文件存在,是否需要删除重建
	 * @return 是否写入成功
	 */
	public static boolean writeFile(InputStream is, String path, boolean recreate) {
		boolean res = false;
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			if (recreate && f.exists()) {
				f.delete();
			}
			if (!f.exists() && null != is) {
				File parentFile = new File(f.getParent());
				parentFile.mkdirs();
				int count = -1;
				byte[] buffer = new byte[1024];
				fos = new FileOutputStream(f);
				while ((count = is.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
				}
				res = true;
			}
		} catch (Exception e) {
			L.e(e);
		} finally {
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				L.e(e);
			}
		}
		return res;
	}

	/** 改名 */
	public static boolean copy(String src, String des, boolean delete) {
		File file = new File(src);
		if (!file.exists()) {
			return false;
		}
		File desFile = new File(des);
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new FileOutputStream(desFile);
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
				out.flush();
			}
		} catch (Exception e) {
			L.e(e);
			return false;
		} finally {
			IOUtils.close(in);
			IOUtils.close(out);
		}
		if (delete) {
			file.delete();
		}
		return true;
	}
}
