package com.ntredize.redstop.main.adapter.redcompany;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanySubCategory;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.redcompany.RedCompanySubCategoryListActivity;
import com.ntredize.redstop.main.adapter.viewholder.RedCompanySubCategoryViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RedCompanySubCategoryAdapter extends RecyclerView.Adapter<RedCompanySubCategoryViewHolder> {
	
	// activity
	private final ActivityBase activity;
	
	// data
	private final List<RedCompanySubCategory> list;
	
	
	/* Init */
	public RedCompanySubCategoryAdapter(ActivityBase activity, List<RedCompanySubCategory> list) {
		// activity
		this.activity = activity;
		
		// data
		this.list = list;
	}
	
	@NonNull
	@Override
	public RedCompanySubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View contactView = LayoutInflater.from(activity).inflate(R.layout.row_red_company_sub_category, parent, false);
		return new RedCompanySubCategoryViewHolder(contactView, new RedCompanySubCategoryViewHolder.MyClickListener());
	}
	
	@Override
	public void onBindViewHolder(@NonNull RedCompanySubCategoryViewHolder holder, int position) {
		// update position
		position = holder.getAdapterPosition();
		
		// get data from list
		final RedCompanySubCategory subCategory = list.get(position);
		
		// category name
		holder.updateSubCategoryName(subCategory.getName());

		// click action
		Runnable action = () -> {
			if (activity instanceof RedCompanySubCategoryListActivity) {
				((RedCompanySubCategoryListActivity) activity).openRedCompanyList(subCategory);
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