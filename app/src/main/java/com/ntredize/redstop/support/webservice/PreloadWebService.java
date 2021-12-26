package com.ntredize.redstop.support.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.ntredize.redstop.common.config.ApiPath;
import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.db.model.PreloadData;
import com.ntredize.redstop.support.utils.HttpClient;

public class PreloadWebService {
	
	private final Context context;
	private final HttpClient httpClient;
	private final Gson gson;
	
	
	/* Init */
	public PreloadWebService(Context context) {
		this.context = context;
		this.httpClient = new HttpClient(context);
		this.gson = new Gson();
	}
	
	
	/* Get Preload Data */
	public PreloadData getPreloadData() throws ApplicationException {
		// api url
		String apiUrl = ApiPath.API_RES_PRELOAD + ApiPath.API_PATH_PRELOAD_ANDROID_DATA;
		
		// get
		try {
			byte[] data = httpClient.doGet(apiUrl);
			
			// build model
			return gson.fromJson(new String(data), PreloadData.class);
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception ignored) {
			throw new ApplicationException(context, ApplicationException.SYSTEM_ERROR);
		}
	}
	
}
