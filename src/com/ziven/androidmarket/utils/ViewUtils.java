package com.ziven.androidmarket.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ViewUtils {
	/* 把自身从父View中移除 */
	public static void removeSelfFromParent(View view) {
		if (view != null) {
			ViewParent parent = view.getParent();
			if (parent != null && parent instanceof ViewGroup) {
				ViewGroup group = (ViewGroup) parent;
				group.removeView(view);
			}
		}
	}

	public static void requestLayoutParent(View view, boolean isAll) {
		ViewParent parent = view.getParent();
		while (parent != null && parent instanceof View) {
			if (!parent.isLayoutRequested()) {
				parent.requestLayout();
				if (!isAll) {
					break;
				}
			}
			parent = parent.getParent();
		}
	}

	public static boolean isTouchView(MotionEvent ev, View view) {
		int[] vLoc = new int[2];
		view.getLocationOnScreen(vLoc);
		float motionX = ev.getRawX();
		float motionY = ev.getRawY();
		return motionX >= vLoc[0] && motionX <= (vLoc[0] + view.getWidth()) && motionY >= vLoc[1]
				&& motionY <= (vLoc[1] + view.getHeight());
	}

	public static <T extends View> T findViewByID(View layout, int id) {
		return (T) layout.findViewById(id);
	}
}
