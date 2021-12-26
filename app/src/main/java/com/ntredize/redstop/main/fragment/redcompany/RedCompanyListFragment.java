package com.ntredize.redstop.main.fragment.redcompany;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.adapter.MyFormListDivider;
import com.ntredize.redstop.main.adapter.redcompany.RedCompanyAdapter;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RedCompanyListFragment extends Fragment {
	
	public static final String KEY_RED_COMPANY_SEARCH_RESULT = "KEY_RED_COMPANY_SEARCH_RESULT";
	
	// activity
	private ActivityBase activity;
	
	// view
	private RedCompanyAdapter redCompanyAdapter;
	
	// data
	private SearchResult<RedCompanySimple> searchResult;
	
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
				// red company search result
				searchResult = (SearchResult<RedCompanySimple>) args.getSerializable(KEY_RED_COMPANY_SEARCH_RESULT);
			}
			
			// finish init
			hasInit = true;
		}
	}
	
	
	/* Layout */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_red_company_list, container, false);

		// init data
		initData();
		
		// find view
		MyRecyclerView recyclerView = fragmentView.findViewById(R.id.red_company_list_recycler_view);
		
		// recycler view
		redCompanyAdapter = new RedCompanyAdapter(activity, this, searchResult.getResults(),
				searchResult.getNumPerPage(), searchResult.getPage() < searchResult.getTotalPage());
		MyFormListDivider myFormListDivider = new MyFormListDivider(activity, MyFormListDivider.VERTICAL);
		recyclerView.setAdapter(redCompanyAdapter);
		recyclerView.addItemDecoration(myFormListDivider);
		recyclerView.scrollToPosition(0);
		
		return fragmentView;
	}


	/* Load Data */
	@SuppressLint("NotifyDataSetChanged")
	public void handleLoadMoreData(SearchResult<RedCompanySimple> searchResult) {
		if (searchResult != null) {
			redCompanyAdapter.updateData(searchResult.getResults(), searchResult.getPage() < searchResult.getTotalPage());
			activity.runOnUiThread(() -> redCompanyAdapter.notifyDataSetChanged());
		}
	}

}
