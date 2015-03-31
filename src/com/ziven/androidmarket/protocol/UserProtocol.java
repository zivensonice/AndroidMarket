package com.ziven.androidmarket.protocol;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ziven.androidmarket.utils.L;
import com.ziven.bean.UserInfo;

public class UserProtocol extends BaseProtocol<List<UserInfo>> {

	@Override
	public String getKey() {
		return "user";
	}

	@Override
	public List<UserInfo> parseFromJson(String jsonStr) {
		try {
			Gson gson = new Gson();
			UserInfo info = gson.fromJson(jsonStr, new TypeToken<UserInfo>() {
			}.getType());
			List<UserInfo> list = new ArrayList<UserInfo>();
			list.add(info);
			// List<UserInfo> list = new ArrayList<UserInfo>();
			// JSONObject jsonObject = new JSONObject(jsonStr);
			// UserInfo info = new UserInfo();
			// info.setEmail(jsonObject.optString("email"));
			// info.setName(jsonObject.optString("name"));
			// info.setUrl(jsonObject.optString("url"));
			// list.add(info);
			return list;
		} catch (JsonSyntaxException e) {
			L.e(e);
			return null;
		}
	}
}
