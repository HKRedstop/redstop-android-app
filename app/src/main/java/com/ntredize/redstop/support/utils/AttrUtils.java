package com.ntredize.redstop.support.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

public class AttrUtils {
	
	private final Context context;
	
	
	/* Init */
	public AttrUtils(Context context) {
		this.context = context;
	}
	
	
	/* Get Value */
	public int getAttrColorInt(int attrId) {
		TypedValue typedValue = new TypedValue();
		Resources.Theme theme = context.getTheme();
		theme.resolveAttribute(attrId, typedValue, true);
		@ColorInt int color = typedValue.data;
		return color;
	}
	
	public float getAttrFloat(int attrId) {
		TypedValue typedValue = new TypedValue();
		Resources.Theme theme = context.getTheme();
		theme.resolveAttribute(attrId, typedValue, true);
		return typedValue.getFloat();
	}
	
}
