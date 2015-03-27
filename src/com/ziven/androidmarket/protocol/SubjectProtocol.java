package com.ziven.androidmarket.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ziven.androidmarket.utils.L;
import com.ziven.bean.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<List<SubjectInfo>> {

	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public List<SubjectInfo> parseFromJson(String jsonStr) {
		L.d("jsonStr" + jsonStr);
		Gson gson = new Gson();
		try {
			List<SubjectInfo> list = gson.fromJson(jsonStr, new TypeToken<List<SubjectInfo>>() {
			}.getType());
			for (SubjectInfo info : list) {
				L.d(info.toString());
			}
			return list;
		} catch (JsonSyntaxException e) {
			L.e(e);
			return null;
		}
	}
}
