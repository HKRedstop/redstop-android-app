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

public class SearchSortPopup extends PopupWindow implements View.OnClickListener {
	
	// style
	private final static int MARGIN_X = 40;
	private final static int MARGIN_Y = 20;
	
	// service
	private final AttrUtils attrUtils;
	
	// view
	private final LinearLayout nameContainer;
	private final ImageView nameImage;
	private final TextView nameText;
	private final LinearLayout categoryContainer;
	private final ImageView categoryImage;
	private final TextView categoryText;
	private final LinearLayout starContainer;
	private final ImageView starImage;
	private final TextView starText;
	
	// data
	private final ActivityBase activityBase;
	private String newSorting;
	
	
	/* Init */
	public SearchSortPopup(ActivityBase activityBase, String sorting) {
		super(activityBase);
		this.activityBase = activityBase;
		this.newSorting = sorting;
		
		// service
		attrUtils = new AttrUtils(activityBase);
		
		// click outside to close
		setOutsideTouchable(true);
		setFocusable(true);
		
		// view
		View view = activityBase.getLayoutInflater().inflate(R.layout.popup_search_sort, null);
		setContentView(view);
		setElevation(20);
		
		int backgroundNormalColor = attrUtils.getAttrColorInt(R.attr.backgroundNormalColor);
		setBackgroundDrawable(new ColorDrawable(backgroundNormalColor));
		
		nameContainer = view.findViewById(R.id.sort_popup_name_container);
		nameImage = view.findViewById(R.id.sort_popup_name_image);
		nameText = view.findViewById(R.id.sort_popup_name_text);
		categoryContainer = view.findViewById(R.id.sort_popup_category_container);
		categoryImage = view.findViewById(R.id.sort_popup_category_image);
		categoryText = view.findViewById(R.id.sort_popup_category_text);
		starContainer = view.findViewById(R.id.sort_popup_star_container);
		starImage = view.findViewById(R.id.sort_popup_star_image);
		starText = view.findViewById(R.id.sort_popup_star_text);
		
		if (RedCompanySorting.NAME.equals(sorting)) setActiveStyle(nameContainer, nameImage, nameText);
		else if (RedCompanySorting.CATEGORY.equals(sorting)) setActiveStyle(categoryContainer, categoryImage, categoryText);
		else if (RedCompanySorting.STAR.equals(sorting)) setActiveStyle(starContainer, starImage, starText);
		
		// listener
		nameContainer.setOnClickListener(this);
		categoryContainer.setOnClickListener(this);
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
			case R.id.sort_popup_name_container:
				setActiveStyle(nameContainer, nameImage, nameText);
				setInactiveStyle(categoryContainer, categoryImage, categoryText);
				setInactiveStyle(starContainer, starImage, starText);
				newSorting = RedCompanySorting.NAME;
				break;
			case R.id.sort_popup_category_container:
				setInactiveStyle(nameContainer, nameImage, nameText);
				setActiveStyle(categoryContainer, categoryImage, categoryText);
				setInactiveStyle(starContainer, starImage, starText);
				newSorting = RedCompanySorting.CATEGORY;
				break;
			case R.id.sort_popup_star_container:
				setInactiveStyle(nameContainer, nameImage, nameText);
				setInactiveStyle(categoryContainer, categoryImage, categoryText);
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
