package com.ntredize.redstop.main.dialog;

import android.content.Context;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class ScanBarcodeResultDialog {
	
	// service
	private final ThemeService themeService;
	
	// data
	private final ActivityBase activityBase;
	private final String country;
	
	
	/* Init */
	public ScanBarcodeResultDialog(@NonNull Context context, String country) {
		this.activityBase = (ActivityBase) context;
		this.country = country;
		
		// service
		themeService = new ThemeService(activityBase);
	}
	
	public AlertDialog buildDialog() {
		// title
		String title = activityBase.getString(R.string.label_scan_barcode_country) + country;
		
		// build
		return new AlertDialog.Builder(activityBase, themeService.getDialogThemeId())
				.setTitle(title)
				.setMessage(activityBase.getString(R.string.label_scan_barcode_remark))
				.setNegativeButton(R.string.label_close, null)
				.create();
	}
	
}
