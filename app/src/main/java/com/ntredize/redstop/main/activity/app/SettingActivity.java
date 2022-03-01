package com.ntredize.redstop.main.activity.app;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.dialog.SettingAppearanceDialog;
import com.ntredize.redstop.main.recycler.MyFormListDivider;
import com.ntredize.redstop.main.recycler.StickyHeaderItemDecoration;
import com.ntredize.redstop.main.recycler.adapter.StickySettingAdapter;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;
import com.ntredize.redstop.support.service.SharedPreferenceService;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.appcompat.app.AlertDialog;

public class SettingActivity extends ActivityBase {
	
	// service
	private SharedPreferenceService sharedPreferenceService;
	private ThemeService themeService;
	
	// view
	private RelativeLayout dummyHeaderContainer;
	private MyRecyclerView recyclerView;
	private StickySettingAdapter settingAdapter;
	
	// dialog
	private AlertDialog appearanceDialog;
	
	
	/* Init */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Start Activity");
		
		initService();
		initTheme();
		initData();
		initView();
	}
	
	@Override
	protected void initService() {
		sharedPreferenceService = new SharedPreferenceService(this);
		themeService = new ThemeService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(true);
	}
	
	@Override
	protected void initData() {
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_setting);
		
		// title
		setTitle(R.string.setting_title);
		
		// dummy view
		dummyHeaderContainer = findViewById(R.id.dummy_sticky_setting_list_header_container);
		
		// recycler view
		recyclerView = findViewById(R.id.setting_recycler_view);
		settingAdapter = new StickySettingAdapter(this);
		recyclerView.setAdapter(settingAdapter);
		initLayoutForSpecifiedView();
	}
	
	private void initLayoutForSpecifiedView() {
		dummyHeaderContainer.post(() -> {
			// measure dummy header height
			int headerHeight = dummyHeaderContainer.getMeasuredHeight();
			dummyHeaderContainer.setVisibility(View.GONE);
			
			// header and divider for recycler view
			StickyHeaderItemDecoration headerDecoration = new StickyHeaderItemDecoration(headerHeight, settingAdapter.getSectionCallback());
			recyclerView.addItemDecoration(headerDecoration);
			
			MyFormListDivider myFormListDivider = new MyFormListDivider(this, MyFormListDivider.VERTICAL);
			recyclerView.addItemDecoration(myFormListDivider);
			recyclerView.scrollToPosition(0);
			recyclerView.setVisibility(View.VISIBLE);
		});
	}
	
	
	/* Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		showBackMenuItem();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	
	/* Appearance */
	public void openSettingAppearanceDialog() {
		appearanceDialog = new SettingAppearanceDialog(this).buildDialog();
		appearanceDialog.show();
	}
	
	public void setAppearance(String appearance) {
		// update dark mode
		sharedPreferenceService.setAppearance(appearance);
		
		// recreate activity to activate new theme
		recreate();
		
		// close dialog
		if (appearanceDialog != null) appearanceDialog.dismiss();
	}
	
	
	/* Contact Us */
	public void contactUs() {
		sendEmail(getString(R.string.app_email));
	}
	
	
	/* Open Privacy */
	public void openPrivacy() {
		openUrl(getString(R.string.app_privacy_url));
	}
	
}
