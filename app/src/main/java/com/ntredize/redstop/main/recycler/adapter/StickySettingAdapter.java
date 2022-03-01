package com.ntredize.redstop.main.recycler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.BuildConfig;
import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.app.SettingActivity;
import com.ntredize.redstop.main.recycler.StickyHeaderItemDecoration;
import com.ntredize.redstop.main.recycler.viewholder.SettingViewHolder;
import com.ntredize.redstop.support.service.SettingService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StickySettingAdapter extends RecyclerView.Adapter<SettingViewHolder> {
	
	// activity
	private final ActivityBase activity;
	
	// service
	private final SettingService settingService;
	
	// data
	private final List<SettingRowData> list;
	
	
	/* Init */
	public StickySettingAdapter(ActivityBase activity) {
		// activity
		this.activity = activity;
		
		// service
		this.settingService = new SettingService(activity);
		
		// data
		this.list = new ArrayList<>();
		this.list.add(new SettingRowData(R.string.setting_section_normal, R.string.setting_appearance));
		this.list.add(new SettingRowData(R.string.setting_section_support, R.string.setting_version));
		this.list.add(new SettingRowData(R.string.setting_section_support, R.string.setting_contact));
		this.list.add(new SettingRowData(R.string.setting_section_support, R.string.setting_privacy));
	}
	
	@NonNull
	@Override
	public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View contactView = LayoutInflater.from(activity).inflate(R.layout.row_setting, parent, false);
		return new SettingViewHolder(contactView, new SettingViewHolder.MyClickListener());
	}
	
	@Override
	public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
		// update position
		position = holder.getAdapterPosition();
		
		// get data from list
		final SettingRowData settingRowData = list.get(position);
		
		int labelId = settingRowData.getLabelId();
		String valueStr = null;
		if (labelId == R.string.setting_appearance) valueStr = settingService.getCurrentAppearanceStr();
		else if (labelId == R.string.setting_version) valueStr = activity.getString(R.string.app_version, BuildConfig.VERSION_NAME);
		
		// name and value
		holder.updateText(labelId, valueStr);
		
		// click action
		Runnable action = () -> {
			if (activity instanceof SettingActivity) {
				if (labelId == R.string.setting_appearance) ((SettingActivity) activity).openSettingAppearanceDialog();
				else if (labelId == R.string.setting_contact) ((SettingActivity) activity).contactUs();
				else if (labelId == R.string.setting_privacy) ((SettingActivity) activity).openPrivacy();
			}
		};
		holder.myClickListener.updateAction(action);
	}
	
	
	/* Item Count */
	@Override
	public int getItemCount() {
		return list.size();
	}
	
	
	/* Sticky Header */
	public StickyHeaderItemDecoration.SectionCallback getSectionCallback() {
		return new StickyHeaderItemDecoration.SectionCallback() {

			@Override
			public boolean showSectionHeader(int position) {
				return position < list.size();
			}
			
			@Override
			public boolean isSameSection(int position) {
				// first row
				if (position == 0) return true;

				// category or sub-category is different from previous row
				else if (position < list.size()) {
					if (!(list.get(position).getSectionId() == list.get(position - 1).getSectionId())) return true;
				}
				
				// other, not show header
				return false;
			}
			
			@Override
			public String getSectionHeader(int position) {
				// red company
				if (position < list.size()) return activity.getString(list.get(position).getSectionId());

				// other, not show header
				else return "";
			}
			
		};
	}
	
	
	/* Setting Row Data */
	static class SettingRowData {
		
		private final int sectionId;
		private final int labelId;
		
		public SettingRowData(int sectionId, int labelId) {
			this.sectionId = sectionId;
			this.labelId = labelId;
		}
		
		public int getSectionId() {
			return sectionId;
		}
		
		public int getLabelId() {
			return labelId;
		}
	}
	
}