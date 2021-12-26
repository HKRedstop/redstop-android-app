package com.ntredize.redstop.main.dialog;

import android.content.DialogInterface;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.app.CheckDeviceActivity;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.appcompat.app.AlertDialog;

public class CheckDevicePrivacyDialog {
	
	// service
	private final ThemeService themeService;
	
	// data
	private final CheckDeviceActivity activity;
	
	
	/* Init */
	public CheckDevicePrivacyDialog(CheckDeviceActivity activity) {
		this.activity = activity;
		
		// service
		themeService = new ThemeService(activity);
	}
	
	public AlertDialog buildDialog() {
		// message
		String msg = activity.getString(R.string.msg_check_device_privacy);
		
		// button
		DialogInterface.OnClickListener continueListener = (dialog, which) -> activity.startCheckDevice();
		
		// build
		return new AlertDialog.Builder(activity, themeService.getDialogThemeId())
				.setMessage(msg)
				.setPositiveButton(R.string.label_confirm, continueListener)
				.setNegativeButton(R.string.label_cancel, null)
				.create();
	}
	
}
