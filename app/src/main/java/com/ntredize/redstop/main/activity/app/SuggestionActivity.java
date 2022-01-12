package com.ntredize.redstop.main.activity.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.service.ThemeService;

import java.net.URLEncoder;
import java.util.Objects;

public class SuggestionActivity extends ActivityBase {
	
	public static final String KEY_RED_COMPANY_CODE = "KEY_RED_COMPANY_CODE";
	public static final String KEY_RED_COMPANY_DISPLAY_NAME = "KEY_RED_COMPANY_DISPLAY_NAME";
	
	// service
	private ThemeService themeService;
	
	// data
	private String googleFormUrl;
	
	
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
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(true);
	}
	
	@Override
	protected void initData() {
		String redCompanyCode = getIntent().getStringExtra(KEY_RED_COMPANY_CODE);
		String redCompanyDisplayName = getIntent().getStringExtra(KEY_RED_COMPANY_DISPLAY_NAME);
		
		googleFormUrl = getString(R.string.drawer_suggestion_url);
		if (redCompanyCode != null && redCompanyDisplayName != null) {
			try {
				String encodedName = URLEncoder.encode(redCompanyDisplayName + " // Reference: " + redCompanyCode, "UTF-8");
				googleFormUrl += "?entry.1120467779=現時資料有錯漏&entry.1250330913=" + encodedName;
			} catch (Exception ignored) {}
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initView() {
		setContentView(R.layout.activity_suggestion);
		
		// title
		setTitle(R.string.label_suggestion);
		
		// loading container
		RelativeLayout loadingContainer = findViewById(R.id.suggestion_loading_container);
		
		// web view
		WebView webView = findViewById(R.id.suggestion_web_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				try {
					if (url.startsWith("intent://")) {
						Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
						if (intent != null) {
							String fallbackUrl = intent.getStringExtra("browser_fallback_url");
							if (fallbackUrl != null) {
								webView.loadUrl(fallbackUrl);
								return true;
							} else {
								return false;
							}
						}
					}
				} catch (Exception e) {
					Log.e(TAG, Objects.requireNonNull(e.getMessage()));
				}
				return false;
			}
			
			@Override
			public void onPageCommitVisible(WebView view, String url) {
				super.onPageFinished(view, url);
				loadingContainer.setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
			}
		});
		webView.loadUrl(googleFormUrl);
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
	
}
