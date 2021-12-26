package com.ntredize.redstop.main.adapter.redcompany;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.app.CheckDeviceActivity;
import com.ntredize.redstop.main.activity.app.CheckWebsiteActivity;
import com.ntredize.redstop.main.activity.redcompany.RedCompanyListActivity;
import com.ntredize.redstop.main.adapter.viewholder.LoadingViewHolder;
import com.ntredize.redstop.main.adapter.viewholder.RedCompanyViewHolder;
import com.ntredize.redstop.support.service.DownloadImageService;
import com.ntredize.redstop.support.service.RedCompanyService;
import com.ntredize.redstop.support.utils.AttrUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RedCompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int ITEM_VIEW_TYPE_RED_COMPANY = 1;
	private static final int ITEM_VIEW_TYPE_LOADING = 2;
	
	// activity
	private final ActivityBase activity;
	private final Fragment fragment;
	
	// service
	private final RedCompanyService redCompanyService;
	private final DownloadImageService downloadImageService;
	
	// data
	private final int imageBorderColor;
	private final int imageBackgroundColor;
	private final List<RedCompanySimple> redCompanyList;
	private final List<String> displayedCompanyCodeList;

	// load more data
	private final int loadMoreBuffer;
	private boolean hasMore;
	private boolean isLoading;
	private Integer lastDisplayRow;
	private int totalCount;
	private int displayCount;


	/* Init */
	public RedCompanyAdapter(ActivityBase activity, Fragment fragment,
	                         List<RedCompanySimple> redCompanyList, int numPerPage, boolean hasMore) {
		// activity
		this.activity = activity;
		this.fragment = fragment;

		// service
		AttrUtils attrUtils = new AttrUtils(activity);
		this.redCompanyService = new RedCompanyService(activity);
		this.downloadImageService = new DownloadImageService(activity);

		// data
		this.imageBorderColor = attrUtils.getAttrColorInt(R.attr.imageBorderColor);
		this.imageBackgroundColor = attrUtils.getAttrColorInt(R.attr.imageBackgroundColor);
		this.redCompanyList = redCompanyList;
		this.displayedCompanyCodeList = new ArrayList<>();

		// load more data
		this.loadMoreBuffer = numPerPage / 2;
		this.hasMore = hasMore;
		this.isLoading = false;
		this.lastDisplayRow = null;
		this.totalCount = redCompanyList.size();
		this.displayCount = 0;
	}

	public void updateData(List<RedCompanySimple> redCompanyList, boolean hasMore) {
		// data
		this.redCompanyList.addAll(redCompanyList);

		// load more data
		this.totalCount += redCompanyList.size();
		this.hasMore = hasMore;
		this.isLoading = false;
	}

	@Override
	public int getItemViewType(int position) {
		if (position < redCompanyList.size()) return ITEM_VIEW_TYPE_RED_COMPANY;
		else return ITEM_VIEW_TYPE_LOADING;
	}
	
	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// red company
		if (viewType == ITEM_VIEW_TYPE_RED_COMPANY) {
			View contactView = LayoutInflater.from(activity).inflate(R.layout.row_red_company, parent, false);
			return new RedCompanyViewHolder(contactView, imageBorderColor, imageBackgroundColor, new RedCompanyViewHolder.MyClickListener());
		}

		// loading
		else {
			View contactView = LayoutInflater.from(activity).inflate(R.layout.row_loading, parent, false);
			return new LoadingViewHolder(contactView);
		}
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		// red company
		if (holder.getItemViewType() == ITEM_VIEW_TYPE_RED_COMPANY) bindRedCompanyViewHolder((RedCompanyViewHolder) holder);

		// loading (no need to bind)
	}

	private void bindRedCompanyViewHolder(RedCompanyViewHolder holder) {
		// update position
		int position = holder.getAdapterPosition();
		
		// get data from list
		RedCompanySimple redCompany = redCompanyList.get(position);
		holder.setRowCompanyCode(redCompany.getCompanyCode());
		
		// logo
		holder.updateLogoImage(activity, downloadImageService, displayedCompanyCodeList, redCompany.getGroupCode(), redCompany.getCompanyCode());
		
		// display name
		holder.updateDisplayName(redCompany.getDisplayName());
		
		// display sub name
		holder.updateDisplaySubName(redCompany.getDisplaySubName());
		
		// red star
		holder.updateRedStar(redCompanyService, redCompany.getRedStar());

		// find last display count
		if (lastDisplayRow == null || position > lastDisplayRow) {
			lastDisplayRow = position;
			displayCount += 1;
		}

		// trigger load more event
		if (hasMore && !isLoading) {
			if (displayCount > totalCount - loadMoreBuffer) {
				isLoading = true;

				// red company list activity
				if (activity instanceof RedCompanyListActivity) {
					((RedCompanyListActivity) activity).loadDataFromServer(fragment);
				}
			}
		}

		// remember displayed company code
		displayedCompanyCodeList.add(redCompany.getCompanyCode());

		// click action
		Runnable action = () -> {
			// red company list page - open red company detail page
			if (activity instanceof RedCompanyListActivity) {
				((RedCompanyListActivity) activity).openRedCompanyDetail(redCompany);
			}
			
			// check device page - open red company detail page
			else if (activity instanceof CheckDeviceActivity) {
				((CheckDeviceActivity) activity).openRedCompanyDetail(redCompany);
			}
			
			// check website page - open red company detail page
			else if (activity instanceof CheckWebsiteActivity) {
				((CheckWebsiteActivity) activity).openRedCompanyDetail(redCompany);
			}
		};
		holder.myClickListener.updateAction(action);
	}


	/* Item Count */
	@Override
	public int getItemCount() {
		return redCompanyList.size() + (hasMore ? 1 : 0);
	}
	
}