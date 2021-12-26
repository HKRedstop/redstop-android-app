package com.ntredize.redstop.main.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.constants.RedStarType;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.view.container.NonAutoFocusScrollView;
import com.ntredize.redstop.support.service.ThemeService;
import com.ntredize.redstop.support.utils.AttrUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class HelpDialog implements View.OnClickListener {
	
	// service
	private final AttrUtils attrUtils;
	private final ThemeService themeService;
	
	// view
	private TextView starTypeButtonTextC;
	private NonAutoFocusScrollView contentLayoutC;
	
	private TextView starTypeButtonTextH;
	private NonAutoFocusScrollView contentLayoutH;
	
	private TextView starTypeButtonTextF;
	private NonAutoFocusScrollView contentLayoutF;
	
	private TextView starTypeButtonTextW;
	private NonAutoFocusScrollView contentLayoutW;
	
	// data
	private final ActivityBase activityBase;
	
	
	/* Init */
	public HelpDialog(@NonNull Context context) {
		this.activityBase = (ActivityBase) context;
		
		// service
		attrUtils = new AttrUtils(activityBase);
		themeService = new ThemeService(activityBase);
	}
	
	public AlertDialog buildDialog() {
		// view
		View view = activityBase.getLayoutInflater().inflate(R.layout.dialog_help, activityBase.findViewById(android.R.id.content), false);
		
		LinearLayout starTypeButtonC = view.findViewById(R.id.help_star_type_c_button);
		starTypeButtonTextC = view.findViewById(R.id.help_star_type_c_button_text);
		contentLayoutC = view.findViewById(R.id.help_content_c);
		
		LinearLayout starTypeButtonH = view.findViewById(R.id.help_star_type_h_button);
		starTypeButtonTextH = view.findViewById(R.id.help_star_type_h_button_text);
		contentLayoutH = view.findViewById(R.id.help_content_h);
		
		LinearLayout starTypeButtonF = view.findViewById(R.id.help_star_type_f_button);
		starTypeButtonTextF = view.findViewById(R.id.help_star_type_f_button_text);
		contentLayoutF = view.findViewById(R.id.help_content_f);
		
		LinearLayout starTypeButtonW = view.findViewById(R.id.help_star_type_w_button);
		starTypeButtonTextW = view.findViewById(R.id.help_star_type_w_button_text);
		contentLayoutW = view.findViewById(R.id.help_content_w);
		
		// action
		starTypeButtonC.setOnClickListener(this);
		starTypeButtonH.setOnClickListener(this);
		starTypeButtonF.setOnClickListener(this);
		starTypeButtonW.setOnClickListener(this);
		
		// init value
		changeHelpStarType(RedStarType.C);
		
		// build
		return new AlertDialog.Builder(activityBase, themeService.getDialogThemeId())
				.setView(view)
				.setNegativeButton(R.string.label_close, null)
				.create();
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.help_star_type_c_button:
				changeHelpStarType(RedStarType.C);
				break;
			case R.id.help_star_type_h_button:
				changeHelpStarType(RedStarType.H);
				break;
			case R.id.help_star_type_f_button:
				changeHelpStarType(RedStarType.F);
				break;
			case R.id.help_star_type_w_button:
				changeHelpStarType(RedStarType.W);
				break;
		}
	}
	
	private void changeHelpStarType(String starType) {
		int normalTextColor = attrUtils.getAttrColorInt(R.attr.textNormalColor);
		int primaryColor = attrUtils.getAttrColorInt(R.attr.primaryColor);
		
		// reset
		starTypeButtonTextC.setTextColor(normalTextColor);
		starTypeButtonTextH.setTextColor(normalTextColor);
		starTypeButtonTextF.setTextColor(normalTextColor);
		starTypeButtonTextW.setTextColor(normalTextColor);
		
		contentLayoutC.setVisibility(View.GONE);
		contentLayoutH.setVisibility(View.GONE);
		contentLayoutF.setVisibility(View.GONE);
		contentLayoutW.setVisibility(View.GONE);
		
		// set active
		switch (starType) {
			case RedStarType.C:
				starTypeButtonTextC.setTextColor(primaryColor);
				contentLayoutC.setVisibility(View.VISIBLE);
				break;
			case RedStarType.H:
				starTypeButtonTextH.setTextColor(primaryColor);
				contentLayoutH.setVisibility(View.VISIBLE);
				break;
			case RedStarType.F:
				starTypeButtonTextF.setTextColor(primaryColor);
				contentLayoutF.setVisibility(View.VISIBLE);
				break;
			case RedStarType.W:
				starTypeButtonTextW.setTextColor(primaryColor);
				contentLayoutW.setVisibility(View.VISIBLE);
				break;
		}
	}
	
}
