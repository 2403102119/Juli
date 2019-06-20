package com.tcckj.juli.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

public class DrawableUtils {

	public static GradientDrawable getShape(int shape, int radius, int strokeWeight, int stokeColor, int selectorColor)
	{
		GradientDrawable gd = new GradientDrawable();
		gd.setShape(shape);// 设置形状
		gd.setCornerRadius(radius);// 设置圆角
		gd.setColor(selectorColor);
		gd.setStroke(strokeWeight,stokeColor);  //设置边框
		return gd;
	}

	public static StateListDrawable getSelector(Drawable normalBg, Drawable pressedBg)
	{
		StateListDrawable selector = new StateListDrawable();
		selector.addState(new int[]{android.R.attr.state_pressed}, pressedBg);
		selector.addState(new int[]{}, normalBg);
		return selector;
	}

	public static ColorStateList getColorSelector(int colorNormalBg, int colorPressedBg)
	{
		int statePressed = android.R.attr.state_pressed;
		int stateFocesed = android.R.attr.state_focused;
		int[][] state = {{statePressed},{-statePressed},{stateFocesed},{-stateFocesed}};
		int[] color = {colorPressedBg, colorNormalBg, colorPressedBg, colorNormalBg};
		ColorStateList selector = new ColorStateList(state,color);
		return selector;
	}

	public static Drawable getCompatDrawable(Context c, int drawableRes) {
		Drawable d = null;
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
				d = c.getResources().getDrawable(drawableRes);
			} else {
				d = c.getResources().getDrawable(drawableRes, c.getTheme());
			}
		} catch (Exception ex) {
		}
		return d;
	}
}
