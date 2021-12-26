package com.ntredize.redstop.main.drawer;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.service.DownloadImageService;
import com.ntredize.redstop.support.utils.AttrUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	// view type
	private final static int VIEW_TYPE_HEADER = 0;
	private final static int VIEW_TYPE_ITEM = 1;
	
	// activity
	private final ActivityBase activity;

	// service
	private final AttrUtils attrUtils;
	private final DownloadImageService downloadImageService;
	
	// data
	private final boolean isDarkMode;
	private final List<DrawerItem> list;
	
	
	/* Init */
	DrawerAdapter(ActivityBase activity, boolean isDarkMode, List<DrawerItem> list) {
		// activity
		this.activity = activity;

		// service
		this.attrUtils = new AttrUtils(activity);
		this.downloadImageService = new DownloadImageService(activity);
		
		// data
		this.isDarkMode = isDarkMode;
		this.list = list;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (DrawerItem.TYPE_HEADER.equals(list.get(position).getType())) return VIEW_TYPE_HEADER;
		else return VIEW_TYPE_ITEM;
	}
	
	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (viewType == VIEW_TYPE_HEADER) {
			View contactView = LayoutInflater.from(activity).inflate(R.layout.row_drawer_header, parent, false);
			return new HeaderViewHolder(contactView);
		}
		else {
			View contactView = LayoutInflater.from(activity).inflate(R.layout.row_drawer_item, parent, false);
			return new ItemViewHolder(contactView, new MyClickListener());
		}
	}
	
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (DrawerItem.TYPE_HEADER.equals(list.get(position).getType())) onBindHeaderViewHolder((HeaderViewHolder) holder, position);
		else onBindItemViewHolder((ItemViewHolder) holder, position);
	}
	
	private void onBindHeaderViewHolder(@NonNull HeaderViewHolder holder, int position) {
		// get data from list
		DrawerItem drawerItem = list.get(position);
		String headerTitle = drawerItem.getHeaderTitle();
		
		// title
		TextView headerText = holder.headerText;
		headerText.setText(headerTitle);
	}
	
	private void onBindItemViewHolder(@NonNull ItemViewHolder holder, int position) {
		// get data from list
		DrawerItem drawerItem = list.get(position);
		Drawable imageDrawable = drawerItem.getImageDrawable();
		Bitmap imageBitmap = drawerItem.getImageBitmap();
		boolean tintImage = drawerItem.isTintImage();
		String name = drawerItem.getName();
		String desc = drawerItem.getDesc();
		Runnable action = drawerItem.getAction();
		boolean enabled = drawerItem.isEnabled();
		
		// update listener position
		holder.myClickListener.updatePositionAndAction(action);
		
		// click cover
		RelativeLayout clickCover = holder.clickCover;
		clickCover.setClickable(enabled);
		
		// image
		ImageView drawerImage = holder.drawerImage;
		drawerImage.setImageDrawable(null);
		
		// image (tint color)
		if (tintImage) {
			int color = attrUtils.getAttrColorInt(R.attr.textNormalColor);
			drawerImage.setColorFilter(color, PorterDuff.Mode.SRC_IN);
		}
		else drawerImage.clearColorFilter();
		
		// image (set or download)
		if (imageDrawable != null) {
			drawerImage.setImageDrawable(imageDrawable);
		}
		else if (imageBitmap != null) {
			drawerImage.setImageBitmap(imageBitmap);
		}
		else if (drawerItem.getImageUrl() != null) {
			new Thread(() -> {
				Bitmap downloadImageBitmap = downloadImageService.getImageByUrl(drawerItem.getImageUrl());
				activity.runOnUiThread(() -> drawerImage.setImageBitmap(downloadImageBitmap));

				// save cache image
				drawerItem.setImageBitmap(downloadImageBitmap);
				downloadImageService.saveFriendImageAsCache(drawerItem.getImageCacheKey(), isDarkMode, downloadImageBitmap);
			}).start();
		}
		
		// name
		TextView nameText = holder.nameText;
		nameText.setText(name);
		
		// desc
		TextView descText = holder.descText;
		if (desc != null && !desc.isEmpty()) {
			descText.setVisibility(View.VISIBLE);
			descText.setText(desc);
		}
		else {
			descText.setVisibility(View.GONE);
			descText.setText("");
		}
	}
	
	
	/* Item Count */
	@Override
	public int getItemCount() {
		return list.size();
	}
	
	
	
	/* View Holder */
	static class HeaderViewHolder extends RecyclerView.ViewHolder {
		
		// view
		TextView headerText;
		
		HeaderViewHolder(View itemView) {
			super(itemView);
			
			// view
			headerText = itemView.findViewById(R.id.drawer_header_text);
		}
	}
	
	static class ItemViewHolder extends RecyclerView.ViewHolder {
		
		// listener
		MyClickListener myClickListener;
		
		// view
		RelativeLayout clickCover;
		ImageView drawerImage;
		TextView nameText;
		TextView descText;
		
		ItemViewHolder(View itemView, MyClickListener myClickListener) {
			super(itemView);
			this.myClickListener = myClickListener;
			
			// view
			clickCover = itemView.findViewById(R.id.drawer_item_row_click_cover);
			drawerImage = itemView.findViewById(R.id.drawer_item_row_image);
			nameText = itemView.findViewById(R.id.drawer_item_row_name);
			descText = itemView.findViewById(R.id.drawer_item_row_desc);
			
			// listener
			clickCover.setOnClickListener(myClickListener);
		}
	}
	
	
	/* Click Listener */
	private static class MyClickListener implements View.OnClickListener {

		private Runnable action;
		
		void updatePositionAndAction(Runnable action) {
			this.action = action;
		}
		
		@Override
		public void onClick(View v) {
			new Handler().post(action);
		}
		
	}
	
}