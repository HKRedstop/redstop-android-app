package com.ntredize.redstop.main.recycler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanyCategory;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.redcompany.RedCompanyCategoryListActivity;
import com.ntredize.redstop.main.recycler.viewholder.RedCompanyCategoryViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RedCompanyCategoryAdapter extends RecyclerView.Adapter<RedCompanyCategoryViewHolder> {
	
	// activity
	private final ActivityBase activity;
	
	// data
	private final List<RedCompanyCategory> list;
	
	
	/* Init */
	public RedCompanyCategoryAdapter(ActivityBase activity, List<RedCompanyCategory> list) {
		// activity
		this.activity = activity;
		
		// data
		this.list = list;
	}
	
	@NonNull
	@Override
	public RedCompanyCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View contactView = LayoutInflater.from(activity).inflate(R.layout.row_red_company_category, parent, false);
		return new RedCompanyCategoryViewHolder(contactView, new RedCompanyCategoryViewHolder.MyClickListener());
	}
	
	@Override
	public void onBindViewHolder(@NonNull RedCompanyCategoryViewHolder holder, int position) {
		// update position
		position = holder.getAdapterPosition();
		
		// get data from list
		final RedCompanyCategory category = list.get(position);
		
		// category name
		holder.updateCategoryName(category.getName());

		// click action
		Runnable action = () -> {
			if (activity instanceof RedCompanyCategoryListActivity) {
				((RedCompanyCategoryListActivity) activity).openRedCompanySubCategoryList(category);
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