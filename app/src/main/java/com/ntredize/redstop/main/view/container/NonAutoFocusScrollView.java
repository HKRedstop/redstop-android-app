package com.ntredize.redstop.main.view.container;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class NonAutoFocusScrollView extends ScrollView {
	
	/* Init */
	public NonAutoFocusScrollView(Context context) {
		super(context);
		initNonAutoFocusScrollView();
	}
	
	public NonAutoFocusScrollView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		initNonAutoFocusScrollView();
	}
	
	public NonAutoFocusScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initNonAutoFocusScrollView();
	}
	
	public NonAutoFocusScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initNonAutoFocusScrollView();
	}
	
	private void initNonAutoFocusScrollView() {
		this.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}
	
}
