package com.ntredize.redstop.support.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.ntredize.redstop.common.constants.RedCompanySorting;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceService {
	
	private static final String FINISH_TUTORIAL = "FinishTutorial";
	private static final String IS_DARK_MODE = "IsDarkMode";
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
	
	
	/* Dark Mode */
	public boolean isDarkMode() {
		return getSharedPreferences().getBoolean(IS_DARK_MODE, false);
	}
	
	public void updateDarkMode(boolean isDarkMode) {
		getSharedPreferences().edit().putBoolean(IS_DARK_MODE, isDarkMode).apply();
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
