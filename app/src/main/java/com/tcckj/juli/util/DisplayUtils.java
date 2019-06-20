package com.tcckj.juli.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class DisplayUtils {
	
	public static int getScreenHeightDip(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		int screenHeight = metric.densityDpi;
		return screenHeight;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		int screenHeight = metric.heightPixels;
		return screenHeight;
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		int screenWidth = metric.widthPixels;
		return screenWidth;
	}

	public static int getScreenWidthInDip(Context context)
	{
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);

		int SCREEN_WIDTH_PIXELS;
		int SCREEN_WIDTH_DP;
		//这里我们只关心竖屏状态下的值
		SCREEN_WIDTH_PIXELS = dm.widthPixels;
		SCREEN_WIDTH_DP = (int) (SCREEN_WIDTH_PIXELS / dm.density);

		return SCREEN_WIDTH_DP;
	}

	public static int getScreenHeightInDip(Context context)
	{
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);

		int SCREEN_HEIGHT_PIXELS;
		int SCREEN_HEIGHT_DP;
		//这里我们只关心竖屏状态下的值
		SCREEN_HEIGHT_PIXELS = dm.heightPixels;
		SCREEN_HEIGHT_DP = (int) (SCREEN_HEIGHT_PIXELS / dm.density);

		return SCREEN_HEIGHT_DP;
	}

	public static int getStatusBarHeight(Context context) {
		int height = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			height = context.getResources().getDimensionPixelSize(resourceId);
		}
		return height;
	}

	public static int getActionBarHeight(Context context) {
		// Calculate ActionBar height
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources()
					.getDisplayMetrics());
		}
		if (actionBarHeight == 0) {
			actionBarHeight = 45;
		}
		return actionBarHeight;
	}

	public static int dp2Px(Context context, int dp){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
		return px;
	}

	public static int sp2Px(Context context, int sp){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, dm);
		return px;
	}
}
