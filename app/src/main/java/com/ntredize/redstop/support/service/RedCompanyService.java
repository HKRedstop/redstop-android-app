package com.ntredize.redstop.support.service;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.constants.RedStarType;
import com.ntredize.redstop.db.dao.app.RedCompanyCategoryDAO;
import com.ntredize.redstop.db.dao.app.RedCompanySubCategoryDAO;
import com.ntredize.redstop.db.model.RedCompanyCategory;
import com.ntredize.redstop.db.model.RedCompanyDetail;
import com.ntredize.redstop.db.model.RedCompanyGroupSearchCriteria;
import com.ntredize.redstop.db.model.RedCompanySearchCriteria;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.db.model.RedCompanySimpleWithCategory;
import com.ntredize.redstop.db.model.RedCompanySubCategory;
import com.ntredize.redstop.db.model.RedCompanySubCategorySearchCriteria;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.support.utils.AttrUtils;
import com.ntredize.redstop.support.webservice.RedCompanyWebService;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class RedCompanyService {

	private final Context context;
	private final RedCompanyCategoryDAO redCompanyCategoryDAO;
	private final RedCompanySubCategoryDAO redCompanySubCategoryDAO;
	private final AttrUtils attrUtils;
	private final RedCompanyWebService redCompanyWebService;
	
	
	/* Init */
	public RedCompanyService(Context context) {
		this.context = context;
		redCompanyCategoryDAO = new RedCompanyCategoryDAO(context);
		redCompanySubCategoryDAO = new RedCompanySubCategoryDAO(context);
		attrUtils = new AttrUtils(context);
		redCompanyWebService = new RedCompanyWebService(context);
	}
	
	
	/* URL */
	public String getRedCompanyCodeFromUri(Uri data) {
		// https://www.redstop.info/info/{companyCode}
		if (data != null) {
			List<String> paths = data.getPathSegments();
			if (paths.size() > 1) return paths.get(1);
		}
		return null;
	}
	
	
	/* Red Company Category */
	void insertRedCompanyCategory(List<RedCompanyCategory> list) {
		redCompanyCategoryDAO.insert(list);
	}

	void deleteAllRedCompanyCategory() {
		redCompanyCategoryDAO.deleteAll();
	}

	public List<RedCompanyCategory> getAllRedCompanyCategoryList() {
		return redCompanyCategoryDAO.selectAll();
	}
	
	public RedCompanyCategory getRedCompanyCategoryByCategoryCode(String categoryCode) {
		return redCompanyCategoryDAO.select(categoryCode);
	}
	
	
	/* Red Company Sub Category */
	void insertRedCompanySubCategory(List<RedCompanySubCategory> list) {
		redCompanySubCategoryDAO.insert(list);
	}

	void deleteAllRedCompanySubCategory() {
		redCompanySubCategoryDAO.deleteAll();
	}

	public List<RedCompanySubCategory> getRedCompanySubCategoryListByCategoryCode(String categoryCode) {
		return redCompanySubCategoryDAO.selectAll(categoryCode);
	}
	
	public RedCompanySubCategory getRedCompanySubCategoryBySubCategoryCode(String subCategoryCode) {
		return redCompanySubCategoryDAO.select(subCategoryCode);
	}
	
	
	/* Red Company */
	public SearchResult<RedCompanySimple> getRedCompanySimpleListBySubCategory(RedCompanySubCategorySearchCriteria criteria) {
		return redCompanyWebService.getRedCompanySimpleListBySubCategory(criteria);
	}

	public SearchResult<RedCompanySimpleWithCategory> getRedCompanySimpleWithCategoryListBySearchKeyword(RedCompanySearchCriteria criteria) {
		return redCompanyWebService.getRedCompanySimpleWithCategoryListBySearchKeyword(criteria);
	}

	public SearchResult<RedCompanySimpleWithCategory> getRedCompanySimpleWithCategoryListByGroup(RedCompanyGroupSearchCriteria criteria) {
		return redCompanyWebService.getRedCompanySimpleWithCategoryListByGroup(criteria);
	}
	
	public RedCompanyDetail getRedCompanyDetailByCompanyCode(String companyCode) {
		return redCompanyWebService.getRedCompanyDetailByCompanyCode(companyCode);
	}
	
	
	/* Red Star */
	public List<Drawable> getRedStarDrawables(Integer redStar) {
		List<Integer> drawableIds = new ArrayList<>();
		if (redStar != null) {
			for (int i = 1; i <= 5; i++) {
				if (i <= redStar) drawableIds.add(R.drawable.image_star_fill);
				else drawableIds.add(R.drawable.image_star_outline);
			}
		}
		
		if (drawableIds.isEmpty()) return new ArrayList<>();
		else {
			List<Drawable> drawables = new ArrayList<>();
			for (Integer drawableId : drawableIds) {
				drawables.add(ContextCompat.getDrawable(context, drawableId));
			}
			return drawables;
		}
	}
	
	public Integer getRedStarColor(Integer redStar) {
		int attrId = R.attr.textNormalColor;
		if (redStar != null && redStar == 5) attrId = R.attr.star5Color;
		else if (redStar != null && redStar == 4) attrId = R.attr.star4Color;
		else if (redStar != null && redStar == 3) attrId = R.attr.star3Color;
		else if (redStar != null && redStar == 2) attrId = R.attr.star2Color;
		else if (redStar != null && redStar == 1) attrId = R.attr.star1Color;
		else if (redStar != null && redStar == 0) attrId = R.attr.star0Color;
		
		return attrUtils.getAttrColorInt(attrId);
	}
	
	public String getRedStarContentDescription(Integer redStar) {
		int strId = R.string.content_desc_company_red_star;
		if (redStar != null && redStar == 5) strId = R.string.content_desc_company_red_star_5;
		else if (redStar != null && redStar == 4) strId = R.string.content_desc_company_red_star_4;
		else if (redStar != null && redStar == 3) strId = R.string.content_desc_company_red_star_3;
		else if (redStar != null && redStar == 2) strId = R.string.content_desc_company_red_star_2;
		else if (redStar != null && redStar == 1) strId = R.string.content_desc_company_red_star_1;
		else if (redStar != null && redStar == 0) strId = R.string.content_desc_company_red_star_0;

		return context.getString(strId);
	}
	
	public String getRedStarDesc(String redStarType, Integer redStar) {
		Integer strId = null;
		
		if (RedStarType.C.equals(redStarType)) {
			if (redStar == 5) strId = R.string.help_company_star_type_c5;
			else if (redStar == 4) strId = R.string.help_company_star_type_c4;
			else if (redStar == 3) strId = R.string.help_company_star_type_c3;
			else if (redStar == 2) strId = R.string.help_company_star_type_c2;
			else if (redStar == 1) strId = R.string.help_company_star_type_c1;
		}
		else if (RedStarType.H.equals(redStarType)) {
			if (redStar == 5) strId = R.string.help_company_star_type_h5;
			else if (redStar == 4) strId = R.string.help_company_star_type_h4;
			else if (redStar == 3) strId = R.string.help_company_star_type_h3;
			else if (redStar == 2) strId = R.string.help_company_star_type_h2;
			else if (redStar == 1) strId = R.string.help_company_star_type_h1;
			else if (redStar == 0) strId = R.string.help_company_star_type_h0;
		}
		else if (RedStarType.F.equals(redStarType)) {
			if (redStar == 3) strId = R.string.help_company_star_type_f3;
			else if (redStar == 2) strId = R.string.help_company_star_type_f2;
			else if (redStar == 1) strId = R.string.help_company_star_type_f1;
			else if (redStar == 0) strId = R.string.help_company_star_type_f0;
		}
		else if (RedStarType.W.equals(redStarType)) {
			if (redStar == 2) strId = R.string.help_company_star_type_w2;
			else if (redStar == 1) strId = R.string.help_company_star_type_w1;
			else if (redStar == 0) strId = R.string.help_company_star_type_w0;
		}
		
		// remove all \n
		if (strId != null) return context.getString(strId).replaceAll("\n", " ");
		else return "";
	}
	
}
