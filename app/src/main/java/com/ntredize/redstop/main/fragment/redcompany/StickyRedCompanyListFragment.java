package com.ntredize.redstop.main.fragment.redcompany;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanySimpleWithCategory;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.adapter.MyFormListDivider;
import com.ntredize.redstop.main.adapter.StickyHeaderItemDecoration;
import com.ntredize.redstop.main.adapter.redcompany.StickyRedCompanyAdapter;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class StickyRedCompanyListFragment extends Fragment {
	
	public static final String KEY_SHOW_STICKY_HEADER = "KEY_SHOW_STICKY_HEADER";
	public static final String KEY_RED_COMPANY_SEARCH_RESULT = "KEY_RED_COMPANY_SEARCH_RESULT";
	
	// activity
	private ActivityBase activity;
	
	// view
	private RelativeLayout dummyHeaderContainer;
	private MyRecyclerView recyclerView;
	private StickyRedCompanyAdapter stickyRedCompanyAdapter;
	
	// data
	private boolean showStickyHeader;
	private SearchResult<RedCompanySimpleWithCategory> searchResult;
	
	// init flag
	private boolean hasInit;
	
	
	/* Init */
	public void initData() {
		if (!hasInit) {
			// from arguments
			Bundle args = getArguments();
			
			// activity
			activity = (ActivityBase) getActivity();
			
			// data
			if (args != null) {
				// show sticky header
				showStickyHeader = args.getBoolean(KEY_SHOW_STICKY_HEADER);
				
				// red company search result
				searchResult = (SearchResult<RedCompanySimpleWithCategory>) args.getSerializable(KEY_RED_COMPANY_SEARCH_RESULT);
			}
			
			// finish init
			hasInit = true;
		}
	}
	
	
	/* Layout */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_sticky_red_company_list, container, false);

		// init data
		initData();
		
		// find view
		dummyHeaderContainer = fragmentView.findViewById(R.id.dummy_sticky_red_company_list_header_container);
		recyclerView = fragmentView.findViewById(R.id.sticky_red_company_list_recycler_view);
		
		// recycler view
		stickyRedCompanyAdapter = new StickyRedCompanyAdapter(activity, this, searchResult.getResults(),
				searchResult.getNumPerPage(), searchResult.getPage() < searchResult.getTotalPage());
		recyclerView.setAdapter(stickyRedCompanyAdapter);
		recyclerView.setVisibility(View.INVISIBLE);
		
		// special handle for view layout
		initLayoutForSpecifiedView(fragmentView);
		
		return fragmentView;
	}
	
	private void initLayoutForSpecifiedView(final View fragmentView) {
		fragmentView.post(() -> {
			// measure dummy header height
			int headerHeight = dummyHeaderContainer.getMeasuredHeight();
			dummyHeaderContainer.setVisibility(View.GONE);
			
			// header and divider for recycler view
			if (showStickyHeader) {
				StickyHeaderItemDecoration headerDecoration = new StickyHeaderItemDecoration(headerHeight, stickyRedCompanyAdapter.getSectionCallback());
				recyclerView.addItemDecoration(headerDecoration);
			}
			MyFormListDivider myFormListDivider = new MyFormListDivider(activity, MyFormListDivider.VERTICAL);
			recyclerView.addItemDecoration(myFormListDivider);
			recyclerView.scrollToPosition(0);
			recyclerView.setVisibility(View.VISIBLE);
		});
	}


	/* Load Data */
	@SuppressLint("NotifyDataSetChanged")
	public void handleLoadMoreData(SearchResult<RedCompanySimpleWithCategory> searchResult) {
		if (searchResult != null) {
			stickyRedCompanyAdapter.updateData(searchResult.getResults(), searchResult.getPage() < searchResult.getTotalPage());
			activity.runOnUiThread(() -> stickyRedCompanyAdapter.notifyDataSetChanged());
		}
	}

}
