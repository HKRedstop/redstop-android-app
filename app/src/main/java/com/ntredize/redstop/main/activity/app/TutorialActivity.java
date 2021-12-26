package com.ntredize.redstop.main.activity.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.fragment.DummyFragment;
import com.ntredize.redstop.main.fragment.tutorial.TutorialFragment;
import com.ntredize.redstop.main.view.viewpager.MyViewPager;
import com.ntredize.redstop.support.service.SharedPreferenceService;
import com.ntredize.redstop.support.service.ThemeService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TutorialActivity extends ActivityBase {
	
	// service
	private SharedPreferenceService sharedPreferenceService;
	private ThemeService themeService;
	
	// view
	private MyPagerAdapter myPagerAdapter;
	
	
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
		themeService.setupTheme(false);
	}
	
	@Override
	protected void initData() {
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_tutorial);
		
		// navigator bar color
		setNavigatorBarColorById(R.color.backgroundSplash);
		
		// fragments
		// 1. introduction
		// 2. red star
		// 3. donate
		List<TutorialFragment> fragmentList = new ArrayList<>();
		fragmentList.add(buildTutorialFragment(R.color.tutorialIntro, R.drawable.tutorial_image_intro, R.string.content_desc_tutorial_introduction,
				R.string.tutorial_introduction, false));
		fragmentList.add(buildTutorialFragment(R.color.tutorialStar, R.drawable.image_red_star_3, R.string.content_desc_tutorial_red_star,
				R.string.tutorial_red_star, false));
		fragmentList.add(buildTutorialFragment(R.color.tutorialCheckDevice, R.drawable.tutorial_image_check_device, R.string.content_desc_tutorial_check_device,
				R.string.tutorial_check_device, true));
		
		// view pager
		MyViewPager viewPager = findViewById(R.id.tutorial_view_pager);
		myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(myPagerAdapter);
	}
	
	private TutorialFragment buildTutorialFragment(int colorId, int imageDrawableId, int imageContentDescriptionId,
	                                               int textId, boolean showStartButton) {
		TutorialFragment fragment = new TutorialFragment();
		
		Bundle args = new Bundle();
		args.putInt(TutorialFragment.KEY_COLOR_ID, colorId);
		args.putInt(TutorialFragment.KEY_IMAGE_DRAWABLE_ID, imageDrawableId);
		args.putInt(TutorialFragment.KEY_IMAGE_CONTENT_DESC_ID, imageContentDescriptionId);
		args.putBoolean(TutorialFragment.KEY_TINT_IMAGE, true);
		args.putInt(TutorialFragment.KEY_TEXT_ID, textId);
		args.putBoolean(TutorialFragment.KEY_SHOW_START_BUTTON, showStartButton);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	/* Menu */
	@Override
	public void onBackPressed() {
		finish();
	}
	
	
	/* When Rotate, Re-Init View */
	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		// re-init view of sticky list when rotate
		if (myPagerAdapter != null) myPagerAdapter.refreshFragments();
	}
	
	
	/* Action: Start Application */
	public void startApplication() {
		Log.i(TAG, "Go To Home Page");
		
		// Save Finish Tutorial
		sharedPreferenceService.markFinishTutorial();
		
		// Open Home Activity
		Intent i = new Intent(this, HomeActivity.class);
		startActivity(i);
		finish();
	}
	
	
	
	/* Page Adapter */
	private static class MyPagerAdapter extends FragmentStatePagerAdapter {
		
		private final List<TutorialFragment> fragmentList;
		
		MyPagerAdapter(FragmentManager fm, List<TutorialFragment> fragmentList) {
			super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
			this.fragmentList = fragmentList;
		}
		
		@NonNull
		@Override
		public Fragment getItem(int position) {
			if (position < fragmentList.size()) return fragmentList.get(position);
			else return new DummyFragment();
		}
		
		@Override
		public int getCount() {
			// total number of pages
			return fragmentList.size();
		}
		
		@Override
		public int getItemPosition(@NonNull Object object) {
			// POSITION_NONE makes it possible to reload the PagerAdapter
			return POSITION_NONE;
		}
		
		// update fragment
		private void refreshFragments() {
			this.notifyDataSetChanged();
		}
	}
	
}
