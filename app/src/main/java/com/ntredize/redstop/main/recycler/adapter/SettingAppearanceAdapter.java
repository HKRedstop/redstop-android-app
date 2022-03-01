package com.ntredize.redstop.main.recycler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.constants.AppearanceType;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.app.SettingActivity;
import com.ntredize.redstop.main.recycler.viewholder.SettingItemViewHolder;
import com.ntredize.redstop.support.service.SettingService;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingAppearanceAdapter extends RecyclerView.Adapter<SettingItemViewHolder> {
	
	// activity
	private final ActivityBase activity;
	
	// service
	private final SettingService settingService;
	
	// data
	private final List<String> list;
	
	
	/* Init */
	public SettingAppearanceAdapter(ActivityBase activity) {
		// activity
		this.activity = activity;
		
		// service
		this.settingService = new SettingService(activity);
		
		// data
		this.list = Arrays.asList(AppearanceType.LIGHT, AppearanceType.DARK, AppearanceType.SYSTEM);
	}
	
	@NonNull
	@Override
	public SettingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View contactView = LayoutInflater.from(activity).inflate(R.layout.row_setting_item, parent, false);
		return new SettingItemViewHolder(contactView, new SettingItemViewHolder.MyClickListener());
	}
	
	@Override
	public void onBindViewHolder(@NonNull SettingItemViewHolder holder, int position) {
		// update position
		position = holder.getAdapterPosition();
		
		// get data from list
		final String appearance = list.get(position);
		
		// text
		holder.updateText(settingService.getAppearanceStrByType(appearance));

		// click action
		Runnable action = () -> {
			if (activity instanceof SettingActivity) {
				((SettingActivity) activity).setAppearance(appearance);
			}
		};
		holder.myClickListener.updateAction(action);
	}
	
	
	/* Item Count */
	@Override
	public int getItemCount() {
		return list.size();
	}
	
}