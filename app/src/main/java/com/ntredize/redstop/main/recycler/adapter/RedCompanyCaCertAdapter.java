package com.ntredize.redstop.main.recycler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.AndroidDeviceCaCert;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.recycler.viewholder.RedCompanyCaCertViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RedCompanyCaCertAdapter extends RecyclerView.Adapter<RedCompanyCaCertViewHolder> {
	
	// activity
	private final ActivityBase activity;
	
	// data
	private final List<AndroidDeviceCaCert> androidDeviceCaCerts;


	/* Init */
	public RedCompanyCaCertAdapter(ActivityBase activity, List<AndroidDeviceCaCert> androidDeviceCaCerts) {
		// activity
		this.activity = activity;

		// data
		this.androidDeviceCaCerts = androidDeviceCaCerts;
	}
	
	@NonNull
	@Override
	public RedCompanyCaCertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View contactView = LayoutInflater.from(activity).inflate(R.layout.row_red_company_ca_cert, parent, false);
		return new RedCompanyCaCertViewHolder(contactView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull RedCompanyCaCertViewHolder holder, int position) {
		// update position
		position = holder.getAdapterPosition();
		
		// get data from list
		AndroidDeviceCaCert androidDeviceCaCert = androidDeviceCaCerts.get(position);
		
		// organization
		holder.updateOrganization(androidDeviceCaCert.getOrganization());
		
		// display sub name
		holder.updateCommonName(androidDeviceCaCert.getCommonName());
	}


	/* Item Count */
	@Override
	public int getItemCount() {
		return androidDeviceCaCerts.size();
	}
	
}