package com.ntredize.redstop.main.recycler;

import android.content.Context;

import com.ntredize.redstop.R;

import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

public class MyFormListDivider extends DividerItemDecoration {
	
	/* Init */
	public MyFormListDivider(Context context, int orientation) {
		super(context, orientation);
		this.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context, R.drawable.form_list_divider)));
	}
	
}
