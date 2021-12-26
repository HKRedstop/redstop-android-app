package com.ntredize.redstop.main.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class WarningDialog {
	
	// service
	private final ThemeService themeService;
	
	// data
	private final ActivityBase activityBase;
	private final String msg;
	private final Runnable action;
	
	
	/* Init */
	public WarningDialog(@NonNull Context context, String msg, Runnable action) {
		this.activityBase = (ActivityBase) context;
		this.msg = msg;
		this.action = action;
		
		// service
		themeService = new ThemeService(activityBase);
	}
	
	public AlertDialog buildDialog() {
		// close button
		DialogInterface.OnClickListener closeListener = (dialog, which) -> {
			if (action != null) new Handler().post(action);
		};
		
		// build
		return new AlertDialog.Builder(activityBase, themeService.getDialogThemeId())
				.setMessage(msg)
				.setNegativeButton(R.string.label_close, closeListener)
				.create();
	}
	
}
