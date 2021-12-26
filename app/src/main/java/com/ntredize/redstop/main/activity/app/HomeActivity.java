package com.ntredize.redstop.main.activity.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.BuildConfig;
import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.redcompany.RedCompanyCategoryListActivity;
import com.ntredize.redstop.main.drawer.MainDrawerContainer;
import com.ntredize.redstop.main.view.container.NoSwipeDrawerLayout;
import com.ntredize.redstop.support.service.SharedPreferenceService;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

public class HomeActivity extends ActivityBase implements View.OnClickListener {
	
	// service
	private SharedPreferenceService sharedPreferenceService;
	private ThemeService themeService;
	
	// view
	private NoSwipeDrawerLayout mainDrawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	private EditText searchText;
	
	// data
	private boolean isDarkMode;
	
	
	/* Init */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Start Activity");
		
		initService();
		initTheme();
		initData();
		initView();
		initDrawer();
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
		isDarkMode = sharedPreferenceService.isDarkMode();
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_home);
		
		// search section
		searchText = findViewById(R.id.home_search_text);
		ImageButton searchButton = findViewById(R.id.home_search_button);
		
		// category button
		RelativeLayout categoryCompanyButton = findViewById(R.id.home_category_company_button);
		
		// version
		TextView versionText = findViewById(R.id.home_version_text);
		versionText.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME));
		
		// listener
		searchText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		searchText.setOnEditorActionListener((textView, i, keyEvent) -> {
			String text = textView.getText().toString();
			
			if (text.isEmpty()) return true;
			else {
				clearFocusAndHideKeyboard(textView);
				doSearch();
				return false;
			}
		});
		
		searchButton.setOnClickListener(this);
		categoryCompanyButton.setOnClickListener(this);
	}
	
	
	/* Navigation Drawer */
	private void initDrawer() {
		// set drawer toggle
		mainDrawerLayout = findViewById(R.id.main_drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, R.string.label_open, R.string.label_close) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}
			
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		mainDrawerLayout.addDrawerListener(drawerToggle);
		
		// set on click listener
		MainDrawerContainer mainDrawerContainer = findViewById(R.id.main_drawer_container);
		mainDrawerContainer.init(this, mainDrawerLayout, isDarkMode);
		mainDrawerContainer.bringToFront();
		
		// use home button to open and close drawer
		showBackMenuItem();
		setHomeButtonEnable();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		// re-init view when rotate
		initView();
		initDrawer();
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		clearFocusAndHideKeyboard(searchText);
		return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) mainDrawerLayout.closeDrawer(GravityCompat.START);
		else super.onBackPressed();
	}
	
	
	/* Click Event */
	@SuppressLint("NonConstantResourceId")
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.home_search_button:
				doSearch();
				break;
			case R.id.home_category_company_button:
				goToRedCompanyCategoryPage();
				break;
			default:
				break;
		}
	}
	
	
	/* Search */
	private void doSearch() {
		String text = searchText.getText().toString();
		
		if (!text.trim().isEmpty()) {
			Intent i = new Intent(this, SearchResultActivity.class);
			i.putExtra(SearchResultActivity.KEY_SEARCH_KEYWORD, text.trim());
			startActivity(i);
		}
	}
	
	
	/* Update Dark Mode */
	public void updateDarkMode() {
		themeService.changeTheme();
	}
	
	
	/* Go to Suggestion Page */
	public void startSuggestion() {
		Intent i = new Intent(this, SuggestionActivity.class);
		startActivity(i);
	}
	
	
	/* Go to Scan Barcode Page */
	public void startScanBarcode() {
		Intent i = new Intent(this, ScanBarcodeActivity.class);
		startActivity(i);
	}
	
	
	/* Go to Check Device Page */
	public void startCheckDevice() {
		Intent i = new Intent(this, CheckDeviceActivity.class);
		startActivity(i);
	}
	
	
	/* Go to Check Website Page */
	public void startCheckWebsite() {
		Intent i = new Intent(this, CheckWebsiteActivity.class);
		startActivity(i);
	}
	
	
	/* Go to Category Page */
	private void goToRedCompanyCategoryPage() {
		Intent i = new Intent(this, RedCompanyCategoryListActivity.class);
		startActivity(i);
	}
	
}
