package com.ntredize.redstop.main.activity.app;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
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
import com.ntredize.redstop.db.model.RedCompanySearchCriteria;
import com.ntredize.redstop.db.model.RedCompanySimpleWithCategory;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.redcompany.RedCompanyDetailActivity;
import com.ntredize.redstop.main.dialog.HelpDialog;
import com.ntredize.redstop.main.fragment.DummyFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyNoSearchResultFragment;
import com.ntredize.redstop.main.fragment.redcompany.StickyRedCompanyListFragment;
import com.ntredize.redstop.main.popup.SearchSortPopup;
import com.ntredize.redstop.main.view.viewpager.MyViewPager;
import com.ntredize.redstop.support.service.SharedPreferenceService;
import com.ntredize.redstop.support.service.RedCompanyService;
import com.ntredize.redstop.support.service.ThemeService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SearchResultActivity extends ActivityBase implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, View.OnClickListener {
	
	public static final String KEY_SEARCH_KEYWORD = "KEY_SEARCH_KEYWORD";
	
	private static final String TAB_KEY_RED_COMPANY = "TAB_KEY_RED_COMPANY";
	
	// activity
	private SearchResultActivity activity;
	
	// service
	private SharedPreferenceService sharedPreferenceService;
	private ThemeService themeService;
	private RedCompanyService redCompanyService;
	
	// menu
	private SearchView searchView;
	private MenuItem sortMenuItem;
	private MenuItem helpMenuItem;
	
	// view
	private RelativeLayout loadingContainer;
	private Toolbar toolbar;
	private TabLayout tabLayout;
	private MyViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	
	// data
	private boolean finishLoading;
	private RedCompanySearchCriteria searchCriteria;

	
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

		// search criteria
		String searchKeyword = getIntent().getStringExtra(KEY_SEARCH_KEYWORD);
		String sorting = sharedPreferenceService.getSearchSorting();
		searchCriteria = new RedCompanySearchCriteria(searchKeyword, sorting);
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_search_result);
		
		// action bar
		toolbar = findViewById(R.id.search_result_toolbar);
		setSupportActionBar(toolbar);
		setTitle(searchCriteria.getKeyword());
		
		// view
		loadingContainer = activity.findViewById(R.id.search_result_loading_container);
		tabLayout = findViewById(R.id.search_result_tabs);
		viewPager = findViewById(R.id.search_result_view_pager);
	}
	
	public void loadDataFromServer(final Fragment targetFragment) {
		new Thread(() -> {
			// search red company
			SearchResult<RedCompanySimpleWithCategory> redCompanySearchResult =
					redCompanyService.getRedCompanySimpleWithCategoryListBySearchKeyword(searchCriteria);
			
			// add one page to criteria
			searchCriteria.setPage(searchCriteria.getPage() + 1);
			
			// call from activity - update view after loading
			if (targetFragment == null) {
				updateViewAfterLoading(redCompanySearchResult);
			}
			
			// call from fragment - update list
			else {
				if (targetFragment instanceof StickyRedCompanyListFragment) {
					((StickyRedCompanyListFragment) targetFragment).handleLoadMoreData(redCompanySearchResult);
				}
			}
		}).start();
	}
	
	private void updateViewAfterLoading(SearchResult<RedCompanySimpleWithCategory> redCompanySearchResult) {
		this.runOnUiThread(() -> {
			// title
			int redCompanyTotalNum = redCompanySearchResult != null ? redCompanySearchResult.getTotalNum() : 0;
			setTitle(searchCriteria.getKeyword() + " (" + redCompanyTotalNum + ")");
			
			// hide loading
			finishLoading = true;
			loadingContainer.setVisibility(View.GONE);
			
			// tabs
			List<String> tabKeyList = new ArrayList<>();
			
			// tab 1: Red Company
			tabKeyList.add(TAB_KEY_RED_COMPANY);
			
			// view pager
			if (viewPager == null || myPagerAdapter == null) {
				viewPager = findViewById(R.id.search_result_view_pager);
				viewPager.setVisibility(View.VISIBLE);
				
				myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), activity, tabKeyList, isSortingShowStickyHeader(), redCompanySearchResult);
				viewPager.setAdapter(myPagerAdapter);
				tabLayout.setupWithViewPager(viewPager);
			}
			else {
				viewPager.setVisibility(View.VISIBLE);
				myPagerAdapter.updateFragments(tabKeyList, isSortingShowStickyHeader(), redCompanySearchResult);
			}
			
			// if more than one tab, show tab layout
			if (tabKeyList.size() > 1) tabLayout.setVisibility(View.VISIBLE);
			else tabLayout.setVisibility(View.GONE);
		});
	}
	
	
	/* When Rotate, Re-Init View */
	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		// re-init view of sticky list when rotate
		if (myPagerAdapter != null) myPagerAdapter.refreshFragments();
	}
	
	
	/* On Click */
	@SuppressLint("NonConstantResourceId")
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.menu_search_button:
				startSearch();
				break;
			default:
				break;
		}
	}
	
	
	/* Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_search_result, menu);
		showBackMenuItem();
		
		// search
		SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchMenuItem = menu.findItem(R.id.menu_search_button);
		searchView = (SearchView) searchMenuItem.getActionView();
		searchView.setQueryHint(getString(R.string.search_hint));
		
		if (searchManager != null) searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
		searchView.setOnQueryTextListener(this);
		searchView.setOnSearchClickListener(this);
		searchView.setOnCloseListener(this);
		
		// sort
		sortMenuItem = menu.findItem(R.id.menu_sort_button);
		updateSortMenuItemIcon(searchCriteria.getSorting());
		
		// help
		helpMenuItem = menu.findItem(R.id.menu_help_button);
		
		return true;
	}
	
	private void updateSortMenuItemIcon(String sorting) {
		if (sortMenuItem != null) {
			if (RedCompanySorting.NAME.equals(sorting)) sortMenuItem.setIcon(R.drawable.button_image_sort_name);
			else if (RedCompanySorting.CATEGORY.equals(sorting)) sortMenuItem.setIcon(R.drawable.button_image_sort_category);
			else if (RedCompanySorting.STAR.equals(sorting)) sortMenuItem.setIcon(R.drawable.button_image_sort_star);
		}
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				handleBackButton();
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
		handleBackButton();
	}
	
	private void handleBackButton() {
		// if searching, close it
		if (searchView != null && !searchView.isIconified()) {
			searchView.setQuery("", false);
			searchView.setIconified(true);
		}
		
		// else, finish activity
		else {
			finish();
		}
	}
	
	
	/* Menu - Search */
	private void startSearch() {
		// hide other menu item
		if (sortMenuItem != null) sortMenuItem.setVisible(false);
		if (helpMenuItem != null) helpMenuItem.setVisible(false);
		
		// put original search keyword to query
		if (searchView != null) searchView.setQuery(searchCriteria.getKeyword(), false);
	}
	
	@Override
	public boolean onQueryTextChange(String query) {
		return false;
	}
	
	@Override
	public boolean onQueryTextSubmit(String newText) {
		if (newText != null && !newText.trim().isEmpty()) {
			submitNewSearch(newText.trim(), searchCriteria.getSorting());
		}
		return false;
	}
	
	private void submitNewSearch(String newText, String newSorting) {
		searchCriteria = new RedCompanySearchCriteria(newText.trim(), newSorting);
		
		// hide keyboard
		clearFocusAndHideKeyboard(loadingContainer);
		
		// close search view
		searchView.setQuery("", false);
		searchView.setIconified(true);
		
		// update title
		setTitle(newText.trim());
		
		// show loading
		loadingContainer.setVisibility(View.VISIBLE);
		viewPager.setVisibility(View.GONE);
		
		// search
		loadDataFromServer(null);
	}
	
	@Override
	public boolean onClose() {
		// show other menu item
		if (sortMenuItem != null) sortMenuItem.setVisible(true);
		if (helpMenuItem != null) helpMenuItem.setVisible(true);
		
		return false;
	}
	
	
	/* Menu - Sort */
	private void showSortPopup() {
		SearchSortPopup popup = new SearchSortPopup(this, searchCriteria.getSorting());
		popup.show(toolbar.getRootView(), getStatusBarHeight() + getActionBarHeight());
		
		// on dismiss
		// 1. submit new search
		// 2. update sort menu item icon
		// 3. save sorting
		popup.setOnDismissListener(() -> {
			String newSorting = popup.getNewSorting();
			if (!newSorting.equals(searchCriteria.getSorting())) {
				submitNewSearch(searchCriteria.getKeyword(), newSorting);
				updateSortMenuItemIcon(newSorting);
				sharedPreferenceService.setSearchSorting(newSorting);
			}
		});
	}
	
	private boolean isSortingShowStickyHeader() {
		return RedCompanySorting.CATEGORY.equals(searchCriteria.getSorting());
	}
	
	
	/* Menu - Help */
	private void showHelpDialog() {
		AlertDialog helpDialog = new HelpDialog(this).buildDialog();
		if (!isFinishing()) helpDialog.show();
	}
	
	
	/* Open Company Detail */
	public void openRedCompanyDetail(RedCompanySimpleWithCategory redCompanySimpleWithCategory) {
		Intent i = new Intent(this, RedCompanyDetailActivity.class);
		i.putExtra(RedCompanyDetailActivity.KEY_RED_COMPANY_CODE, redCompanySimpleWithCategory.getCompanyCode());
		i.putExtra(RedCompanyDetailActivity.KEY_RED_COMPANY_DISPLAY_NAME, redCompanySimpleWithCategory.getDisplayName());
		startActivity(i);
	}
	
	
	/* Page Adapter */
	private static class MyPagerAdapter extends FragmentStatePagerAdapter {
		
		private final SearchResultActivity activity;
		private List<String> tabKeyList;
		private boolean showStickyHeader;
		private SearchResult<RedCompanySimpleWithCategory> redCompanySearchResult;
		
		MyPagerAdapter(FragmentManager fm, SearchResultActivity activity, List<String> tabKeyList, boolean showStickyHeader,
		               SearchResult<RedCompanySimpleWithCategory> redCompanySearchResult) {
			super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
			this.activity = activity;
			this.tabKeyList = tabKeyList;
			this.showStickyHeader = showStickyHeader;
			this.redCompanySearchResult = redCompanySearchResult;
		}
		
		@NonNull
		@Override
		public Fragment getItem(int position) {
			if (tabKeyList != null && position < tabKeyList.size()) {
				String tabKey = tabKeyList.get(position);
				
				// red company
				if (TAB_KEY_RED_COMPANY.equals(tabKey)) {
					// no search result
					if (redCompanySearchResult == null || redCompanySearchResult.getTotalNum() <= 0) {
						return new RedCompanyNoSearchResultFragment();
					}

					// normal
					else {
						StickyRedCompanyListFragment fragment = new StickyRedCompanyListFragment();
						fragment.setArguments(buildRedCompanyArgs());
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
				
				if (TAB_KEY_RED_COMPANY.equals(tabKey)) {
					int totalNum = redCompanySearchResult != null ? redCompanySearchResult.getTotalNum() : 0;
					return activity.getString(R.string.label_search_company) + " (" + totalNum + ")";
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
		private Bundle buildRedCompanyArgs() {
			Bundle args = new Bundle();
			args.putSerializable(StickyRedCompanyListFragment.KEY_SHOW_STICKY_HEADER, showStickyHeader);
			args.putSerializable(StickyRedCompanyListFragment.KEY_RED_COMPANY_SEARCH_RESULT, redCompanySearchResult);
			return args;
		}
		
		
		// update fragment
		private void updateFragments(List<String> tabKeyList, boolean showStickyHeader,
		                             SearchResult<RedCompanySimpleWithCategory> redCompanySearchResult) {
			this.tabKeyList = tabKeyList;
			this.showStickyHeader = showStickyHeader;
			this.redCompanySearchResult = redCompanySearchResult;
			this.notifyDataSetChanged();
		}
		
		private void refreshFragments() {
			this.notifyDataSetChanged();
		}
		
	}
	
}
