package com.ntredize.redstop.main.fragment.checkwebsite;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.WebsiteSearchCriteria;
import com.ntredize.redstop.main.activity.app.CheckWebsiteActivity;
import com.ntredize.redstop.support.service.CheckWebsiteService;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class CheckWebsiteFragment extends Fragment {
	
	// key
	public static final String KEY_SEARCH_VALUE = "KEY_SEARCH_VALUE";
	
	// activity
	private CheckWebsiteActivity activity;
	
	// service
	private CheckWebsiteService checkWebsiteService;
	
	// view
	private EditText searchText;
	private ImageView websiteImage;
	private ImageView wikiImage;
	private ImageView facebookImage;
	private ImageView igImage;
	private ImageView twitterImage;
	private ImageView openriceImage;
	private ImageView iosImage;
	private ImageView androidImage;
	private ImageView steamImage;
	private ImageView psImage;
	private ImageView xboxImage;
	private ImageView switchImage;
	
	
	/* Layout */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_check_website, container, false);
		
		// activity
		activity = (CheckWebsiteActivity) getActivity();
		assert activity != null;
		
		// service
		checkWebsiteService = new CheckWebsiteService(activity);
		
		// data
		assert getArguments() != null;
		String searchValue = getArguments().getString(KEY_SEARCH_VALUE);
		
		// search text
		searchText = view.findViewById(R.id.check_website_search_text);
		searchText.setText(searchValue);
		searchText.setOnEditorActionListener((textView, i, keyEvent) -> {
			doSearch();
			return false;
		});
		searchText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			
			@Override
			public void afterTextChanged(Editable editable) {
				updateWebsiteIcon();
			}
		});
		
		// search button
		ImageButton searchButton = view.findViewById(R.id.check_website_search_button);
		searchButton.setOnClickListener(view1 -> doSearch());
		
		// website
		websiteImage = view.findViewById(R.id.check_website_website_image_website);
		checkWebsiteService.setWebsiteIcon(websiteImage, false);
		
		// wiki
		wikiImage = view.findViewById(R.id.check_website_website_image_wiki);
		checkWebsiteService.setWikiIcon(wikiImage, false);
		
		// facebook
		facebookImage = view.findViewById(R.id.check_website_website_image_facebook);
		checkWebsiteService.setFacebookIcon(facebookImage, false);
		
		// ig
		igImage = view.findViewById(R.id.check_website_website_image_ig);
		checkWebsiteService.setIgIcon(igImage, false);
		
		// twitter
		twitterImage = view.findViewById(R.id.check_website_website_image_twitter);
		checkWebsiteService.setTwitterIcon(twitterImage, false);
		
		// openrice
		openriceImage = view.findViewById(R.id.check_website_website_image_openrice);
		checkWebsiteService.setOpenriceIcon(openriceImage, false);
		
		// ios
		iosImage = view.findViewById(R.id.check_website_website_image_ios);
		checkWebsiteService.setIosIcon(iosImage, false);
		
		// android
		androidImage = view.findViewById(R.id.check_website_website_image_android);
		checkWebsiteService.setAndroidIcon(androidImage, false);
		
		// steam
		steamImage = view.findViewById(R.id.check_website_website_image_steam);
		checkWebsiteService.setSteamIcon(steamImage, false);
		
		// ps
		psImage = view.findViewById(R.id.check_website_website_image_ps);
		checkWebsiteService.setPsIcon(psImage, false);
		
		// xbox
		xboxImage = view.findViewById(R.id.check_website_website_image_xbox);
		checkWebsiteService.setXboxIcon(xboxImage, false);
		
		// switch
		switchImage = view.findViewById(R.id.check_website_website_image_switch);
		checkWebsiteService.setSwitchIcon(switchImage, false);
		
		return view;
	}
	
	private void updateWebsiteIcon() {
		WebsiteSearchCriteria criteria = checkWebsiteService.extractWebsite(searchText.getText().toString());
		checkWebsiteService.setWebsiteIcon(websiteImage, criteria.getWebsite() != null);
		checkWebsiteService.setWikiIcon(wikiImage, criteria.getWikiId() != null);
		checkWebsiteService.setFacebookIcon(facebookImage, criteria.getFacebookId() != null);
		checkWebsiteService.setIgIcon(igImage, criteria.getIgId() != null);
		checkWebsiteService.setTwitterIcon(twitterImage, criteria.getTwitterId() != null);
		checkWebsiteService.setOpenriceIcon(openriceImage, criteria.getOpenriceId() != null);
		checkWebsiteService.setIosIcon(iosImage, criteria.getIosStoreId() != null || criteria.getIosAppId() != null);
		checkWebsiteService.setAndroidIcon(androidImage, criteria.getAndroidStoreId() != null || criteria.getAndroidPackage() != null);
		checkWebsiteService.setSteamIcon(steamImage, criteria.getSteamStoreId() != null || criteria.getSteamAppId() != null);
		checkWebsiteService.setPsIcon(psImage, criteria.getPsAppId() != null);
		checkWebsiteService.setXboxIcon(xboxImage, criteria.getXboxAppId() != null);
		checkWebsiteService.setSwitchIcon(switchImage, criteria.getSwitchAppId() != null);
	}
	
	private void doSearch() {
		WebsiteSearchCriteria criteria = checkWebsiteService.extractWebsite(searchText.getText().toString());
		if (criteria.hasValue()) {
			activity.clearFocusAndHideKeyboard(searchText);
			activity.startCheckWebsite(criteria);
		}
	}
	
	public String getCurrentValue() {
		return searchText.getText().toString();
	}
	
}
