package com.ntredize.redstop.main.fragment.redcompany;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.AndroidDeviceCaCert;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.recycler.MyFormListDivider;
import com.ntredize.redstop.main.recycler.adapter.RedCompanyCaCertAdapter;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RedCompanyCaCertsListFragment extends Fragment {
	
	public static final String KEY_ANDROID_DEVICE_CA_CERTS = "KEY_ANDROID_DEVICE_CA_CERTS";
	
	// activity
	private ActivityBase activity;
	
	// view
	private RedCompanyCaCertAdapter redCompanyCaCertAdapter;
	
	// data
	private List<AndroidDeviceCaCert> androidDeviceCaCerts;
	
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
				// ca cert
				androidDeviceCaCerts = (List<AndroidDeviceCaCert>) args.getSerializable(KEY_ANDROID_DEVICE_CA_CERTS);
			}
			
			// finish init
			hasInit = true;
		}
	}
	
	
	/* Layout */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_red_company_ca_certs_list, container, false);

		// init data
		initData();
		
		// find view
		MyRecyclerView recyclerView = fragmentView.findViewById(R.id.red_company_ca_certs_list_recycler_view);
		
		// recycler view
		redCompanyCaCertAdapter = new RedCompanyCaCertAdapter(activity, androidDeviceCaCerts);
		MyFormListDivider myFormListDivider = new MyFormListDivider(activity, MyFormListDivider.VERTICAL);
		recyclerView.setAdapter(redCompanyCaCertAdapter);
		recyclerView.addItemDecoration(myFormListDivider);
		recyclerView.scrollToPosition(0);
		
		return fragmentView;
	}

}
