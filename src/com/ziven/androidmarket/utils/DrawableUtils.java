package com.ziven.androidmarket.utils;

import android.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

/**
 * @author Ziven
 * 
 */
public class DrawableUtils {

	/**
	 * 创建一个圆角图片
	 * 
	 * @param contentColor
	 *            内部填充颜色
	 * @param strokeColor
	 *            描边颜色
	 * @param radius
	 *            圆角
	 * @return
	 */
	public static GradientDrawable createDrawable(int contentColor,
			int strokeColor, int radius) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setGradientType(GradientDrawable.RECTANGLE);
		drawable.setColor(contentColor);
		drawable.setStroke(1, strokeColor);
		drawable.setCornerRadius(radius);
		return drawable;
	}

	/**
	 * 创建一个图片背景选择器
	 * 
	 * @param normalState
	 * @param pressedState
	 * @return
	 */
	public static StateListDrawable createSelector(Drawable normalState,
			Drawable pressedState) {
		StateListDrawable bg = new StateListDrawable();
		bg.addState(new int[] { R.attr.state_pressed, R.attr.state_enabled },
				pressedState);
		bg.addState(new int[] { R.attr.state_enabled }, normalState);
		bg.addState(new int[] {}, normalState);
		return bg;
	}

	/* 获取图片的大小 */
	@SuppressLint("NewApi")
	public static int getDrawableSize(Drawable drawable) {
		if (null == drawable) {
			return 0;
		}
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		} else {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}
}
