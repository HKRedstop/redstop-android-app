package com.ntredize.redstop.main.fragment.redcompany;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanyDetail;
import com.ntredize.redstop.db.model.RedCompanyDetailDesc;
import com.ntredize.redstop.support.service.DownloadImageService;
import com.ntredize.redstop.support.service.RedCompanyService;
import com.ntredize.redstop.support.utils.AttrUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RedCompanyDetailInfoFragment extends Fragment {
	
	public static final String KEY_IS_DARK_MODE = "KEY_IS_DARK_MODE";
	public static final String KEY_RED_COMPANY_DETAIL = "KEY_RED_COMPANY_DETAIL";
	public static final String KEY_RED_COMPANY_LOGO = "KEY_RED_COMPANY_LOGO";
	
	// service
	private AttrUtils attrUtils;
	private RedCompanyService redCompanyService;
	private DownloadImageService downloadImageService;
	
	// data
	private boolean isDarkMode;
	private RedCompanyDetail redCompanyDetail;
	private String logoFileName;
	
	// init flag
	private boolean hasInit;
	
	
	/* Init */
	public void initData() {
		if (!hasInit) {
			// from arguments
			Bundle args = getArguments();
			
			// service
			attrUtils = new AttrUtils(getActivity());
			redCompanyService = new RedCompanyService(getActivity());
			downloadImageService = new DownloadImageService(getActivity());
			
			// data
			if (args != null) {
				isDarkMode = args.getBoolean(KEY_IS_DARK_MODE);
				redCompanyDetail = (RedCompanyDetail) args.getSerializable(KEY_RED_COMPANY_DETAIL);
				logoFileName = args.getString(KEY_RED_COMPANY_LOGO);
			}
			
			// finish init
			hasInit = true;
		}
	}
	
	
	/* Layout */
	@SuppressLint("SetTextI18n")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_red_company_detail_info, container, false);
		
		// init data
		initData();
		String nameHk = redCompanyDetail.getFullNameHk();
		String nameEn = redCompanyDetail.getFullNameEn();
		String redStarType = redCompanyDetail.getRedStarType();
		Integer redStar = redCompanyDetail.getRedStar();
		List<RedCompanyDetailDesc> descs = redCompanyDetail.getDescs();
		List<RedCompanyDetailDesc> stockInfos = redCompanyDetail.getStockInfos();
		String updateDate = redCompanyDetail.getUpdateDate();
		
		// logo
		RelativeLayout logoImageContainer = fragmentView.findViewById(R.id.red_company_detail_info_logo_image_container);
		ImageView logoImage = fragmentView.findViewById(R.id.red_company_detail_info_logo_image);
		
		Bitmap redCompanyLogo = downloadImageService.getTempRedCompanyImage(logoFileName);
		if (redCompanyLogo != null) {
			logoImageContainer.setBackgroundColor(attrUtils.getAttrColorInt(R.attr.imageBorderColor));
			logoImage.setBackgroundColor(attrUtils.getAttrColorInt(R.attr.imageBackgroundColor));
			logoImage.setImageBitmap(redCompanyLogo);
			
			// special handle for light mode (resize the logo image)
			if (!isDarkMode) {
				int width = redCompanyLogo.getWidth();
				int height = redCompanyLogo.getHeight();
				
				if (width > height) {
					int targetWidth = logoImage.getLayoutParams().width;
					logoImage.getLayoutParams().height = targetWidth * height / width;
				}
			}
		}
		else logoImageContainer.setVisibility(View.GONE);
		
		// name hk
		TextView nameHkText = fragmentView.findViewById(R.id.red_company_detail_info_name_hk_text);
		if (nameHk != null && !nameHk.isEmpty()) nameHkText.setText(nameHk);
		else nameHkText.setVisibility(View.GONE);
		
		// name en
		TextView nameEnText = fragmentView.findViewById(R.id.red_company_detail_info_name_en_text);
		if (nameEn != null && !nameEn.isEmpty()) nameEnText.setText(nameEn);
		else nameEnText.setVisibility(View.GONE);
		
		// red star
		ImageView redStarImage1 = fragmentView.findViewById(R.id.red_company_detail_info_red_star_image_1);
		ImageView redStarImage2 = fragmentView.findViewById(R.id.red_company_detail_info_red_star_image_2);
		ImageView redStarImage3 = fragmentView.findViewById(R.id.red_company_detail_info_red_star_image_3);
		ImageView redStarImage4 = fragmentView.findViewById(R.id.red_company_detail_info_red_star_image_4);
		ImageView redStarImage5 = fragmentView.findViewById(R.id.red_company_detail_info_red_star_image_5);
		
		List<Drawable> redStarDrawables = redCompanyService.getRedStarDrawables(redStar);
		if (!redStarDrawables.isEmpty()) {
			redStarImage1.setImageDrawable(redStarDrawables.get(0));
			redStarImage2.setImageDrawable(redStarDrawables.get(1));
			redStarImage3.setImageDrawable(redStarDrawables.get(2));
			redStarImage4.setImageDrawable(redStarDrawables.get(3));
			redStarImage5.setImageDrawable(redStarDrawables.get(4));
		}
		
		int redStarColor = redCompanyService.getRedStarColor(redStar);
		redStarImage1.setColorFilter(redStarColor, PorterDuff.Mode.SRC_IN);
		redStarImage2.setColorFilter(redStarColor, PorterDuff.Mode.SRC_IN);
		redStarImage3.setColorFilter(redStarColor, PorterDuff.Mode.SRC_IN);
		redStarImage4.setColorFilter(redStarColor, PorterDuff.Mode.SRC_IN);
		redStarImage5.setColorFilter(redStarColor, PorterDuff.Mode.SRC_IN);
		
		String redStarContentDesc = redCompanyService.getRedStarContentDescription(redStar);
		redStarImage1.setContentDescription(redStarContentDesc);
		redStarImage2.setContentDescription(redStarContentDesc);
		redStarImage3.setContentDescription(redStarContentDesc);
		redStarImage4.setContentDescription(redStarContentDesc);
		redStarImage5.setContentDescription(redStarContentDesc);
		
		TextView redStarDescText = fragmentView.findViewById(R.id.red_company_detail_info_red_star_desc);
		redStarDescText.setText("( " + redCompanyService.getRedStarDesc(redStarType, redStar) + " )");
		
		// desc
		SpannableStringBuilder descBuilder = new SpannableStringBuilder();
		for (RedCompanyDetailDesc desc : descs) {
			SpannableString str = new SpannableString(desc.getContent());
			if (desc.getColor() != null) str.setSpan(new ForegroundColorSpan(redCompanyService.getRedStarColor(desc.getColor())), 0, desc.getContent().length(), 0);
			descBuilder.append(str);
		}
		TextView descText = fragmentView.findViewById(R.id.red_company_detail_info_desc_text);
		descText.setText(descBuilder, TextView.BufferType.SPANNABLE);
		
		// stock
		SpannableStringBuilder stockInfoBuilder = new SpannableStringBuilder();
		for (RedCompanyDetailDesc stockInfo : stockInfos) {
			SpannableString str = new SpannableString(stockInfo.getContent());
			if (stockInfo.getColor() != null) str.setSpan(new ForegroundColorSpan(redCompanyService.getRedStarColor(stockInfo.getColor())), 0, stockInfo.getContent().length(), 0);
			stockInfoBuilder.append(str);
		}
		TextView stockText = fragmentView.findViewById(R.id.red_company_detail_info_stock_text);
		stockText.setText(stockInfoBuilder, TextView.BufferType.SPANNABLE);
		
		// update date
		TextView updateDateText = fragmentView.findViewById(R.id.red_company_detail_info_update_date);
		updateDateText.setText(getActivity().getText(R.string.label_red_company_update_date) + updateDate);
		
		return fragmentView;
	}
	
}
