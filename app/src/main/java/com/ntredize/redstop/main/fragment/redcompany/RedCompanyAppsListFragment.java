package com.ntredize.redstop.main.fragment.redcompany;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.AndroidDeviceAppPackageInfo;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.recycler.MyFormListDivider;
import com.ntredize.redstop.main.recycler.adapter.RedCompanyAppAdapter;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RedCompanyAppsListFragment extends Fragment {
	
	public static final String KEY_ANDROID_DEVICE_APP_PACKAGE_INFOS = "KEY_ANDROID_DEVICE_APP_PACKAGE_INFOS";
	
	// activity
	private ActivityBase activity;
	
	// view
	private RedCompanyAppAdapter redCompanyAppAdapter;
	
	// data
	private List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos;
	
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
				// android packages
				androidDeviceAppPackageInfos = (List<AndroidDeviceAppPackageInfo>) args.getSerializable(KEY_ANDROID_DEVICE_APP_PACKAGE_INFOS);
			}
			
			// finish init
			hasInit = true;
		}
	}
	
	
	/* Layout */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_red_company_apps_list, container, false);

		// init data
		initData();
		
		// find view
		MyRecyclerView recyclerView = fragmentView.findViewById(R.id.red_company_apps_list_recycler_view);
		
		// recycler view
		redCompanyAppAdapter = new RedCompanyAppAdapter(activity, androidDeviceAppPackageInfos);
		MyFormListDivider myFormListDivider = new MyFormListDivider(activity, MyFormListDivider.VERTICAL);
		recyclerView.setAdapter(redCompanyAppAdapter);
		recyclerView.addItemDecoration(myFormListDivider);
		recyclerView.scrollToPosition(0);
		
		return fragmentView;
	}

}
