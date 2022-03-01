package com.ntredize.redstop.support.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.ntredize.redstop.common.constants.AppearanceType;
import com.ntredize.redstop.common.constants.RedCompanySorting;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceService {
	
	private static final String FINISH_TUTORIAL = "FinishTutorial";
	private static final String IS_DARK_MODE = "IsDarkMode";
	private static final String APPEARANCE = "Appearance";
	private static final String SEARCH_SORTING = "SearchSorting";
	private static final String RED_COMPANY_SUB_CATEGORY_SORTING = "RedCompanySubCategorySorting";
	
	private final Context context;
	
	
	/* Init */
	public SharedPreferenceService(Context context) {
		this.context = context;
	}
	
	private SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
	}
	
	
	/* Finish Tutorial */
	public boolean isFinishTutorial() {
		return getSharedPreferences().getBoolean(FINISH_TUTORIAL, false);
	}
	
	public void markFinishTutorial() {
		getSharedPreferences().edit().putBoolean(FINISH_TUTORIAL, true).apply();
	}
	
	
	/* Appearance */
	public void initAppearance() {
		if ("".equals(getSharedPreferences().getString(APPEARANCE, ""))) {
			boolean darkMode = getSharedPreferences().getBoolean(IS_DARK_MODE, false);
			getSharedPreferences().edit().putString(APPEARANCE, darkMode ? AppearanceType.DARK : AppearanceType.LIGHT).remove(IS_DARK_MODE).apply();
		}
	}
	
	public String getAppearance() {
		return getSharedPreferences().getString(APPEARANCE, "");
	}
	
	public void setAppearance(String appearance) {
		getSharedPreferences().edit().putString(APPEARANCE, appearance).apply();
	}
	
	public boolean isDarkMode() {
		String appearance = getSharedPreferences().getString(APPEARANCE, AppearanceType.LIGHT);
		
		switch (appearance) {
			case AppearanceType.LIGHT:
				return false;
			case AppearanceType.DARK:
				return true;
			case AppearanceType.SYSTEM:
				int deviceNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
				if (deviceNightMode == Configuration.UI_MODE_NIGHT_YES) return true;
				break;
		}
		return false;
	}
	
	
	/* Sorting */
	public String getSearchSorting() {
		return getSharedPreferences().getString(SEARCH_SORTING, RedCompanySorting.NAME);
	}
	
	public void setSearchSorting(String sorting) {
		getSharedPreferences().edit().putString(SEARCH_SORTING, sorting).apply();
	}
	
	public String getRedCompanySubCategorySorting() {
		return getSharedPreferences().getString(RED_COMPANY_SUB_CATEGORY_SORTING, RedCompanySorting.POPULAR);
	}
	
	public void setRedCompanySubCategorySorting(String sorting) {
		getSharedPreferences().edit().putString(RED_COMPANY_SUB_CATEGORY_SORTING, sorting).apply();
	}
	
}
