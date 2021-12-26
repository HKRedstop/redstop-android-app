package com.ntredize.redstop.main.fragment.redcompany;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RedCompanyNoSearchResultFragment extends Fragment {
	
	/* Layout */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_red_company_no_search_result, container, false);
	}
	
}
