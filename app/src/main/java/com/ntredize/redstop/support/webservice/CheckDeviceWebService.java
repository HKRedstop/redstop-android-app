package com.ntredize.redstop.support.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.ntredize.redstop.common.config.ApiPath;
import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.db.model.AndroidDeviceSearchCriteria;
import com.ntredize.redstop.db.model.AndroidDeviceSearchResult;
import com.ntredize.redstop.support.utils.HttpClient;

public class CheckDeviceWebService {
	
	private final HttpClient httpClient;
	private final Gson gson;
	
	
	/* Init */
	public CheckDeviceWebService(Context context) {
		this.httpClient = new HttpClient(context);
		this.gson = new Gson();
	}
	
	
	/* Check Android Device */
	public AndroidDeviceSearchResult checkAndroidDevice(AndroidDeviceSearchCriteria criteria) throws ApplicationException {
		// api url
		String apiUrl = ApiPath.API_RES_DEVICE + ApiPath.API_PATH_DEVICE_CHECK_ANDROID;
		
		// get
		byte[] data = httpClient.doPost(apiUrl, gson.toJson(criteria));
		
		// build model
		return gson.fromJson(new String(data), AndroidDeviceSearchResult.class);
	}
	
}
