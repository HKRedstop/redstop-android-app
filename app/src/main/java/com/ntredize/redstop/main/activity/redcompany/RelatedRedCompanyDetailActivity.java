package com.ntredize.redstop.main.activity.redcompany;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.config.PermissionRequestCode;
import com.ntredize.redstop.db.model.RedCompanyDetail;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.dialog.HelpDialog;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyDetailInfoFragment;
import com.ntredize.redstop.main.fragment.redcompany.RedCompanyNoSearchResultFragment;
import com.ntredize.redstop.support.service.DownloadImageService;
import com.ntredize.redstop.support.service.SharedPreferenceService;
import com.ntredize.redstop.support.service.RedCompanyService;
import com.ntredize.redstop.support.service.ShareService;
import com.ntredize.redstop.support.service.ThemeService;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import androidx.appcompat.app.AlertDialog;

public class RelatedRedCompanyDetailActivity extends ActivityBase {
	
	public static final String KEY_RELATED_RED_COMPANY_CODE = "KEY_RELATED_RED_COMPANY_CODE";
	public static final String KEY_RELATED_RED_COMPANY_DISPLAY_NAME = "KEY_RELATED_RED_COMPANY_DISPLAY_NAME";
	
	// service
	private SharedPreferenceService sharedPreferenceService;
	private ThemeService themeService;
	private RedCompanyService redCompanyService;
	private DownloadImageService downloadImageService;
	private ShareService shareService;
	
	// view
	private RelativeLayout loadingContainer;
	private int fragmentContainerId;
	private RelativeLayout fragmentContainer;
	
	// data
	private boolean isDarkMode;
	private boolean finishLoading;
	private String relatedRedCompanyCode;
	private String relatedRedCompanyDisplayName;
	private String relatedRedCompanyGroupCode;
	private RedCompanyDetail relatedRedCompanyDetail;
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
		shareService = new ShareService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(true);
	}
	
	@Override
	protected void initData() {
		// is dark mode
		this.isDarkMode = sharedPreferenceService.isDarkMode();
		
		// company detail
		this.relatedRedCompanyCode = getIntent().getStringExtra(KEY_RELATED_RED_COMPANY_CODE);
		this.relatedRedCompanyDisplayName = getIntent().getStringExtra(KEY_RELATED_RED_COMPANY_DISPLAY_NAME);
		
		// company group code
		// - format must be [groupCode]_xxxxxxx
		this.relatedRedCompanyGroupCode = relatedRedCompanyCode.split("_")[0];
	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_red_company_detail_related);

		// title
		setTitle(relatedRedCompanyDisplayName);
		
		// view
		loadingContainer = findViewById(R.id.related_red_company_detail_loading_container);
		fragmentContainerId = R.id.related_red_company_detail_fragment_container;
		fragmentContainer = findViewById(fragmentContainerId);
	}

	private void loadDataFromServer() {
		new Thread(() -> {
			countDownLatch = new CountDownLatch(2);
			
			// get red company detail
			// get red company logo
			loadRelatedRedCompanyDetailFromServer();
			loadRelatedRedCompanyLogoFromServer();
			
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
	
	private void loadRelatedRedCompanyDetailFromServer() {
		new Thread(() -> {
			// get red company detail
			relatedRedCompanyDetail = redCompanyService.getRedCompanyDetailByCompanyCode(relatedRedCompanyCode);
			
			// count down latch
			countDownLatch.countDown();
		}).start();
	}
	
	private void loadRelatedRedCompanyLogoFromServer() {
		new Thread(() -> {
			// get logo
			String logoUrl = downloadImageService.getRedCompanyImageUrl(relatedRedCompanyGroupCode, relatedRedCompanyCode);
			Bitmap redCompanyLogo = downloadImageService.getImageByUrl(logoUrl);
			logoFileName = downloadImageService.getTempRedCompanyRelatedDetailImageFileName();
			downloadImageService.saveTempRedCompanyImage(logoFileName, redCompanyLogo);
			
			// count down latch
			countDownLatch.countDown();
		}).start();
	}

	private void updateViewAfterLoading() {
		this.runOnUiThread(() -> {
			// title
			if (relatedRedCompanyDetail != null) setTitle(relatedRedCompanyDetail.getDisplayName());
			
			// hide loading
			finishLoading = true;
			loadingContainer.setVisibility(View.GONE);

			// fragment container
			fragmentContainer.setVisibility(View.VISIBLE);

			// red company detail info fragment
			if (relatedRedCompanyDetail != null) {
				RedCompanyDetailInfoFragment fragment = new RedCompanyDetailInfoFragment();
				
				// fragment args
				Bundle args = new Bundle();
				args.putBoolean(RedCompanyDetailInfoFragment.KEY_IS_DARK_MODE, isDarkMode);
				args.putSerializable(RedCompanyDetailInfoFragment.KEY_RED_COMPANY_DETAIL, relatedRedCompanyDetail);
				args.putString(RedCompanyDetailInfoFragment.KEY_RED_COMPANY_LOGO, logoFileName);
				fragment.setArguments(args);
				
				// show fragment
				this.showFragment(fragmentContainerId, fragment);
			}
			
			// no result fragment
			else {
				RedCompanyNoSearchResultFragment fragment = new RedCompanyNoSearchResultFragment();
				this.showFragment(fragmentContainerId, fragment);
			}
		});
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
		File logoFile = downloadImageService.getTempRedCompanyImageFile(logoFileName);
		Uri logoUri = includeLogo ? shareService.getShareImageUri(logoFile) : null;
		String msg = shareService.buildShareRedCompanyMsg(relatedRedCompanyDetail);
		
		if (logoUri  != null) sendImageMsg(logoUri , msg);
		else sendTextMsg(msg);
	}
	
	
	/* Menu - Help */
	private void showHelpDialog() {
		AlertDialog helpDialog = new HelpDialog(this).buildDialog();
		if (!isFinishing()) helpDialog.show();
	}
	
}
