package com.ntredize.redstop.main.dialog;

import android.content.Context;
import android.view.View;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class CaCertHelpDialog {
	
	// service
	private final ThemeService themeService;
	
	// data
	private final ActivityBase activityBase;
	
	
	/* Init */
	public CaCertHelpDialog(@NonNull Context context) {
		this.activityBase = (ActivityBase) context;
		
		// service
		themeService = new ThemeService(activityBase);
	}
	
	public AlertDialog buildDialog() {
		// view
		View view = activityBase.getLayoutInflater().inflate(R.layout.dialog_ca_cert_help, activityBase.findViewById(android.R.id.content), false);
		
		// build
		return new AlertDialog.Builder(activityBase, themeService.getDialogThemeId())
				.setTitle(R.string.ca_cert_help_title)
				.setView(view)
				.setNegativeButton(R.string.label_close, null)
				.create();
	}
	
}
