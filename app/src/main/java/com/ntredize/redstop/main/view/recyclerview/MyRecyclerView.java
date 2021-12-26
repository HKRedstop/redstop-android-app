package com.ntredize.redstop.main.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView {
	
	/* Init */
	public MyRecyclerView(Context context) {
		super(context);
		initMyRecyclerView(context);
	}
	
	public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		initMyRecyclerView(context);
	}
	
	public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initMyRecyclerView(context);
	}
	
	private void initMyRecyclerView(Context context) {
		this.setLayoutManager(new LinearLayoutManager(context));
		this.setHasFixedSize(true);
	}
	
}
