package com.ntredize.redstop.main.fragment.tutorial;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.app.TutorialActivity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class TutorialFragment extends Fragment {
	
	// key
	public final static String KEY_COLOR_ID = "KEY_COLOR_ID";
	public final static String KEY_IMAGE_DRAWABLE_ID = "KEY_IMAGE_DRAWABLE_ID";
	public final static String KEY_IMAGE_CONTENT_DESC_ID = "KEY_IMAGE_CONTENT_DESC_ID";
	public final static String KEY_TINT_IMAGE = "KEY_TINT_IMAGE";
	public final static String KEY_TEXT_ID = "KEY_TEXT_ID";
	public final static String KEY_SHOW_START_BUTTON = "KEY_SHOW_START_BUTTON";
	
	
	/* Layout */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
		
		// activity
		TutorialActivity activity = (TutorialActivity) getActivity();
		assert activity != null;
		
		// data
		assert getArguments() != null;
		int colorId = getArguments().getInt(KEY_COLOR_ID);
		int imageDrawableId = getArguments().getInt(KEY_IMAGE_DRAWABLE_ID);
		int imageContentDescriptionId = getArguments().getInt(KEY_IMAGE_CONTENT_DESC_ID);
		boolean tintImage = getArguments().getBoolean(KEY_TINT_IMAGE);
		int textId = getArguments().getInt(KEY_TEXT_ID);
		boolean showStartButton = getArguments().getBoolean(KEY_SHOW_START_BUTTON);
		
		// color
		int color = ContextCompat.getColor(activity, colorId);
		
		// container
		RelativeLayout tutorialContainer = view.findViewById(R.id.tutorial_container);
		tutorialContainer.setBackgroundColor(color);
		
		// image
		ImageView tutorialImage = view.findViewById(R.id.tutorial_image);
		tutorialImage.setImageDrawable(ContextCompat.getDrawable(activity, imageDrawableId));
		tutorialImage.setContentDescription(activity.getString(imageContentDescriptionId));
		if (tintImage) tutorialImage.setColorFilter(color, PorterDuff.Mode.SRC_IN);
		
		// text
		TextView tutorialText = view.findViewById(R.id.tutorial_text);
		tutorialText.setText(textId);
		
		// start button
		RelativeLayout startButtonContainer = view.findViewById(R.id.tutorial_start_button_container);
		Button startButton = view.findViewById(R.id.tutorial_start_button);
		if (showStartButton) {
			startButtonContainer.setVisibility(View.VISIBLE);
			startButton.setOnClickListener((View v) -> activity.startApplication());
		}
		else {
			startButtonContainer.setVisibility(View.INVISIBLE);
		}
		
		return view;
	}
	
}
