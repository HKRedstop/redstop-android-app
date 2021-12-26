package com.ntredize.redstop.main.adapter.redcompany;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanySimpleWithCategory;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.activity.app.SearchResultActivity;
import com.ntredize.redstop.main.activity.redcompany.RedCompanyDetailActivity;
import com.ntredize.redstop.main.adapter.StickyHeaderItemDecoration;
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

public class StickyRedCompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
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
	private final List<RedCompanySimpleWithCategory> redCompanyList;
	private final List<String> displayedCompanyCodeList;

	// load more data
	private final int loadMoreBuffer;
	private boolean hasMore;
	private boolean isLoading;
	private Integer lastDisplayRow;
	private int totalCount;
	private int displayCount;
	
	
	/* Init */
	public StickyRedCompanyAdapter(ActivityBase activity, Fragment fragment,
	                               List<RedCompanySimpleWithCategory> redCompanyList, int numPerPage, boolean hasMore) {
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

	public void updateData(List<RedCompanySimpleWithCategory> redCompanyList, boolean hasMore) {
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
		final RedCompanySimpleWithCategory redCompany = redCompanyList.get(position);
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

				// search activity
				if (activity instanceof SearchResultActivity) {
					((SearchResultActivity) activity).loadDataFromServer(fragment);
				}

				// red company detail activity
                if (activity instanceof RedCompanyDetailActivity) {
                    ((RedCompanyDetailActivity) activity).loadRelatedRedCompanyFromServer(fragment);
                }
			}
		}

		// remember displayed company code
		displayedCompanyCodeList.add(redCompany.getCompanyCode());

		// click event
		Runnable action = () -> {
			// search page - open red company detail page
			if (activity instanceof SearchResultActivity) {
				((SearchResultActivity) activity).openRedCompanyDetail(redCompany);
			}

			// red company detail page - open related red company detail page
			else if (activity instanceof RedCompanyDetailActivity) {
				((RedCompanyDetailActivity) activity).openRelatedRedCompanyDetail(redCompany);
			}
		};
		holder.myClickListener.updateAction(action);
	}
	
	
	/* Item Count */
	@Override
	public int getItemCount() {
		return redCompanyList.size() + (hasMore ? 1 : 0);
	}
	
	
	/* Sticky Header */
	public StickyHeaderItemDecoration.SectionCallback getSectionCallback() {
		return new StickyHeaderItemDecoration.SectionCallback() {

			@Override
			public boolean showSectionHeader(int position) {
				return position < redCompanyList.size();
			}
			
			@Override
			public boolean isSameSection(int position) {
				// first row
				if (position == 0) return true;

				// category or sub-category is different from previous row
				else if (position < redCompanyList.size()) {
					if (!redCompanyList.get(position).getCategoryCode().equals(redCompanyList.get(position - 1).getCategoryCode())) return true;
					else if (!redCompanyList.get(position).getSubCategoryCode().equals(redCompanyList.get(position - 1).getSubCategoryCode())) return true;
				}
				
				// other, not show header
				return false;
			}
			
			@Override
			public String getSectionHeader(int position) {
				// red company
				if (position < redCompanyList.size()) return redCompanyList.get(position).getCategoryName() + "  (" + redCompanyList.get(position).getSubCategoryName() + ")";

				// other, not show header
				else return "";
			}
			
		};
	}
	
}