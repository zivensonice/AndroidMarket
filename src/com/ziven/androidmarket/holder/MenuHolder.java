package com.ziven.androidmarket.holder;

import com.ziven.bean.UserInfo;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuHolder extends BaseHolder<UserInfo> implements OnClickListener {
	private RelativeLayout mHomeLayout, mSettingLayout, mThemeLayout, mScansLayout, mFeedbackLayout, mUpdatesLayout,
			mAboutLayout, mExitLayout, mPhotoLayout;
	private ImageView mPhoto;
	private TextView mTvUserName, mTvUserEmail;
	private UserInfo mInfo;

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}

}
