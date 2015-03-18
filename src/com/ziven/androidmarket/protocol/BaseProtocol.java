package com.ziven.androidmarket.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.ziven.androidmarket.base.Constant;
import com.ziven.androidmarket.http.HttpHelper;
import com.ziven.androidmarket.http.HttpHelper.HttpResult;
import com.ziven.androidmarket.utils.FileUtils;
import com.ziven.androidmarket.utils.IOUtils;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.StringUtils;

import android.os.SystemClock;

/*协议访问基类*/
public abstract class BaseProtocol<T> implements Constant {

	/* 加载数据 */
	public T load(int index) {
		// 数据加载过快
		SystemClock.sleep(1000);
		String json = null;
		// 从本地获取数据
		json = loadFromLocal(index);
		if (StringUtils.isEmpty(json)) {
			// 从网络获取数据
			json = loadFromNet(index);
			if (null == json) {
				return null;
			} else {
				// 加入本地文件缓存
				saveToLoacl(json, index);
			}
		}
		// 获取数据实体
		return parseFromJson(json);

	}

	public String loadFromLocal(int index) {
		String path = FileUtils.getCacheDir();
		BufferedReader reader = null;
		try {
			// 因为创建文件的时候不能包含特殊字符,所以使用_代替?index=
			reader = new BufferedReader(new FileReader(path + getKey() + "_" + index + getParams()));
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
	public String loadFromNet(int index) {
		String result = null;
		HttpResult httpResult = HttpHelper.get(IP + getKey() + "?index=" + index);
		if (httpResult != null) {
			result = httpResult.getString();
			httpResult.close();
		}
		return result;
	}

	public void saveToLoacl(String json, int index) {
		String path = FileUtils.getCacheDir();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path + getKey() + "_" + index + getParams()));
			// 设置过期时间,为1天
			long time = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
			// 反斜杠转义,表示Enter键 换行
			writer.write(time + "\r\n");
			writer.write(json.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(writer);
		}
	}

	/* 需要增加的额外参数 */
	protected String getParams() {
		return "";
	}

	public abstract String getKey();

	/* 从数据实体中解析数据 */
	public abstract T parseFromJson(String jsonStr);
}
