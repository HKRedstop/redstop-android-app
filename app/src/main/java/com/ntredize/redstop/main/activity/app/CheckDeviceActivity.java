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

import com.google.android.material.tabs.TabLayout;
import com.ntredize.redstop.R;
import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.db.model.AndroidDeviceSearchResult;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.redcompany.RedCompanyDetailActivity;
import com.ntredize.redstop.main.dialog.CaCertHelpDialog;
import com.ntredize.redstop.main.dialog.CheckDevicePrivacyDialog;
import com.ntredize.redstop.main.dialog.HelpDialog;
import com.ntredize.redstop.main.fragment.DummyFragment;
import com.ntredize.redstop.main.fragment.checkdevice.CheckDeviceFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyListFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyNoSearchResultFragment;
import com.ntredize.redstop.main.fragment.redcompany.StickyRedCompanyListFragment;
import com.ntredize.redstop.main.view.viewpager.MyViewPager;
import com.ntredize.redstop.support.service.CheckDeviceService;
import com.ntredize.redstop.support.service.ThemeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CheckDeviceActivity extends ActivityBase {
	
	private final String TAB_KEY_START = "TAB_KEY_START";
	private final String TAB_KEY_NO_RESULT = "TAB_KEY_NO_RESULT";
	private final String TAB_KEY_DEVICE = "TAB_KEY_DEVICE";
	private final String TAB_KEY_APPS = "TAB_KEY_APPS";
	private final String TAB_KEY_CA_CERTS = "TAB_KEY_CA_CERTS";
	
	// activity
	private CheckDeviceActivity activity;
	
	// service
	private ThemeService themeService;
	private CheckDeviceService checkDeviceService;
	
	// view
	private RelativeLayout loadingContainer;
	private TabLayout tabLayout;
	private MyViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	
	// menu
	private MenuItem helpMenuItem;
	private MenuItem caCertHelpMenuItem;
	
	
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
		checkDeviceService = new CheckDeviceService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(false);
	}
	
	@Override
	protected void initData() {
		// activity
		activity = this;
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_check_device);
		
		// action bar
		Toolbar toolbar = findViewById(R.id.check_device_toolbar);
		setSupportActionBar(toolbar);
		setTitle(R.string.label_check_device);
		
		// view
		loadingContainer = findViewById(R.id.check_device_loading_container);
		tabLayout = findViewById(R.id.check_device_tabs);
		viewPager = findViewById(R.id.check_device_view_pager);
		
		myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), activity, Collections.singletonList(TAB_KEY_START),
				null,null, null);
		viewPager.setAdapter(myPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
	}
	
	
	/* Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_check_device, menu);
		
		// show help after starting checking
		helpMenuItem = menu.findItem(R.id.menu_help_button);
		helpMenuItem.setVisible(false);
		
		// show ca cert button after starting checking, and result have ca certs red companies
		caCertHelpMenuItem = menu.findItem(R.id.menu_ca_cert_help_button);
		caCertHelpMenuItem.setVisible(false);
		
		showBackMenuItem();
		return true;
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.menu_help_button:
				showHelpDialog();
				return true;
			case R.id.menu_ca_cert_help_button:
				showCaCertDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	/* Menu - Back */
	@Override
	public void onBackPressed() {
		finish();
	}
	
	
	/* Menu - Help */
	private void showHelpDialog() {
		AlertDialog helpDialog = new HelpDialog(this).buildDialog();
		if (!isFinishing()) helpDialog.show();
	}
	
	
	/* Menu - CA Cert */
	private void showCaCertDialog() {
		AlertDialog caCertHelpDialog = new CaCertHelpDialog(this).buildDialog();
		if (!isFinishing()) caCertHelpDialog.show();
	}
	
	
	/* When Rotate, Re-Init View */
	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		// when rotate
		if (myPagerAdapter != null) myPagerAdapter.refreshFragments();
	}
	
	
	/* Check Device */
	public void showCheckDevicePrivacyDialog() {
		AlertDialog dialog = new CheckDevicePrivacyDialog(this).buildDialog();
		dialog.show();
	}
	
	public void startCheckDevice() {
		// show loading
		loadingContainer.setVisibility(View.VISIBLE);
		
		// hide view pager
		viewPager.setVisibility(View.GONE);
		
		// load data from server
		new Thread(() -> {
			// get red company by device
			try {
				AndroidDeviceSearchResult searchResult = checkDeviceService.checkAndroidDevice();
				
				// update view
				if (searchResult != null) updateViewAfterLoading(searchResult);
				else updateViewForException(new ApplicationException(this, ApplicationException.HTTP_CLIENT_ERROR));
				
			} catch (ApplicationException e) {
				updateViewForException(e);
			}
		}).start();
	}
	
	private void updateViewForException(ApplicationException e) {
		this.runOnUiThread(() -> {
			// show view pager
			viewPager.setVisibility(View.VISIBLE);
			
			// hide loading
			loadingContainer.setVisibility(View.GONE);
			
			// show error dialog
			errorHandling(e);
		});
	}
	
	private void updateViewAfterLoading(AndroidDeviceSearchResult searchResult) {
		this.runOnUiThread(() -> {
			// menu
			helpMenuItem.setVisible(true);
			
			// tabs
			List<String> tabKeyList = new ArrayList<>();
			
			// tab 1: Device
			if (searchResult.getDeviceRedCompanyList() != null && searchResult.getDeviceRedCompanyList().getTotalNum() > 0) {
				tabKeyList.add(TAB_KEY_DEVICE);
			}
			
			// tab 2: Apps
			if (searchResult.getAppRedCompanyList() != null && searchResult.getAppRedCompanyList().getTotalNum() > 0) {
				tabKeyList.add(TAB_KEY_APPS);
			}
			
			// tab 3: CA Certs
			if (searchResult.getCaCertRedCompanyList() != null && searchResult.getCaCertRedCompanyList().getTotalNum() > 0) {
				tabKeyList.add(TAB_KEY_CA_CERTS);
				caCertHelpMenuItem.setVisible(true);
			}
			
			// tab: No Result
			if (tabKeyList.isEmpty()) {
				tabKeyList.add(TAB_KEY_NO_RESULT);
			}
			
			// view pager
			myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), activity, tabKeyList,
					searchResult.getDeviceRedCompanyList(), searchResult.getAppRedCompanyList(), searchResult.getCaCertRedCompanyList());
			viewPager.setAdapter(myPagerAdapter);
			tabLayout.setupWithViewPager(viewPager);
			
			// show tab if have result
			if (!tabKeyList.contains(TAB_KEY_NO_RESULT)) tabLayout.setVisibility(View.VISIBLE);
			
			// wait 200ms for fragment to be ready
			new Handler().postDelayed(() -> {
				// show view pager
				viewPager.setVisibility(View.VISIBLE);
				
				// hide loading
				loadingContainer.setVisibility(View.GONE);
			}, 200);
		});
	}
	
	
	/* Open Company Detail */
	public void openRedCompanyDetail(RedCompanySimple redCompanySimple) {
		Intent i = new Intent(this, RedCompanyDetailActivity.class);
		i.putExtra(RedCompanyDetailActivity.KEY_RED_COMPANY_CODE, redCompanySimple.getCompanyCode());
		i.putExtra(RedCompanyDetailActivity.KEY_RED_COMPANY_DISPLAY_NAME, redCompanySimple.getDisplayName());
		startActivity(i);
	}
	
	
	/* Page Adapter */
	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		
		private final CheckDeviceActivity activity;
		private final List<String> tabKeyList;
		private final SearchResult<RedCompanySimple> deviceRedCompanySearchResult;
		private final SearchResult<RedCompanySimple> appsRedCompanySearchResult;
		private final SearchResult<RedCompanySimple> caCertsRedCompanySearchResult;
		
		MyPagerAdapter(FragmentManager fm, CheckDeviceActivity activity, List<String> tabKeyList,
		               SearchResult<RedCompanySimple> deviceRedCompanySearchResult,
		               SearchResult<RedCompanySimple> appsRedCompanySearchResult,
		               SearchResult<RedCompanySimple> caCertsRedCompanySearchResult) {
			super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
			this.activity = activity;
			this.tabKeyList = tabKeyList;
			this.deviceRedCompanySearchResult = deviceRedCompanySearchResult;
			this.appsRedCompanySearchResult = appsRedCompanySearchResult;
			this.caCertsRedCompanySearchResult = caCertsRedCompanySearchResult;
		}
		
		@NonNull
		@Override
		public Fragment getItem(int position) {
			if (tabKeyList != null && position < tabKeyList.size()) {
				String tabKey = tabKeyList.get(position);
				
				// start
				if (TAB_KEY_START.equals(tabKey)) {
					return new CheckDeviceFragment();
				}
				
				// no result
				else if (TAB_KEY_NO_RESULT.equals(tabKey)) {
					return new RedCompanyNoSearchResultFragment();
				}
				
				// device
				if (TAB_KEY_DEVICE.equals(tabKey)) {
					RedCompanyListFragment fragment = new RedCompanyListFragment();
					fragment.setArguments(buildDeviceArgs());
					return fragment;
				}
				
				// apps
				else if (TAB_KEY_APPS.equals(tabKey)) {
					RedCompanyListFragment fragment = new RedCompanyListFragment();
					fragment.setArguments(buildAppsArgs());
					return fragment;
				}
				
				// apps
				else if (TAB_KEY_CA_CERTS.equals(tabKey)) {
					RedCompanyListFragment fragment = new RedCompanyListFragment();
					fragment.setArguments(buildCaCertsArgs());
					return fragment;
				}
			}
			return new DummyFragment();
		}
		
		@Override
		public int getCount() {
			// total number of pages
			return (tabKeyList != null) ? tabKeyList.size() : 0;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			if (tabKeyList != null && position < tabKeyList.size()) {
				String tabKey = tabKeyList.get(position);
				
				if (TAB_KEY_DEVICE.equals(tabKey)) return activity.getString(R.string.label_check_device_tab_device);
				else if (TAB_KEY_APPS.equals(tabKey)) {
					int totalNum = appsRedCompanySearchResult != null ? appsRedCompanySearchResult.getTotalNum() : 0;
					return activity.getString(R.string.label_check_device_tab_apps) + " (" + totalNum + ")";
				}
				else if (TAB_KEY_CA_CERTS.equals(tabKey)) {
					int totalNum = caCertsRedCompanySearchResult != null ? caCertsRedCompanySearchResult.getTotalNum() : 0;
					return activity.getString(R.string.label_check_device_tab_ca_certs) + " (" + totalNum + ")";
				}
			}
			return null;
		}
		
		@Override
		public int getItemPosition(@NonNull Object object) {
			// POSITION_NONE makes it possible to reload the PagerAdapter
			return POSITION_NONE;
		}
		
		
		// build fragment arguments
		private Bundle buildDeviceArgs() {
			Bundle args = new Bundle();
			args.putSerializable(StickyRedCompanyListFragment.KEY_RED_COMPANY_SEARCH_RESULT, deviceRedCompanySearchResult);
			return args;
		}
		
		private Bundle buildAppsArgs() {
			Bundle args = new Bundle();
			args.putSerializable(StickyRedCompanyListFragment.KEY_RED_COMPANY_SEARCH_RESULT, appsRedCompanySearchResult);
			return args;
		}
		
		private Bundle buildCaCertsArgs() {
			Bundle args = new Bundle();
			args.putSerializable(StickyRedCompanyListFragment.KEY_RED_COMPANY_SEARCH_RESULT, caCertsRedCompanySearchResult);
			return args;
		}
		
		
		// update fragment
		private void refreshFragments() {
			this.notifyDataSetChanged();
		}
		
	}
	
}
