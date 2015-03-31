package com.ziven.androidmarket.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ziven.androidmarket.utils.L;
import com.ziven.bean.AppInfo;

public class HomeProtocol extends BaseProtocol<List<AppInfo>> {
	private List<String> mPictureUrl;

	@Override
	public String getKey() {
		return "home";
	}

	public List<String> getPictureUrl() {
		return mPictureUrl;
	}

	@Override
	public List<AppInfo> parseFromJson(String jsonStr) {
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			mPictureUrl = new ArrayList<String>();
			JSONArray array = jsonObject.optJSONArray("picture");
			if (array != null) {
				for (int index = 0; index < array.length(); index++) {
					mPictureUrl.add(array.getString(index));
				}
			}
			array = jsonObject.optJSONArray("list");
			Gson gson = new Gson();
			List<AppInfo> datas = gson.fromJson(array.toString(), new TypeToken<List<AppInfo>>() {
			}.getType());
			L.d("datas length: " + datas.size());
			return datas;
		} catch (Exception e) {
			L.e(e);
			return null;
		}
	}
}
