package com.ntredize.redstop.main.view.container;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class NoSwipeDrawerLayout extends DrawerLayout {
	
	/* Init */
	public NoSwipeDrawerLayout(@NonNull Context context) {
		super(context);
	}
	
	public NoSwipeDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}
	
	public NoSwipeDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	/* Override Touch */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isDrawerOpen(GravityCompat.START) || ev.getAction() == MotionEvent.ACTION_MOVE) return false;
		return super.onInterceptTouchEvent(ev);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!isDrawerOpen(GravityCompat.START) || ev.getAction() == MotionEvent.ACTION_MOVE) return false;
		return super.onTouchEvent(ev);
	}
	
}
