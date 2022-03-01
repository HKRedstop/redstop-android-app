package com.ntredize.redstop.support.service;

import android.os.Build;
import android.view.View;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.utils.AttrUtils;

public class ThemeService {
	
	private final ActivityBase activityBase;
	private final AttrUtils attrUtils;
	private final SharedPreferenceService sharedPreferenceService;
	
	
	/* Init */
	public ThemeService(ActivityBase activityBase) {
		this.activityBase = activityBase;
		attrUtils = new AttrUtils(activityBase);
		sharedPreferenceService = new SharedPreferenceService(activityBase);
	}
	
	
	/* Theme */
	public void setupTheme(boolean hasActionBar) {
		// theme
		boolean isDarkMode = sharedPreferenceService.isDarkMode();
		int themeId;
		
		if (isDarkMode) {
			if (hasActionBar) themeId = R.style.RedstopDarkTheme;
			else themeId = R.style.RedstopDarkTheme_NoActionBar;
		}
		else {
			if (hasActionBar) themeId = R.style.RedstopLightTheme;
			else themeId = R.style.RedstopLightTheme_NoActionBar;
		}
		
		activityBase.setTheme(themeId);
		
		// navigation bar
		// - do not set it in style.xml, since it cannot handle android version < 28
		if (Build.VERSION.SDK_INT >= 28) {
			if (!isDarkMode) {
				int flags = activityBase.getWindow().getDecorView().getSystemUiVisibility();
				activityBase.getWindow().getDecorView().setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
			}
			
			int navigationBackgroundColor = attrUtils.getAttrColorInt(R.attr.navigationBackgroundColor);
			activityBase.getWindow().setNavigationBarColor(navigationBackgroundColor);
			
			int navigationSeparatorColor = attrUtils.getAttrColorInt(R.attr.navigationSeparatorColor);
			activityBase.getWindow().setNavigationBarDividerColor(navigationSeparatorColor);
		}
	}
	
	public int getDialogThemeId() {
		boolean isDarkMode = sharedPreferenceService.isDarkMode();
		return isDarkMode ? R.style.RedstopDarkTheme_Dialog : R.style.RedstopLightTheme_Dialog;
	}
	
}
