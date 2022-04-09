package com.ntredize.redstop.main.activity.redcompany;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.ntredize.redstop.R;
import com.ntredize.redstop.common.config.PermissionRequestCode;
import com.ntredize.redstop.db.model.AndroidDeviceAppPackageInfo;
import com.ntredize.redstop.db.model.AndroidDeviceCaCert;
import com.ntredize.redstop.db.model.RedCompanyDetail;
import com.ntredize.redstop.db.model.RedCompanyGroupSearchCriteria;
import com.ntredize.redstop.db.model.RedCompanySimpleWithCategory;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.app.SuggestionActivity;
import com.ntredize.redstop.main.dialog.HelpDialog;
import com.ntredize.redstop.main.fragment.DummyFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyAppsListFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyCaCertsListFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyDetailInfoFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyNoSearchResultFragment;
import com.ntredize.redstop.main.fragment.redcompany.StickyRedCompanyListFragment;
import com.ntredize.redstop.main.view.viewpager.MyViewPager;
import com.ntredize.redstop.support.service.CheckDeviceService;
import com.ntredize.redstop.support.service.DownloadImageService;
import com.ntredize.redstop.support.service.SharedPreferenceService;
import com.ntredize.redstop.support.service.RedCompanyService;
import com.ntredize.redstop.support.service.ShareService;
import com.ntredize.redstop.support.service.ThemeService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class RedCompanyDetailActivity extends ActivityBase {
	
	public static final String KEY_RED_COMPANY_CODE = "KEY_RED_COMPANY_CODE";
	public static final String KEY_RED_COMPANY_DISPLAY_NAME = "KEY_RED_COMPANY_DISPLAY_NAME";
	public static final String KEY_ANDROID_DEVICE_APP_PACKAGE_INFOS = "KEY_ANDROID_DEVICE_APP_PACKAGE_INFOS";
	public static final String KEY_ANDROID_DEVICE_CA_CERTS = "KEY_ANDROID_DEVICE_CA_CERTS";
	
	private final String TAB_KEY_RED_COMPANY_DETAIL_INFO = "TAB_KEY_RED_COMPANY_DETAIL_INFO";
	private final String TAB_KEY_RED_COMPANY_RELATED_COMPANY = "TAB_KEY_RED_COMPANY_RELATED_COMPANY";
	private final String TAB_KEY_RED_COMPANY_APPS = "TAB_KEY_RED_COMPANY_APPS";
	private final String TAB_KEY_RED_COMPANY_CA_CERTS = "TAB_KEY_RED_COMPANY_CA_CERTS";
	
	// activity
	private RedCompanyDetailActivity activity;
	
	// service
	private SharedPreferenceService sharedPreferenceService;
	private ThemeService themeService;
	private RedCompanyService redCompanyService;
	private DownloadImageService downloadImageService;
	private CheckDeviceService checkDeviceService;
	private ShareService shareService;
	
	// view
	private RelativeLayout loadingContainer;
	private TabLayout tabLayout;
	private MyViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	
	// data
	private boolean isDarkMode;
	private boolean finishLoading;
	private String redCompanyCode;
	private String redCompanyDisplayName;
	private String redCompanyGroupCode;
	private RedCompanyDetail redCompanyDetail;
	private RedCompanyGroupSearchCriteria relatedRedCompanySearchCriteria;
	private SearchResult<RedCompanySimpleWithCategory> relatedRedCompanySearchResult;
	private List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos;
	private List<AndroidDeviceCaCert> androidDeviceCaCerts;
	private String logoFileName;
	private CountDownLatch countDownLatch;
	
	
	/* Init */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Start Activity");
		
		initService();
		initTheme();
		initData();
		initView();

		loadDataFromServer();
	}
	
	@Override
	protected void initService() {
		sharedPreferenceService = new SharedPreferenceService(this);
		themeService = new ThemeService(this);
		redCompanyService = new RedCompanyService(this);
		downloadImageService = new DownloadImageService(this);
		checkDeviceService = new CheckDeviceService(this);
		shareService = new ShareService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(false);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void initData() {
		// activity
		activity = this;
		
		// is dark mode
		isDarkMode = sharedPreferenceService.isDarkMode();
		
		// company code and display name (from link)
		if (getIntent().getAction() != null && Intent.ACTION_VIEW.equals(getIntent().getAction())) {
			redCompanyCode = redCompanyService.getRedCompanyCodeFromUri(getIntent().getData());
		}
		
		// company code and display name (normal case)
		else {
			redCompanyCode = getIntent().getStringExtra(KEY_RED_COMPANY_CODE);
			redCompanyDisplayName = getIntent().getStringExtra(KEY_RED_COMPANY_DISPLAY_NAME);
			androidDeviceAppPackageInfos = (List<AndroidDeviceAppPackageInfo>) getIntent().getSerializableExtra(KEY_ANDROID_DEVICE_APP_PACKAGE_INFOS);
			androidDeviceCaCerts = (List<AndroidDeviceCaCert>) getIntent().getSerializableExtra(KEY_ANDROID_DEVICE_CA_CERTS);
		}
		
		if (redCompanyCode == null) finish();
		
		// company group code and related company search criteria
		// - format must be [groupCode]_xxxxxxx
		redCompanyGroupCode = redCompanyCode.split("_")[0];
		relatedRedCompanySearchCriteria = new RedCompanyGroupSearchCriteria(redCompanyGroupCode, redCompanyCode);
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_red_company_detail);
		
		// action bar
		Toolbar toolbar = findViewById(R.id.red_company_detail_toolbar);
		setSupportActionBar(toolbar);
		setTitle(redCompanyDisplayName);
		
		// view
		loadingContainer = findViewById(R.id.red_company_detail_loading_container);
		tabLayout = findViewById(R.id.red_company_detail_tabs);
		viewPager = findViewById(R.id.red_company_detail_view_pager);
	}
	
	private void loadDataFromServer() {
		new Thread(() -> {
			countDownLatch = new CountDownLatch(3);
			
			// get red company detail
			// get red company logo
			// get red company related companies
			loadRedCompanyDetailFromServer();
			loadRedCompanyLogoFromServer();
			loadRelatedRedCompanyFromServer(null);
			
			// wait for all threads finish
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage(), e);
			} finally {
				updateViewAfterLoading();
			}
		}).start();
	}
	
	private void loadRedCompanyDetailFromServer() {
		new Thread(() -> {
			// get red company detail
			redCompanyDetail = redCompanyService.getRedCompanyDetailByCompanyCode(redCompanyCode, androidDeviceAppPackageInfos != null, androidDeviceCaCerts != null);
			if (redCompanyDetail != null) androidDeviceAppPackageInfos = checkDeviceService.filterRedCompanyAndroidDeviceAppPackageInfos(androidDeviceAppPackageInfos, redCompanyDetail.getAndroidPackages());
			if (redCompanyDetail != null) androidDeviceCaCerts = checkDeviceService.filterRedCompanyAndroidDeviceCaCerts(androidDeviceCaCerts, redCompanyDetail.getCaCertOrganizations());
			
			// count down latch
			countDownLatch.countDown();
		}).start();
	}
	
	private void loadRedCompanyLogoFromServer() {
		new Thread(() -> {
			// get logo
			String logoUrl = downloadImageService.getRedCompanyImageUrl(redCompanyGroupCode, redCompanyCode);
			Bitmap redCompanyLogo = downloadImageService.getImageByUrl(logoUrl);
			logoFileName = downloadImageService.getTempRedCompanyDetailImageFileName();
			downloadImageService.saveTempRedCompanyImage(logoFileName, redCompanyLogo);
			
			// count down latch
			countDownLatch.countDown();
		}).start();
	}

	public void loadRelatedRedCompanyFromServer(final Fragment targetFragment) {
		new Thread(() -> {
			// get related red company
			relatedRedCompanySearchResult = redCompanyService.getRedCompanySimpleWithCategoryListByGroup(relatedRedCompanySearchCriteria);
			
			// add one page to criteria
			relatedRedCompanySearchCriteria.setPage(relatedRedCompanySearchCriteria.getPage() + 1);
			
			// call from activity - count down latch
			if (targetFragment == null) {
				countDownLatch.countDown();
			}
			
			// call from fragment - update list
			else {
				if (targetFragment instanceof StickyRedCompanyListFragment) {
					((StickyRedCompanyListFragment) targetFragment).handleLoadMoreData(relatedRedCompanySearchResult);
				}
			}
		}).start();
	}
	
	private void updateViewAfterLoading() {
		this.runOnUiThread(() -> {
			// title
			if (redCompanyDetail != null) setTitle(redCompanyDetail.getDisplayName());
			
			// hide loading
			finishLoading = true;
			loadingContainer.setVisibility(View.GONE);
			
			// tabs
			List<String> tabKeyList = new ArrayList<>();
			
			// tab 1: Company Detail
			tabKeyList.add(TAB_KEY_RED_COMPANY_DETAIL_INFO);
			
			// tab 2: Related Company (Show if have related company only)
			if (redCompanyDetail != null && relatedRedCompanySearchResult != null && relatedRedCompanySearchResult.getTotalNum() > 0) {
				tabKeyList.add(TAB_KEY_RED_COMPANY_RELATED_COMPANY);
			}
			
			// tab 3: Apps (Show if have apps only)
			if (redCompanyDetail != null && androidDeviceAppPackageInfos != null && !androidDeviceAppPackageInfos.isEmpty()) {
				tabKeyList.add(TAB_KEY_RED_COMPANY_APPS);
			}
			
			// tab 4: CA Certs (Show if have ca certs only)
			if (redCompanyDetail != null && androidDeviceCaCerts != null && !androidDeviceCaCerts.isEmpty()) {
				tabKeyList.add(TAB_KEY_RED_COMPANY_CA_CERTS);
			}

			// view pager
			viewPager.setVisibility(View.VISIBLE);
			myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), activity, tabKeyList, redCompanyDetail, relatedRedCompanySearchResult, androidDeviceAppPackageInfos, androidDeviceCaCerts);
			viewPager.setAdapter(myPagerAdapter);
			tabLayout.setupWithViewPager(viewPager);
			
			// if more than one tab, show tab layout
			if (tabKeyList.size() > 1) tabLayout.setVisibility(View.VISIBLE);
		});
	}
	
	
	/* When Rotate, Re-Init View */
	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		// re-init view of sticky list when rotate
		if (myPagerAdapter != null) myPagerAdapter.refreshFragments();
	}
	
	
	/* Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_red_company_detail, menu);
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
			case R.id.menu_share_button:
				if (finishLoading) checkPermissionForShare(PermissionRequestCode.SHARE_RED_COMPANY);
				return true;
			case R.id.menu_suggestion_button:
				if (finishLoading) goToSuggestionPage();
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
	
	
	/* Menu - Share */
	private void checkPermissionForShare(int requestCode) {
		if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode)) shareRedCompany(true);
	}
	
	@Override
	protected void returnFromPermission(int requestCode) {
		if (requestCode == PermissionRequestCode.SHARE_RED_COMPANY) shareRedCompany(true);
	}
	
	@Override
	protected void failFromPermission(int requestCode) {
		if (requestCode == PermissionRequestCode.SHARE_RED_COMPANY) shareRedCompany(false);
	}
	
	private void shareRedCompany(boolean includeLogo) {
		File logoFile = includeLogo ? downloadImageService.getTempRedCompanyImageFile(logoFileName) : null;
		Uri logoUri = includeLogo ? shareService.getShareImageUri(logoFile) : null;
		String msg = shareService.buildShareRedCompanyMsg(redCompanyDetail);
		
		if (logoUri != null) sendImageMsg(logoUri, msg);
		else sendTextMsg(msg);
	}
	
	
	/* Menu - Suggestion */
	private void goToSuggestionPage() {
		Intent i = new Intent(this, SuggestionActivity.class);
		i.putExtra(SuggestionActivity.KEY_RED_COMPANY_CODE, redCompanyDetail.getCompanyCode());
		i.putExtra(SuggestionActivity.KEY_RED_COMPANY_DISPLAY_NAME, redCompanyDetail.getDisplayName());
		startActivity(i);
	}
	
	
	/* Menu - Help */
	private void showHelpDialog() {
		AlertDialog helpDialog = new HelpDialog(this).buildDialog();
		if (!isFinishing()) helpDialog.show();
	}
	
	
	/* Action - Open Related Company */
	public void openRelatedRedCompanyDetail(RedCompanySimpleWithCategory redCompanySimpleWithCategory) {
		Intent i = new Intent(this, RelatedRedCompanyDetailActivity.class);
		i.putExtra(RelatedRedCompanyDetailActivity.KEY_RELATED_RED_COMPANY_CODE, redCompanySimpleWithCategory.getCompanyCode());
		i.putExtra(RelatedRedCompanyDetailActivity.KEY_RELATED_RED_COMPANY_DISPLAY_NAME, redCompanySimpleWithCategory.getDisplayName());
		this.startActivity(i);
	}
	
	
	
	/* Page Adapter */
	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		
		private final RedCompanyDetailActivity activity;
		private final List<String> tabKeyList;
		private final RedCompanyDetail redCompanyDetail;
		private final SearchResult<RedCompanySimpleWithCategory> relatedRedCompanySearchResult;
		private final List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos;
		private final List<AndroidDeviceCaCert> androidDeviceCaCerts;
		
		MyPagerAdapter(FragmentManager fm, RedCompanyDetailActivity activity, List<String> tabKeyList,
		               RedCompanyDetail redCompanyDetail, SearchResult<RedCompanySimpleWithCategory> relatedRedCompanySearchResult,
		               List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos, List<AndroidDeviceCaCert> androidDeviceCaCerts) {
			super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
			this.activity = activity;
			this.tabKeyList = tabKeyList;
			this.redCompanyDetail = redCompanyDetail;
			this.relatedRedCompanySearchResult = relatedRedCompanySearchResult;
			this.androidDeviceAppPackageInfos = androidDeviceAppPackageInfos;
			this.androidDeviceCaCerts = androidDeviceCaCerts;
		}
		
		@NonNull
		@Override
		public Fragment getItem(int position) {
			if (tabKeyList != null && position < tabKeyList.size()) {
				String tabKey = tabKeyList.get(position);

				// red company
				if (TAB_KEY_RED_COMPANY_DETAIL_INFO.equals(tabKey)) {
					// no result
					if (redCompanyDetail == null) {
						return new RedCompanyNoSearchResultFragment();
					}

					// normal case
					else {
						RedCompanyDetailInfoFragment fragment = new RedCompanyDetailInfoFragment();
						fragment.setArguments(buildInfoArgs());
						return fragment;
					}
				}

				// related red company
				else if (TAB_KEY_RED_COMPANY_RELATED_COMPANY.equals(tabKey)) {
					StickyRedCompanyListFragment fragment = new StickyRedCompanyListFragment();
					fragment.setArguments(buildRelatedCompanyArgs());
					return fragment;
				}
				
				// apps
				else if (TAB_KEY_RED_COMPANY_APPS.equals(tabKey)) {
					RedCompanyAppsListFragment fragment = new RedCompanyAppsListFragment();
					fragment.setArguments(buildAppsArgs());
					return fragment;
				}
				
				// ca certs
				else if (TAB_KEY_RED_COMPANY_CA_CERTS.equals(tabKey)) {
					RedCompanyCaCertsListFragment fragment = new RedCompanyCaCertsListFragment();
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
				
				if (TAB_KEY_RED_COMPANY_DETAIL_INFO.equals(tabKey)) return activity.getString(R.string.label_red_company_detail_info);
				else if (TAB_KEY_RED_COMPANY_RELATED_COMPANY.equals(tabKey)) {
					int totalNum = relatedRedCompanySearchResult != null ? relatedRedCompanySearchResult.getTotalNum() : 0;
					return activity.getString(R.string.label_red_company_related_company) + " (" + totalNum + ")";
				}
				else if (TAB_KEY_RED_COMPANY_APPS.equals(tabKey)) {
					int totalNum = androidDeviceAppPackageInfos != null ? androidDeviceAppPackageInfos.size() : 0;
					return activity.getString(R.string.label_red_company_apps) + " (" + totalNum + ")";
				}
				else if (TAB_KEY_RED_COMPANY_CA_CERTS.equals(tabKey)) {
					int totalNum = androidDeviceCaCerts != null ? androidDeviceCaCerts.size() : 0;
					return activity.getString(R.string.label_red_company_ca_certs) + " (" + totalNum + ")";
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
		private Bundle buildInfoArgs() {
			Bundle args = new Bundle();
			args.putBoolean(RedCompanyDetailInfoFragment.KEY_IS_DARK_MODE, isDarkMode);
			args.putSerializable(RedCompanyDetailInfoFragment.KEY_RED_COMPANY_DETAIL, redCompanyDetail);
			args.putString(RedCompanyDetailInfoFragment.KEY_RED_COMPANY_LOGO, logoFileName);
			return args;
		}
		
		private Bundle buildRelatedCompanyArgs() {
			Bundle args = new Bundle();
			args.putBoolean(StickyRedCompanyListFragment.KEY_SHOW_STICKY_HEADER, true);
			args.putSerializable(StickyRedCompanyListFragment.KEY_RED_COMPANY_SEARCH_RESULT, relatedRedCompanySearchResult);
			return args;
		}
		
		private Bundle buildAppsArgs() {
			Bundle args = new Bundle();
			args.putSerializable(RedCompanyAppsListFragment.KEY_ANDROID_DEVICE_APP_PACKAGE_INFOS, new ArrayList<>(androidDeviceAppPackageInfos));
			return args;
		}
		
		private Bundle buildCaCertsArgs() {
			Bundle args = new Bundle();
			args.putSerializable(RedCompanyCaCertsListFragment.KEY_ANDROID_DEVICE_CA_CERTS, new ArrayList<>(androidDeviceCaCerts));
			return args;
		}
		
		
		// update fragment
		private void refreshFragments() {
			this.notifyDataSetChanged();
		}
		
	}
	
}
