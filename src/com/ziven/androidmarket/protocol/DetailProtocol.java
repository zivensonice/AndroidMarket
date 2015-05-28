package com.ziven.androidmarket.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.StringUtils;
import com.ziven.bean.AppInfo;

public class DetailProtocol extends BaseProtocol<AppInfo> {

  private String mPackageName = "";

  public void setPackageName(String packageName) {
    this.mPackageName = packageName;
  }


  public String getPackageName() {
    return mPackageName;
  }


  @Override
  public String getKey() {
    return "detail";
  }

  @Override
  protected String getParams() {
    if (StringUtils.isEmpty(getPackageName())) {
      return super.getParams();
    } else {
      return "&packageName=" + getPackageName();
    }
  }

  @Override
  public AppInfo parseFromJson(String jsonStr) {
    AppInfo appInfo = null;
    try {
      Gson gson = new Gson();
      appInfo = gson.fromJson(jsonStr, new TypeToken<AppInfo>() {}.getType());
      JSONObject json = new JSONObject(jsonStr);
      JSONArray array = json.getJSONArray("safe");
      List<String> safeUrlList = new ArrayList<String>();
      List<String> safeDesList = new ArrayList<String>();
      List<String> safeDesUrlList = new ArrayList<String>();
      List<Integer> safeDesColorList = new ArrayList<Integer>();

      for (int i = 0; i < array.length(); i++) {
        JSONObject obj = array.getJSONObject(i);
        safeUrlList.add(obj.getString("safeUrl"));
        safeDesList.add(obj.getString("safeDesUrl"));
        safeDesUrlList.add(obj.getString("safeDesUrl"));
        safeDesColorList.add(obj.getInt("safeDesColor"));
      }
      appInfo.setSafeUrl(safeUrlList);
      appInfo.setSafeDes(safeDesList);
      appInfo.setSafeDesUrl(safeDesUrlList);
      appInfo.setSafeDesColor(safeDesColorList);
      L.d(appInfo.toString());
      return appInfo;
    } catch (Exception e) {
      e.printStackTrace();
      return appInfo;
    }

  }
}
