package com.ntredize.redstop.main.drawer;

import android.graphics.Bitmap;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	// view type
	private final static int VIEW_TYPE_HEADER = 0;
	private final static int VIEW_TYPE_SPACE = 1;
	private final static int VIEW_TYPE_ITEM = 2;
	
	// activity
	private final ActivityBase activity;
	
	// service
	private final DownloadImageService downloadImageService;
	
	// data
	private final List<DrawerItem> list;
	
	
	/* Init */
	DrawerAdapter(ActivityBase activity, List<DrawerItem> list) {
		// activity
		this.activity = activity;

		// service
		this.downloadImageService = new DownloadImageService(activity);
		
		// data
		this.list = list;
	}
	
	@Override
	public int getItemViewType(int position) {
		String type = list.get(position).getType();
		if (DrawerItem.TYPE_HEADER.equals(type)) return VIEW_TYPE_HEADER;
		else if (DrawerItem.TYPE_SPACE.equals(type)) return VIEW_TYPE_SPACE;
		else return VIEW_TYPE_ITEM;
	}
	
	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (viewType == VIEW_TYPE_HEADER) {
			View contactView = LayoutInflater.from(activity).inflate(R.layout.row_drawer_header, parent, false);
			return new HeaderViewHolder(contactView);
		}
		else if (viewType == VIEW_TYPE_SPACE) {
			View contactView = LayoutInflater.from(activity).inflate(R.layout.row_drawer_space, parent, false);
			return new SpaceViewHolder(contactView);
		}
		else {
			View contactView = LayoutInflater.from(activity).inflate(R.layout.row_drawer_item, parent, false);
			return new ItemViewHolder(contactView, new MyClickListener());
		}
	}
	
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		String type = list.get(position).getType();
		if (DrawerItem.TYPE_HEADER.equals(type)) onBindHeaderViewHolder((HeaderViewHolder) holder, position);
		else if (DrawerItem.TYPE_SPACE.equals(type)) onBindSpaceViewHolder();
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
	
	private void onBindSpaceViewHolder() {
		// nothing to do
	}
	
	private void onBindItemViewHolder(@NonNull ItemViewHolder holder, int position) {
		// get data from list
		DrawerItem drawerItem = list.get(position);
		String type = drawerItem.getType();
		Drawable imageDrawable = drawerItem.getImageDrawable();
		Bitmap imageBitmap = drawerItem.getImageBitmap();
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
		ImageView functionImage = holder.functionImage;
		RelativeLayout friendImageContainer = holder.friendImageContainer;
		ImageView friendImage = holder.friendImage;
		
		functionImage.setImageDrawable(null);
		functionImage.setVisibility(View.GONE);
		friendImage.setImageDrawable(null);
		friendImageContainer.setVisibility(View.GONE);
		
		if (DrawerItem.TYPE_FUNCTION.equals(type)) functionImage.setVisibility(View.VISIBLE);
		else if (DrawerItem.TYPE_FRIEND.equals(type)) friendImageContainer.setVisibility(View.VISIBLE);
		
		// image (set or download)
		final ImageView targetImageView = DrawerItem.TYPE_FUNCTION.equals(type) ? functionImage : DrawerItem.TYPE_FRIEND.equals(type) ? friendImage : null;
		if (targetImageView != null) {
			if (imageDrawable != null) targetImageView.setImageDrawable(imageDrawable);
			else if (imageBitmap != null) targetImageView.setImageBitmap(imageBitmap);
			else if (drawerItem.getImageUrl() != null) {
				new Thread(() -> {
					Bitmap downloadImageBitmap = downloadImageService.getImageByUrl(drawerItem.getImageUrl());
					activity.runOnUiThread(() -> targetImageView.setImageBitmap(downloadImageBitmap));
					
					// save cache image
					drawerItem.setImageBitmap(downloadImageBitmap);
					downloadImageService.saveFriendImageAsCache(drawerItem.getImageCacheKey(), downloadImageBitmap);
				}).start();
			}
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
	
	static class SpaceViewHolder extends RecyclerView.ViewHolder {
		SpaceViewHolder(View itemView) {
			super(itemView);
		}
	}
	
	static class ItemViewHolder extends RecyclerView.ViewHolder {
		
		// listener
		MyClickListener myClickListener;
		
		// view
		RelativeLayout clickCover;
		ImageView functionImage;
		RelativeLayout friendImageContainer;
		ImageView friendImage;
		TextView nameText;
		TextView descText;
		
		ItemViewHolder(View itemView, MyClickListener myClickListener) {
			super(itemView);
			this.myClickListener = myClickListener;
			
			// view
			clickCover = itemView.findViewById(R.id.drawer_item_row_click_cover);
			functionImage = itemView.findViewById(R.id.drawer_item_row_function_image);
			friendImageContainer = itemView.findViewById(R.id.drawer_item_row_friend_image_container);
			friendImage = itemView.findViewById(R.id.drawer_item_row_friend_image);
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