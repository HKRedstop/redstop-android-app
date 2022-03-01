package com.ntredize.redstop.main.recycler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntredize.redstop.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {

	// view
	private View headerView;
	private TextView headerText;

	// data
	private final int headerOffset;
	private final SectionCallback sectionCallback;


	/* Init */
	public StickyHeaderItemDecoration(int headerHeight, @NonNull SectionCallback sectionCallback) {
		this.headerOffset = headerHeight;
		this.sectionCallback = sectionCallback;
	}


	/* Item Offsets */
	@Override
	public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);

		int pos = parent.getChildAdapterPosition(view);
		if (sectionCallback.isSameSection(pos)) {
			outRect.top = headerOffset - 5; // -5 for cover the divider of last row
		}
	}


	/* Draw */
	@Override
	public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		super.onDrawOver(c, parent, state);

		// header
		if (headerView == null) {
			headerView = inflateHeaderView(parent);
			headerText = headerView.findViewById(R.id.row_header_text);
			fixLayoutSize(headerView, parent);
		}

		// draw
		String previousHeader = "";

		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			final int position = parent.getChildAdapterPosition(child);

			String title = sectionCallback.getSectionHeader(position);
			headerText.setText(title);

			if (!previousHeader.equals(title) || sectionCallback.isSameSection(position)) {
				if (sectionCallback.showSectionHeader(position)) {
					drawHeader(c, child, headerView);
					previousHeader = title;
				}
			}
		}
	}

	private void drawHeader(Canvas c, View child, View headerView) {
		c.save();
		c.translate(0, Math.max(0, child.getTop() - headerView.getHeight()));
		headerView.draw(c);
		c.restore();
	}

	private View inflateHeaderView(RecyclerView parent) {
		return LayoutInflater.from(parent.getContext()).inflate(R.layout.row_header, parent, false);
	}

	private void fixLayoutSize(View view, ViewGroup parent) {
		int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
		int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

		int childWidth = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingStart() + parent.getPaddingEnd(), view.getLayoutParams().width);
		int childHeight = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

		view.measure(childWidth, childHeight);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	}


	/* Section Callback */
	public interface SectionCallback {

		boolean showSectionHeader(int position);

		boolean isSameSection(int position);

		String getSectionHeader(int position);

	}

}