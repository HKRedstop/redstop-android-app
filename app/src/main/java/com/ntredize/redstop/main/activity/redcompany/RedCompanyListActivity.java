package com.ntredize.redstop.main.activity.redcompany;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.ntredize.redstop.R;
import com.ntredize.redstop.common.constants.RedCompanySorting;
import com.ntredize.redstop.db.model.RedCompanySubCategory;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.db.model.RedCompanySubCategorySearchCriteria;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.dialog.HelpDialog;
import com.ntredize.redstop.main.fragment.DummyFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyListFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyNoSearchResultFragment;
import com.ntredize.redstop.main.popup.RedCompanySubCategorySortPopup;
import com.ntredize.redstop.main.view.viewpager.MyViewPager;
import com.ntredize.redstop.support.service.SharedPreferenceService;
import com.ntredize.redstop.support.service.RedCompanyService;
import com.ntredize.redstop.support.service.ThemeService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class RedCompanyListActivity extends ActivityBase {
	
	public static final String KEY_RED_COMPANY_SUB_CATEGORY_CODE = "KEY_RED_COMPANY_SUB_CATEGORY_CODE";

	private static final String TAB_KEY_RED_COMPANY = "TAB_KEY_RED_COMPANY";
	
	// activity
	private RedCompanyListActivity activity;
	
	// service
	private SharedPreferenceService sharedPreferenceService;
	private ThemeService themeService;
	private RedCompanyService redCompanyService;
	
	// menu
	private MenuItem sortMenuItem;

	// view
	private RelativeLayout loadingContainer;
	private Toolbar toolbar;
	private TabLayout tabLayout;
	private MyViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	
	// data
	private boolean finishLoading;
	private RedCompanySubCategory subCategory;
	private RedCompanySubCategorySearchCriteria searchCriteria;
	
	
	/* Init */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Start Activity");
		
		initService();
		initTheme();
		initData();
		initView();

		loadDataFromServer(null);
	}
	
	@Override
	protected void initService() {
		sharedPreferenceService = new SharedPreferenceService(this);
		themeService = new ThemeService(this);
		redCompanyService = new RedCompanyService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(false);
	}
	
	@Override
	protected void initData() {
		// activity
		activity = this;
		
		// sub category
		String subCategoryCode = getIntent().getStringExtra(KEY_RED_COMPANY_SUB_CATEGORY_CODE);
		if (subCategoryCode == null) finish();
		subCategory = redCompanyService.getRedCompanySubCategoryBySubCategoryCode(subCategoryCode);

		// search criteria
		String sorting = sharedPreferenceService.getRedCompanySubCategorySorting();
		searchCriteria = new RedCompanySubCategorySearchCriteria(subCategoryCode, sorting);
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_red_company_list);

		// action bar
		toolbar = findViewById(R.id.red_company_list_toolbar);
		setSupportActionBar(toolbar);
		setTitle(subCategory.getName());
		
		// view
		loadingContainer = findViewById(R.id.red_company_list_loading_container);
		tabLayout = findViewById(R.id.red_company_list_tabs);
		viewPager = findViewById(R.id.red_company_list_view_pager);
	}
	
	public void loadDataFromServer(final Fragment targetFragment) {
		new Thread(() -> {
			// get red company by sub category
			SearchResult<RedCompanySimple> searchResult = redCompanyService.getRedCompanySimpleListBySubCategory(searchCriteria);
			
			// add one page to criteria
			searchCriteria.setPage(searchCriteria.getPage() + 1);
			
			// call from activity - update view after loading
			if (targetFragment == null) {
				updateViewAfterLoading(searchResult);
			}
			
			// call from fragment - update list
			else {
				if (targetFragment instanceof RedCompanyListFragment) {
					((RedCompanyListFragment) targetFragment).handleLoadMoreData(searchResult);
				}
			}
		}).start();
	}

	private void updateViewAfterLoading(SearchResult<RedCompanySimple> searchResult) {
		this.runOnUiThread(() -> {
			// title
			int totalNum = searchResult != null ? searchResult.getTotalNum() : 0;
			setTitle(subCategory.getName() + " (" + totalNum + ")");

			// hide loading
			finishLoading = true;
			loadingContainer.setVisibility(View.GONE);

			// tabs
			List<String> tabKeyList = new ArrayList<>();

			// tab 1: Red Company
			tabKeyList.add(TAB_KEY_RED_COMPANY);

			// view pager
			viewPager.setVisibility(View.VISIBLE);

			myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), activity, tabKeyList, searchResult);
			viewPager.setAdapter(myPagerAdapter);
			tabLayout.setupWithViewPager(viewPager);

			// if more than one tab, show tab layout
			if (tabKeyList.size() > 1) tabLayout.setVisibility(View.VISIBLE);
		});
	}
	
	private void submitNewSearch(String newSorting) {
		searchCriteria = new RedCompanySubCategorySearchCriteria(subCategory.getSubCategoryCode(), newSorting);
		
		// show loading
		loadingContainer.setVisibility(View.VISIBLE);
		viewPager.setVisibility(View.GONE);
		
		// search
		loadDataFromServer(null);
	}


	/* When Rotate, Re-Init View */
	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// re-init view of list when rotate
		if (myPagerAdapter != null) myPagerAdapter.refreshFragments();
	}
	
	
	/* Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_red_company_list, menu);
		showBackMenuItem();
		
		// sort
		sortMenuItem = menu.findItem(R.id.menu_sort_button);
		updateSortMenuItemIcon(searchCriteria.getSorting());
		
		return true;
	}
	
	private void updateSortMenuItemIcon(String sorting) {
		if (sortMenuItem != null) {
			if (RedCompanySorting.POPULAR.equals(sorting)) sortMenuItem.setIcon(R.drawable.button_image_sort_popular);
			else if (RedCompanySorting.NAME.equals(sorting)) sortMenuItem.setIcon(R.drawable.button_image_sort_name);
			else if (RedCompanySorting.STAR.equals(sorting)) sortMenuItem.setIcon(R.drawable.button_image_sort_star);
		}
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.menu_sort_button:
				if (finishLoading) showSortPopup();
				return true;
			case R.id.menu_help_button:
				if (finishLoading) showHelpDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	
	/* Menu - Sort */
	private void showSortPopup() {
		RedCompanySubCategorySortPopup popup = new RedCompanySubCategorySortPopup(this, searchCriteria.getSorting());
		popup.show(toolbar.getRootView(), getStatusBarHeight() + getActionBarHeight());
		
		// on dismiss
		// 1. submit new search
		// 2. update sort menu item icon
		// 3. save sorting
		popup.setOnDismissListener(() -> {
			String newSorting = popup.getNewSorting();
			if (!newSorting.equals(searchCriteria.getSorting())) {
				submitNewSearch(newSorting);
				updateSortMenuItemIcon(newSorting);
				sharedPreferenceService.setRedCompanySubCategorySorting(newSorting);
			}
		});
	}
	
	
	/* Menu - Help */
	private void showHelpDialog() {
		AlertDialog helpDialog = new HelpDialog(this).buildDialog();
		if (!isFinishing()) helpDialog.show();
	}
	
	
	/* Open Company Detail */
	public void openRedCompanyDetail(RedCompanySimple redCompanySimple) {
		Intent i = new Intent(this, RedCompanyDetailActivity.class);
		i.putExtra(RedCompanyDetailActivity.KEY_RED_COMPANY_CODE, redCompanySimple.getCompanyCode());
		i.putExtra(RedCompanyDetailActivity.KEY_RED_COMPANY_DISPLAY_NAME, redCompanySimple.getDisplayName());
		startActivity(i);
	}


	/* Page Adapter */
	private static class MyPagerAdapter extends FragmentStatePagerAdapter {

		private final RedCompanyListActivity activity;
		private final List<String> tabKeyList;
		private final SearchResult<RedCompanySimple> searchResult;

		MyPagerAdapter(FragmentManager fm, RedCompanyListActivity activity, List<String> tabKeyList, SearchResult<RedCompanySimple> searchResult) {
			super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
			this.activity = activity;
			this.tabKeyList = tabKeyList;
			this.searchResult = searchResult;
		}

		@NonNull
		@Override
		public Fragment getItem(int position) {
			if (tabKeyList != null && position < tabKeyList.size()) {
				String tabKey = tabKeyList.get(position);

				// red company
				if (TAB_KEY_RED_COMPANY.equals(tabKey)) {
					// no search result
					if (searchResult == null || searchResult.getTotalNum() <= 0) {
						return new RedCompanyNoSearchResultFragment();
					}

					// normal
					else {
						RedCompanyListFragment fragment = new RedCompanyListFragment();
						fragment.setArguments(buildCompanyArgs());
						return fragment;
					}
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

				if (TAB_KEY_RED_COMPANY.equals(tabKey)) return activity.getString(R.string.label_search_company);
			}
			return null;
		}

		@Override
		public int getItemPosition(@NonNull Object object) {
			// POSITION_NONE makes it possible to reload the PagerAdapter
			return POSITION_NONE;
		}


		// build fragment arguments
		private Bundle buildCompanyArgs() {
			Bundle args = new Bundle();
			args.putSerializable(RedCompanyListFragment.KEY_RED_COMPANY_SEARCH_RESULT, searchResult);
			return args;
		}

		private void refreshFragments() {
			this.notifyDataSetChanged();
		}

	}
	
}
