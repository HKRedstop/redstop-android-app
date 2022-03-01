package com.ntredize.redstop.support.service;

import android.content.Context;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.constants.AppearanceType;

public class SettingService {
	
	// service
	private final SharedPreferenceService sharedPreferenceService;
	
	// data
	private final Context context;
	
	
	/* Init */
	public SettingService(Context context) {
		this.sharedPreferenceService = new SharedPreferenceService(context);
		this.context = context;
	}
	
	
	/* Appearance */
	public String getCurrentAppearanceStr() {
		String appearance = sharedPreferenceService.getAppearance();
		return getAppearanceStrByType(appearance);
	}
	
	public String getAppearanceStrByType(String appearance) {
		Integer strId = null;
		if (appearance.equals(AppearanceType.LIGHT)) strId = R.string.setting_appearance_light;
		else if (appearance.equals(AppearanceType.DARK)) strId = R.string.setting_appearance_dark;
		else if (appearance.equals(AppearanceType.SYSTEM)) strId = R.string.setting_appearance_system;
		
		if (strId != null) return context.getString(strId);
		else return "";
	}
	
}
