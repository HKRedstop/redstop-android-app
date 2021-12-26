package com.ntredize.redstop.main.view.viewpager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
	
	public MyViewPager(@NonNull Context context) {
		super(context);
		this.setSaveEnabled(false);
	}
	
	public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		this.setSaveEnabled(false);
	}
	
}
