package com.ntredize.redstop.main.activity.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.db.model.PreloadData;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.service.PreloadService;
import com.ntredize.redstop.support.service.SharedPreferenceService;
import com.ntredize.redstop.support.service.ThemeService;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends ActivityBase {
	
	private SharedPreferenceService sharedPreferenceService;
	private ThemeService themeService;
	private PreloadService preloadService;

	// data
	private long start;
	
	
	/* Init */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Start Activity");
		
		initService();
		initTheme();
		initData();
		initView();

		startLoadData();
	}
	
	@Override
	protected void initService() {
		// service
		sharedPreferenceService = new SharedPreferenceService(this);
		themeService = new ThemeService(this);
		preloadService = new PreloadService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(false);
	}
	
	@Override
	protected void initData() {
		this.start = System.currentTimeMillis();
	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_splash_screen);
		
		// navigator bar color
		setNavigatorBarColorById((R.color.backgroundSplash));
		
		// screen image
		ImageView backgroundImage = findViewById(R.id.splash_screen_image);
		
		if (isLandscape()) setScreenImage(backgroundImage, R.drawable.screen_splash_landscape);
		else setScreenImage(backgroundImage, R.drawable.screen_splash_portrait);
	}


	/* Start Load Data */
	private void startLoadData() {
		new Thread(() -> {
			// Delete Cache File
			preloadService.deleteCacheFile();

			// Download Data from Server
			try {
				PreloadData preloadData = preloadService.getPreloadData();
				
				// success
				// 1. save preload data
				// 2. go to next page
				preloadService.insertPreloadData(preloadData);
				waitUntilGoToNextPage();
			} catch (ApplicationException ae) {
				// error
				// 1. show alert dialog
				// 2. close app
				this.runOnUiThread(() -> {
					Runnable closeAppRunnable = this::closeApp;
					errorHandling(ae, closeAppRunnable);
				});
			}
		}).start();
	}
	
	
	/* Start Activity */
	private void waitUntilGoToNextPage() {
		// time delay
		long diff = System.currentTimeMillis() - start;
		long timeCount = (diff > 500) ? 0 : 500 - diff;

		// go to next page
		Looper.prepare();

		new Handler().postDelayed(() -> {
			if (!sharedPreferenceService.isFinishTutorial()) goToTutorialPage();
			else goToHomePage();
			Objects.requireNonNull(Looper.myLooper()).quit();
		}, timeCount);

		Looper.loop();
	}

	private void goToTutorialPage() {
		Log.i(TAG, "Go To Tutorial Page");
		
		// open tutorial activity
		Intent i = new Intent(this, TutorialActivity.class);
		startActivity(i);
		finish();
	}
	
	private void goToHomePage() {
		Log.i(TAG, "Go To Home Page");
		
		// open home activity
		Intent i = new Intent(this, HomeActivity.class);
		startActivity(i);
		finish();
	}
	
}
