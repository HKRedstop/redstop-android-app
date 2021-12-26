package com.ntredize.redstop.support.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ntredize.redstop.common.config.ApiPath;
import com.ntredize.redstop.db.model.RedCompanyDetail;
import com.ntredize.redstop.db.model.RedCompanyGroupSearchCriteria;
import com.ntredize.redstop.db.model.RedCompanySearchCriteria;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.db.model.RedCompanySimpleWithCategory;
import com.ntredize.redstop.db.model.RedCompanySubCategorySearchCriteria;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.support.utils.HttpClient;

import java.lang.reflect.Type;

public class RedCompanyWebService {
	
	private final HttpClient httpClient;
	private final Gson gson;
	
	
	/* Init */
	public RedCompanyWebService(Context context) {
		this.httpClient = new HttpClient(context);
		this.gson = new Gson();
	}
	
	
	/* Get Red Company List By Sub Category */
	public SearchResult<RedCompanySimple> getRedCompanySimpleListBySubCategory(RedCompanySubCategorySearchCriteria criteria) {
		// api url
		String apiUrl = ApiPath.API_RES_RED_COMPANY + ApiPath.API_PATH_RED_COMPANY_LIST_BY_SUB_CATEGORY;
		
		// get
		try {
			byte[] data = httpClient.doGet(apiUrl, criteria.toUrlQueryItems());
			
			// build model
			Type type = TypeToken.getParameterized(SearchResult.class, RedCompanySimple.class).getType();
			return gson.fromJson(new String(data), type);
		} catch (Exception ignored) {
			return null;
		}
	}
	
	
	/* Get Red Company List By Search Keyword */
	public SearchResult<RedCompanySimpleWithCategory> getRedCompanySimpleWithCategoryListBySearchKeyword(RedCompanySearchCriteria criteria) {
		// api url
		String apiUrl = ApiPath.API_RES_RED_COMPANY + ApiPath.API_PATH_RED_COMPANY_SEARCH;
		
		// get
		try {
			byte[] data = httpClient.doGet(apiUrl, criteria.toUrlQueryItems());
			
			// build model
			Type type = TypeToken.getParameterized(SearchResult.class, RedCompanySimpleWithCategory.class).getType();
			return gson.fromJson(new String(data), type);
		} catch (Exception ignored) {
			return null;
		}
	}
	
	
	/* Get Red Company List By Group */
	public SearchResult<RedCompanySimpleWithCategory> getRedCompanySimpleWithCategoryListByGroup(RedCompanyGroupSearchCriteria criteria) {
		// api url
		String apiUrl = ApiPath.API_RES_RED_COMPANY + ApiPath.API_PATH_RED_COMPANY_LIST_BY_GROUP;
		
		// get
		try {
			byte[] data = httpClient.doGet(apiUrl, criteria.toUrlQueryItems());
			
			// build model
			Type type = TypeToken.getParameterized(SearchResult.class, RedCompanySimpleWithCategory.class).getType();
			return gson.fromJson(new String(data), type);
		} catch (Exception ignored) {
			return null;
		}
	}
	
	
	/* Get Red Company Detail */
	public RedCompanyDetail getRedCompanyDetailByCompanyCode(String companyCode) {
		// api url
		String apiUrl = ApiPath.API_RES_RED_COMPANY + ApiPath.API_PATH_RED_COMPANY_DETAIL;
		apiUrl = apiUrl.replace(ApiPath.API_PARAM_COMPANY_CODE, companyCode);
		
		// get
		try {
			byte[] data = httpClient.doGet(apiUrl);
			
			// build model
			return gson.fromJson(new String(data), RedCompanyDetail.class);
		} catch (Exception ignored) {
			return null;
		}
	}
	
}
