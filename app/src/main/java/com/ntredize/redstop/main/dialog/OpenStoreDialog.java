package com.ntredize.redstop.main.dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.appcompat.app.AlertDialog;

public class OpenStoreDialog {
	
	// service
	private final ThemeService themeService;
	
	// data
	private final ActivityBase activityBase;
	private final String name;
	private final String url;
	
	
	/* Init */
	public OpenStoreDialog(Context context, String name, String url) {
		this.activityBase = (ActivityBase) context;
		this.name = name;
		this.url = url;
		
		// service
		themeService = new ThemeService(activityBase);
	}
	
	public AlertDialog buildDialog() {
		// message
		String msg = activityBase.getString(R.string.open_store_msg_prefix) + name + activityBase.getString(R.string.open_store_msg_suffix);
		
		// button
		DialogInterface.OnClickListener continueListener = (dialog, which) -> activityBase.openUrl(url);
		
		// build
		return new AlertDialog.Builder(activityBase, themeService.getDialogThemeId())
				.setMessage(msg)
				.setPositiveButton(R.string.label_continue, continueListener)
				.setNegativeButton(R.string.label_stop, null)
				.create();
	}
	
}
