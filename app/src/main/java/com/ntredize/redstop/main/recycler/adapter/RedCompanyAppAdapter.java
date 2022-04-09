package com.ntredize.redstop.main.recycler.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.AndroidDeviceAppPackageInfo;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.recycler.viewholder.RedCompanyAppViewHolder;
import com.ntredize.redstop.support.service.CheckDeviceService;
import com.ntredize.redstop.support.utils.AttrUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RedCompanyAppAdapter extends RecyclerView.Adapter<RedCompanyAppViewHolder> {
	
	// activity
	private final ActivityBase activity;
	private final CheckDeviceService checkDeviceService;
	
	// data
	private final int imageBorderColor;
	private final int imageBackgroundColor;
	private final List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos;


	/* Init */
	public RedCompanyAppAdapter(ActivityBase activity, List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos) {
		// activity
		this.activity = activity;

		// service
		AttrUtils attrUtils = new AttrUtils(activity);
		this.checkDeviceService = new CheckDeviceService(activity);

		// data
		this.imageBorderColor = attrUtils.getAttrColorInt(R.attr.imageBorderColor);
		this.imageBackgroundColor = attrUtils.getAttrColorInt(R.attr.imageBackgroundColor);
		this.androidDeviceAppPackageInfos = androidDeviceAppPackageInfos;
	}
	
	@NonNull
	@Override
	public RedCompanyAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View contactView = LayoutInflater.from(activity).inflate(R.layout.row_red_company_app, parent, false);
		return new RedCompanyAppViewHolder(contactView, imageBorderColor, imageBackgroundColor);
	}
	
	@Override
	public void onBindViewHolder(@NonNull RedCompanyAppViewHolder holder, int position) {
		// update position
		position = holder.getAdapterPosition();
		
		// get data from list
		AndroidDeviceAppPackageInfo androidDeviceAppPackageInfo = androidDeviceAppPackageInfos.get(position);
		
		// logo
		Drawable logo = checkDeviceService.getDeviceAppLogo(androidDeviceAppPackageInfo.getAndroidPackage());
		holder.updateLogoImage(logo);
		
		// name
		holder.updateName(androidDeviceAppPackageInfo.getName());
		
		// display sub name
		holder.updateAndroidPackage(androidDeviceAppPackageInfo.getAndroidPackage());
	}


	/* Item Count */
	@Override
	public int getItemCount() {
		return androidDeviceAppPackageInfos.size();
	}
	
}