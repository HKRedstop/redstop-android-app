package com.ntredize.redstop.main.fragment.checkdevice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.app.CheckDeviceActivity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class CheckDeviceFragment extends Fragment {
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_check_device, container, false);
		
		// activity
		CheckDeviceActivity activity = (CheckDeviceActivity) getActivity();
		assert activity != null;
		
		// image
		ImageView imageView = view.findViewById(R.id.check_device_image);
		imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.check_device_image));
		imageView.setContentDescription(activity.getString(R.string.content_desc_check_device_start));
		
		// text
		TextView tutorialText = view.findViewById(R.id.check_device_text);
		tutorialText.setText(R.string.label_check_device_start_remark);
		
		// start button
		RelativeLayout startButtonContainer = view.findViewById(R.id.check_device_start_button_container);
		Button startButton = view.findViewById(R.id.check_device_start_button);
		startButtonContainer.setVisibility(View.VISIBLE);
		startButton.setOnClickListener((View v) -> activity.showCheckDevicePrivacyDialog());
		
		return view;
	}

}
