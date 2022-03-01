package com.ntredize.redstop.main.dialog;

import android.content.Context;
import android.view.View;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.recycler.MyFormListDivider;
import com.ntredize.redstop.main.recycler.adapter.SettingAppearanceAdapter;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;
import com.ntredize.redstop.support.service.ThemeService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class SettingAppearanceDialog {
	
	// service
	private final ThemeService themeService;
	
	// data
	private final ActivityBase activityBase;
	
	
	/* Init */
	public SettingAppearanceDialog(@NonNull Context context) {
		this.activityBase = (ActivityBase) context;
		
		// service
		themeService = new ThemeService(activityBase);
	}
	
	public AlertDialog buildDialog() {
		// view
		View view = activityBase.getLayoutInflater().inflate(R.layout.dialog_setting_item, activityBase.findViewById(android.R.id.content), false);
		
		// recycler view
		MyRecyclerView recyclerView = view.findViewById(R.id.dialog_setting_item_recycler_view);
		recyclerView.setAdapter(new SettingAppearanceAdapter(activityBase));
		
		MyFormListDivider myFormListDivider = new MyFormListDivider(activityBase, MyFormListDivider.VERTICAL);
		recyclerView.addItemDecoration(myFormListDivider);
		
		// build
		return new AlertDialog.Builder(activityBase, themeService.getDialogThemeId())
				.setView(view)
				.create();
	}
	
}
