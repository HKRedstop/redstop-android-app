package com.ntredize.redstop.main.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.FriendSuggestion;
import com.ntredize.redstop.main.activity.app.HomeActivity;
import com.ntredize.redstop.main.dialog.HelpDialog;
import com.ntredize.redstop.main.dialog.OpenStoreDialog;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;
import com.ntredize.redstop.support.service.DownloadImageService;
import com.ntredize.redstop.support.service.FriendSuggestionService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainDrawerContainer extends RelativeLayout {
	
	// activity
	private HomeActivity activity;

	// service
	private FriendSuggestionService friendSuggestionService;
	private DownloadImageService downloadImageService;
	
	
	/* Init */
	public MainDrawerContainer(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void init(Context context, DrawerLayout drawerLayout, boolean isDarkMode) {
		// activity
		this.activity = (HomeActivity) context;

		// service
		this.friendSuggestionService = new FriendSuggestionService(context);
		this.downloadImageService = new DownloadImageService(context);
		
		// item
		List<DrawerItem> drawerItemList = new ArrayList<>();
		drawerItemList.addAll(buildMainDrawerItemList(isDarkMode));
		drawerItemList.addAll(buildFriendDrawerItemList(isDarkMode));
		
		// view
		DrawerAdapter drawerAdapter = new DrawerAdapter(activity, isDarkMode, drawerItemList);
		MyRecyclerView recyclerView = findViewById(R.id.drawer_recycler_view);
		recyclerView.setVisibility(View.VISIBLE);
		recyclerView.setAdapter(drawerAdapter);
		
		// listener
		drawerLayout.addDrawerListener(new MyDrawerListener(recyclerView));
	}
	
	private List<DrawerItem> buildMainDrawerItemList(boolean isDarkMode) {
		List<DrawerItem> mainDrawerItemList = new ArrayList<>();

		// help
		DrawerItem helpItem = new DrawerItem();
		helpItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_help));
		helpItem.setTintImage(true);
		helpItem.setName(activity.getString(R.string.drawer_help));
		helpItem.setAction(this::showHelpDialog);
		mainDrawerItemList.add(helpItem);
		
		// suggestion
		DrawerItem suggestionItem = new DrawerItem();
		suggestionItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_suggestion));
		suggestionItem.setTintImage(true);
		suggestionItem.setName(activity.getString(R.string.drawer_suggestion));
		suggestionItem.setAction(() -> activity.startSuggestion());
		mainDrawerItemList.add(suggestionItem);
		
		// scan barcode
		DrawerItem scanBarcodeItem = new DrawerItem();
		scanBarcodeItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_barcode));
		scanBarcodeItem.setTintImage(true);
		scanBarcodeItem.setName(activity.getString(R.string.drawer_scan_barcode));
		scanBarcodeItem.setDesc(activity.getString(R.string.drawer_scan_barcode_desc));
		scanBarcodeItem.setAction(() -> activity.startScanBarcode());
		mainDrawerItemList.add(scanBarcodeItem);
		
		// check device
		DrawerItem checkDeviceItem = new DrawerItem();
		checkDeviceItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_android));
		checkDeviceItem.setTintImage(true);
		checkDeviceItem.setName(activity.getString(R.string.drawer_check_device));
		checkDeviceItem.setAction(() -> activity.startCheckDevice());
		mainDrawerItemList.add(checkDeviceItem);
		
		// check website
		DrawerItem checkWebsiteItem = new DrawerItem();
		checkWebsiteItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_browser));
		checkWebsiteItem.setTintImage(true);
		checkWebsiteItem.setName(activity.getString(R.string.drawer_check_website));
		checkWebsiteItem.setAction(() -> activity.startCheckWebsite());
		mainDrawerItemList.add(checkWebsiteItem);
		
		// dark mode
		DrawerItem darkModeItem = new DrawerItem();
		darkModeItem.setImageDrawable(ContextCompat.getDrawable(activity, isDarkMode ? R.drawable.drawer_image_light_mode : R.drawable.drawer_image_dark_mode));
		darkModeItem.setTintImage(true);
		darkModeItem.setName(activity.getString(isDarkMode ? R.string.drawer_light_mode : R.string.drawer_dark_mode));
		darkModeItem.setAction(() -> activity.updateDarkMode());
		mainDrawerItemList.add(darkModeItem);
		
		return mainDrawerItemList;
	}
	
	private List<DrawerItem> buildFriendDrawerItemList(boolean isDarkMode) {
		List<DrawerItem> friendDrawerItemList = new ArrayList<>();
		String lastCategoryName = null;

		for (FriendSuggestion friend : friendSuggestionService.getAllFriendSuggestion()) {
			// header
			if (lastCategoryName == null || !lastCategoryName.equals(friend.getCategoryName())) {
				DrawerItem drawerHeader = new DrawerItem();
				drawerHeader.setType(DrawerItem.TYPE_HEADER);
				drawerHeader.setHeaderTitle(friend.getCategoryName());
				drawerHeader.setEnabled(false);
				friendDrawerItemList.add(drawerHeader);

				lastCategoryName = friend.getCategoryName();
			}

			// item
			DrawerItem drawerItem = new DrawerItem();
			drawerItem.setName(friend.getName());

			// item: image
			drawerItem.setImageCacheKey(friend.getFriendCode());
			Bitmap cacheImage = downloadImageService.getCacheFriendImage(friend.getFriendCode(), isDarkMode);
			if (cacheImage != null) {
				drawerItem.setImageBitmap(cacheImage);
			}
			else {
				drawerItem.setImageUrl(downloadImageService.getFriendThumbnailImageUrl(friend.getFriendCode(), isDarkMode));
			}

			// item: remark
			if (friend.getRemark() != null && !friend.getRemark().isEmpty()) {
				drawerItem.setDesc(friend.getRemark());
			}

			// item: url
			if (friend.getUrl() != null && !friend.getUrl().isEmpty()) {
				drawerItem.setAction(() -> {
					if (friend.getOpenStore()) showOpenStoreDialog(friend.getName(), friend.getUrl());
					else openLink(friend.getUrl());
				});
			}
			
			friendDrawerItemList.add(drawerItem);
		}

		return friendDrawerItemList;
	}
	
	
	/* Action */
	private void showHelpDialog() {
		AlertDialog helpDialog = new HelpDialog(activity).buildDialog();
		if (!activity.isFinishing()) helpDialog.show();
	}
	
	private void openLink(String url) {
		activity.openUrl(url);
	}
	
	private void showOpenStoreDialog(String name, String url) {
		AlertDialog dialog = new OpenStoreDialog(activity, name, url).buildDialog();
		if (!activity.isFinishing()) dialog.show();
	}
	
	
	/* Drawer Listener */
	static class MyDrawerListener implements DrawerLayout.DrawerListener {
		
		private final MyRecyclerView recyclerView;
		
		MyDrawerListener(MyRecyclerView recyclerView) {
			this.recyclerView = recyclerView;
		}
		
		@Override
		public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
		}
		
		@Override
		public void onDrawerOpened(@NonNull View drawerView) {
		}
		
		@Override
		public void onDrawerClosed(@NonNull View drawerView) {
			// drawer scroll to top
			recyclerView.scrollToPosition(0);
		}
		
		@Override
		public void onDrawerStateChanged(int newState) {
		}
		
	}
	
}