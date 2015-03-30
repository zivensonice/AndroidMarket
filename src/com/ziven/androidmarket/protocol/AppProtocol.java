package com.ziven.androidmarket.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ziven.androidmarket.utils.L;
import com.ziven.bean.AppInfo;

public class AppProtocol extends BaseProtocol<List<AppInfo>> {

	@Override
	public String getKey() {
		return "app";
	}

	@Override
	public List<AppInfo> parseFromJson(String jsonStr) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(jsonStr, new TypeToken<List<AppInfo>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
			L.e(e);
			return null;
		}
	}
}
