package com.ntredize.redstop.main.activity.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.db.model.WebsiteSearchCriteria;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.redcompany.RedCompanyDetailActivity;
import com.ntredize.redstop.main.dialog.HelpDialog;
import com.ntredize.redstop.main.fragment.checkwebsite.CheckWebsiteFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyListFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyNoSearchResultFragment;
import com.ntredize.redstop.support.service.CheckWebsiteService;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class CheckWebsiteActivity extends ActivityBase {
	
	// service
	private ThemeService themeService;
	private CheckWebsiteService checkWebsiteService;
	
	// view
	private RelativeLayout loadingContainer;
	private int fragmentContainerId;
	private RelativeLayout fragmentContainer;
	private CheckWebsiteFragment checkWebsiteFragment;
	private Fragment currentFragment;
	
	// menu
	private MenuItem helpMenuItem;
	
	// data
	private boolean isStartChecking;
	
	
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
		themeService = new ThemeService(this);
		checkWebsiteService = new CheckWebsiteService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(true);
	}
	
	@Override
	protected void initData() {
		// is start checking
		isStartChecking = false;
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_check_website);
		
		// title
		setTitle(R.string.label_check_website);
		
		// loading
		loadingContainer = findViewById(R.id.check_website_loading_container);
		
		// fragment container
		fragmentContainerId = R.id.check_website_fragment_container;
		fragmentContainer = findViewById(fragmentContainerId);
		
		// start check fragment
		checkWebsiteFragment = initCheckWebsiteFragment(true);
		this.showFragment(fragmentContainerId, checkWebsiteFragment);
		currentFragment = checkWebsiteFragment;
	}
	
	private CheckWebsiteFragment initCheckWebsiteFragment(boolean forceClearValue) {
		String currentValue = (checkWebsiteFragment != null && !forceClearValue) ? checkWebsiteFragment.getCurrentValue() : "";
		
		CheckWebsiteFragment fragment = new CheckWebsiteFragment();
		
		Bundle args = new Bundle();
		args.putString(CheckWebsiteFragment.KEY_SEARCH_VALUE, currentValue);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	/* Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_check_website, menu);
		
		// show help after starting checking
		helpMenuItem = menu.findItem(R.id.menu_help_button);
		helpMenuItem.setVisible(false);
		
		showBackMenuItem();
		return true;
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				checkWebsiteBack();
				return true;
			case R.id.menu_help_button:
				showHelpDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	/* Menu - Back */
	@Override
	public void onBackPressed() {
		checkWebsiteBack();
	}
	
	private void checkWebsiteBack() {
		if (isStartChecking) {
			// title
			setTitle(R.string.label_check_website);
			
			// change fragment back (and clear value)
			checkWebsiteFragment = initCheckWebsiteFragment(true);
			this.showFragment(fragmentContainerId, checkWebsiteFragment);
			currentFragment = checkWebsiteFragment;
			
			// update data
			isStartChecking = false;
			
			// hide help menu item
			helpMenuItem.setVisible(false);
		}
		else finish();
	}
	
	
	/* Menu - Help */
	private void showHelpDialog() {
		AlertDialog helpDialog = new HelpDialog(this).buildDialog();
		if (!isFinishing()) helpDialog.show();
	}
	
	
	/* When Rotate, Re-Init View */
	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		// when rotate
		// 1. before clicking start: re-init fragment for rotation
		// 2. after clicking start:  refresh fragment
		if (!isStartChecking) {
			checkWebsiteFragment = initCheckWebsiteFragment(false);
			this.showFragment(fragmentContainerId, checkWebsiteFragment);
			currentFragment = checkWebsiteFragment;
		}
		else refreshFragment(currentFragment);
	}
	
	
	/* Check Website */
	public void startCheckWebsite(WebsiteSearchCriteria criteria) {
		// update data
		isStartChecking = true;
		
		// show loading
		loadingContainer.setVisibility(View.VISIBLE);
		
		// hide fragment
		fragmentContainer.setVisibility(View.GONE);
		
		// load data from server
		new Thread(() -> {
			// get red company by website
			SearchResult<RedCompanySimple> searchResult = checkWebsiteService.checkWebsite(criteria);
			
			// update view
			if (searchResult != null) updateViewAfterLoading(searchResult);
			else updateViewForException(new ApplicationException(this, ApplicationException.HTTP_CLIENT_ERROR));
		}).start();
	}
	
	private void updateViewForException(ApplicationException e) {
		this.runOnUiThread(() -> {
			// show fragment
			fragmentContainer.setVisibility(View.VISIBLE);
			
			// hide loading
			loadingContainer.setVisibility(View.GONE);
			
			// show error dialog
			errorHandling(e);
		});
	}
	
	private void updateViewAfterLoading(SearchResult<RedCompanySimple> searchResult) {
		this.runOnUiThread(() -> {
			// title
			setTitle(getString(R.string.label_check_website) + " (" + searchResult.getTotalNum() + ")");
			
			// menu
			helpMenuItem.setVisible(true);
			
			// init fragment
			if (searchResult.getTotalNum() <= 0) {
				Fragment fragment = new RedCompanyNoSearchResultFragment();
				showFragment(fragmentContainerId, fragment);
				currentFragment = fragment;
			}
			else {
				RedCompanyListFragment fragment = new RedCompanyListFragment();
				fragment.setArguments(buildRedCompanyListFragmentArgs(searchResult));
				showFragment(fragmentContainerId, fragment);
				currentFragment = fragment;
			}
			
			// wait 200ms for fragment to be ready
			new Handler().postDelayed(() -> {
				// show fragment
				fragmentContainer.setVisibility(View.VISIBLE);
				
				// hide loading
				loadingContainer.setVisibility(View.GONE);
			}, 200);
		});
	}
	
	private Bundle buildRedCompanyListFragmentArgs(SearchResult<RedCompanySimple> searchResult) {
		Bundle args = new Bundle();
		args.putSerializable(RedCompanyListFragment.KEY_RED_COMPANY_SEARCH_RESULT, searchResult);
		return args;
	}
	
	
	/* Open Company Detail */
	public void openRedCompanyDetail(RedCompanySimple redCompanySimple) {
		Intent i = new Intent(this, RedCompanyDetailActivity.class);
		i.putExtra(RedCompanyDetailActivity.KEY_RED_COMPANY_CODE, redCompanySimple.getCompanyCode());
		i.putExtra(RedCompanyDetailActivity.KEY_RED_COMPANY_DISPLAY_NAME, redCompanySimple.getDisplayName());
		startActivity(i);
	}
	
}
