package com.ntredize.redstop.support.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ntredize.redstop.common.config.ApiPath;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.db.model.WebsiteSearchCriteria;
import com.ntredize.redstop.support.utils.HttpClient;

import java.lang.reflect.Type;

public class CheckWebsiteWebService {
	
	private final HttpClient httpClient;
	private final Gson gson;
	
	
	/* Init */
	public CheckWebsiteWebService(Context context) {
		this.httpClient = new HttpClient(context);
		this.gson = new Gson();
	}
	
	
	/* Check Website */
	public SearchResult<RedCompanySimple> checkWebsite(WebsiteSearchCriteria criteria) {
		// api url
		String apiUrl = ApiPath.API_RES_EXTENSION + ApiPath.API_PATH_EXTENSION_CHECK;
		
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
	
}
