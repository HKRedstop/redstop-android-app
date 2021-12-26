package com.ntredize.redstop.main.popup;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.constants.RedCompanySorting;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.utils.AttrUtils;

import androidx.core.content.ContextCompat;

public class RedCompanySubCategorySortPopup extends PopupWindow implements View.OnClickListener {
	
	// style
	private final static int MARGIN_X = 40;
	private final static int MARGIN_Y = 20;
	
	// service
	private final AttrUtils attrUtils;
	
	// view
	private final LinearLayout popularContainer;
	private final ImageView popularImage;
	private final TextView popularText;
	private final LinearLayout nameContainer;
	private final ImageView nameImage;
	private final TextView nameText;
	private final LinearLayout starContainer;
	private final ImageView starImage;
	private final TextView starText;
	
	// data
	private final ActivityBase activityBase;
	private String newSorting;
	
	
	/* Init */
	public RedCompanySubCategorySortPopup(ActivityBase activityBase, String sorting) {
		super(activityBase);
		this.activityBase = activityBase;
		this.newSorting = sorting;
		
		// service
		attrUtils = new AttrUtils(activityBase);
		
		// click outside to close
		setOutsideTouchable(true);
		setFocusable(true);
		
		// view
		View view = activityBase.getLayoutInflater().inflate(R.layout.popup_red_company_sub_category_sort, null);
		setContentView(view);
		setElevation(20);
		
		int backgroundNormalColor = attrUtils.getAttrColorInt(R.attr.backgroundNormalColor);
		setBackgroundDrawable(new ColorDrawable(backgroundNormalColor));
		
		popularContainer = view.findViewById(R.id.sort_popup_popular_container);
		popularImage = view.findViewById(R.id.sort_popup_popular_image);
		popularText = view.findViewById(R.id.sort_popup_popular_text);
		nameContainer = view.findViewById(R.id.sort_popup_name_container);
		nameImage = view.findViewById(R.id.sort_popup_name_image);
		nameText = view.findViewById(R.id.sort_popup_name_text);
		starContainer = view.findViewById(R.id.sort_popup_star_container);
		starImage = view.findViewById(R.id.sort_popup_star_image);
		starText = view.findViewById(R.id.sort_popup_star_text);
		
		if (RedCompanySorting.POPULAR.equals(sorting)) setActiveStyle(popularContainer, popularImage, popularText);
		else if (RedCompanySorting.NAME.equals(sorting)) setActiveStyle(nameContainer, nameImage, nameText);
		else if (RedCompanySorting.STAR.equals(sorting)) setActiveStyle(starContainer, starImage, starText);
		
		// listener
		popularContainer.setOnClickListener(this);
		nameContainer.setOnClickListener(this);
		starContainer.setOnClickListener(this);
	}
	
	
	/* Show */
	public void show(View parentView, int y) {
		showAtLocation(parentView, Gravity.TOP | Gravity.END, MARGIN_X, y - MARGIN_Y);
	}
	
	
	/* Style */
	private void setActiveStyle(LinearLayout container, ImageView imageView, TextView textView) {
		int primaryColor = attrUtils.getAttrColorInt(R.attr.primaryColor);
		int textColor = ContextCompat.getColor(activityBase, R.color.textOverColor);
		
		container.setBackground(new ColorDrawable(primaryColor));
		imageView.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
		textView.setTextColor(textColor);
	}
	
	private void setInactiveStyle(LinearLayout container, ImageView imageView, TextView textView) {
		int primaryColor = attrUtils.getAttrColorInt(R.attr.primaryColor);
		
		container.setBackground(ContextCompat.getDrawable(activityBase, R.drawable.background_sort_button_item));
		imageView.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
		textView.setTextColor(primaryColor);
	}
	
	
	/* Click */
	@SuppressLint("NonConstantResourceId")
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.sort_popup_popular_container:
				setActiveStyle(popularContainer, popularImage, popularText);
				setInactiveStyle(nameContainer, nameImage, nameText);
				setInactiveStyle(starContainer, starImage, starText);
				newSorting = RedCompanySorting.POPULAR;
				break;
			case R.id.sort_popup_name_container:
				setInactiveStyle(popularContainer, popularImage, popularText);
				setActiveStyle(nameContainer, nameImage, nameText);
				setInactiveStyle(starContainer, starImage, starText);
				newSorting = RedCompanySorting.NAME;
				break;
			case R.id.sort_popup_star_container:
				setInactiveStyle(popularContainer, popularImage, popularText);
				setInactiveStyle(nameContainer, nameImage, nameText);
				setActiveStyle(starContainer, starImage, starText);
				newSorting = RedCompanySorting.STAR;
				break;
			default:
		}
		
		// close popup after 50ms
		dismiss();
	}
	
	
	/* Data */
	public String getNewSorting() {
		return newSorting;
	}
	
}
