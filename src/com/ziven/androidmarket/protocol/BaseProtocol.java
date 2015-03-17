package com.ziven.androidmarket.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.ziven.androidmarket.http.HttpHelper;
import com.ziven.androidmarket.http.HttpHelper.HttpResult;
import com.ziven.androidmarket.utils.FileUtils;
import com.ziven.androidmarket.utils.IOUtils;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.StringUtils;

import android.os.SystemClock;

/*协议访问基类*/
public abstract class BaseProtocol<T> {

	/* 加载数据 */
	public T load(String urlStr) {
		// 数据加载过快
		SystemClock.sleep(1000);
		String json = null;
		// 从本地获取数据
		json = loadFromLocal(urlStr);
		if (StringUtils.isEmpty(json)) {
			// 从网络获取数据
			json = loadFromNet(urlStr);
			if (null == json) {
				return null;
			} else {
				// 加入本地文件缓存
				saveToLoacl(json, urlStr);
			}
		}
		// 获取数据实体
		return parseFromJson(json);

	}

	public String loadFromLocal(String urlStr) {
		String path = FileUtils.getCacheDir();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path + getKey() + urlStr));
			long time = Long.valueOf(reader.readLine());
			// 判断时间未过期
			if (time > SystemClock.currentThreadTimeMillis()) {
				StringBuilder sb = new StringBuilder();
				String data;
				while ((data = reader.readLine()) != null) {
					sb.append(data);
				}
				return sb.toString();
			}
		} catch (IOException e) {
			L.e(e);
		} finally {
			IOUtils.close(reader);
		}
		return null;
	}

	// 从网络加载
	public String loadFromNet(String urlStr) {
		String result = null;
		HttpResult httpResult = HttpHelper
		return null;
	}

	public void saveToLoacl(String json, String urlStr) {
		// TODO Auto-generated method stub

	}

	public abstract String getKey();

	/* 从数据实体中解析数据 */
	public abstract T parseFromJson(String jsonStr);
}
