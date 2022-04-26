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
	
	public void init(Context context, DrawerLayout drawerLayout) {
		// activity
		this.activity = (HomeActivity) context;

		// service
		this.friendSuggestionService = new FriendSuggestionService(context);
		this.downloadImageService = new DownloadImageService(context);
		
		// item
		List<DrawerItem> drawerItemList = new ArrayList<>();
		drawerItemList.addAll(buildMainDrawerItemList());
		drawerItemList.addAll(buildFriendDrawerItemList());
		drawerItemList.add(new DrawerItem(DrawerItem.TYPE_SPACE));
		
		// view
		DrawerAdapter drawerAdapter = new DrawerAdapter(activity, drawerItemList);
		MyRecyclerView recyclerView = findViewById(R.id.drawer_recycler_view);
		recyclerView.setVisibility(View.VISIBLE);
		recyclerView.setAdapter(drawerAdapter);
		
		// listener
		drawerLayout.addDrawerListener(new MyDrawerListener(recyclerView));
	}
	
	private List<DrawerItem> buildMainDrawerItemList() {
		List<DrawerItem> mainDrawerItemList = new ArrayList<>();

		// help
		DrawerItem helpItem = new DrawerItem(DrawerItem.TYPE_FUNCTION);
		helpItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_help));
		helpItem.setName(activity.getString(R.string.drawer_help));
		helpItem.setAction(this::showHelpDialog);
		mainDrawerItemList.add(helpItem);
		
		// suggestion
		DrawerItem suggestionItem = new DrawerItem(DrawerItem.TYPE_FUNCTION);
		suggestionItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_suggestion));
		suggestionItem.setName(activity.getString(R.string.drawer_suggestion));
		suggestionItem.setAction(() -> activity.startSuggestion());
		mainDrawerItemList.add(suggestionItem);
		
		// scan barcode
		DrawerItem scanBarcodeItem = new DrawerItem(DrawerItem.TYPE_FUNCTION);
		scanBarcodeItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_barcode));
		scanBarcodeItem.setName(activity.getString(R.string.drawer_scan_barcode));
		scanBarcodeItem.setDesc(activity.getString(R.string.drawer_scan_barcode_desc));
		scanBarcodeItem.setAction(() -> activity.startScanBarcode());
		mainDrawerItemList.add(scanBarcodeItem);
		
		// check device
		DrawerItem checkDeviceItem = new DrawerItem(DrawerItem.TYPE_FUNCTION);
		checkDeviceItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_android));
		checkDeviceItem.setName(activity.getString(R.string.drawer_check_device));
		checkDeviceItem.setAction(() -> activity.startCheckDevice());
		mainDrawerItemList.add(checkDeviceItem);
		
		// check website
		DrawerItem checkWebsiteItem = new DrawerItem(DrawerItem.TYPE_FUNCTION);
		checkWebsiteItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_browser));
		checkWebsiteItem.setName(activity.getString(R.string.drawer_check_website));
		checkWebsiteItem.setAction(() -> activity.startCheckWebsite());
		mainDrawerItemList.add(checkWebsiteItem);
		
		// setting
		DrawerItem settingItem = new DrawerItem(DrawerItem.TYPE_FUNCTION);
		settingItem.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drawer_image_setting));
		settingItem.setName(activity.getString(R.string.drawer_setting));
		settingItem.setAction(() -> activity.startSetting());
		mainDrawerItemList.add(settingItem);
		
		return mainDrawerItemList;
	}
	
	private List<DrawerItem> buildFriendDrawerItemList() {
		List<DrawerItem> friendDrawerItemList = new ArrayList<>();
		String lastCategoryName = null;
		
		for (FriendSuggestion friend : friendSuggestionService.getAllFriendSuggestion()) {
			// header
			if (lastCategoryName == null || !lastCategoryName.equals(friend.getCategoryName())) {
				DrawerItem drawerHeader = new DrawerItem(DrawerItem.TYPE_HEADER);
				drawerHeader.setHeaderTitle(friend.getCategoryName());
				drawerHeader.setEnabled(false);
				friendDrawerItemList.add(drawerHeader);
				
				lastCategoryName = friend.getCategoryName();
			}
			
			// item
			DrawerItem drawerItem = new DrawerItem(DrawerItem.TYPE_FRIEND);
			drawerItem.setName(friend.getName());
			
			// item: image
			drawerItem.setImageCacheKey(friend.getFriendCode());
			Bitmap cacheImage = downloadImageService.getCacheFriendImage(friend.getFriendCode());
			if (cacheImage != null) drawerItem.setImageBitmap(cacheImage);
			else drawerItem.setImageUrl(downloadImageService.getFriendThumbnailImageUrl(friend.getFriendCode()));
			
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
