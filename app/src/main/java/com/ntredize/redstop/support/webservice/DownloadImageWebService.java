package com.ntredize.redstop.support.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ntredize.redstop.common.config.ApiPath;
import com.ntredize.redstop.support.utils.HttpClient;

public class DownloadImageWebService {
    
    private final HttpClient httpClient;
    
    /* Init */
    public DownloadImageWebService(Context context) {
        this.httpClient = new HttpClient(context);
    }
    

    /* Common */
    public Bitmap getImageByUrl(String url) {
        byte[] data = httpClient.doGetImage(url);
        if (data != null) {
            try {
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            } catch (Exception ignored) {}
        }
        return null;
    }


    /* Friend */
    public String getFriendImageUrl(String friendCode, boolean isThumbnail) {
        String apiUrl = isThumbnail ? ApiPath.API_PATH_IMAGE_FRIEND_THUMBNAIL : ApiPath.API_PATH_IMAGE_FRIEND;
        apiUrl = apiUrl.replace(ApiPath.API_PARAM_FRIEND_CODE, friendCode);
        return apiUrl;
    }


    /* Red Company */
    public String getRedCompanyImageUrl(String groupCode, String companyCode, boolean isThumbnail) {
        String apiUrl = isThumbnail ? ApiPath.API_PATH_IMAGE_RED_COMPANY_THUMBNAIL : ApiPath.API_PATH_IMAGE_RED_COMPANY;
        apiUrl = apiUrl.replace(ApiPath.API_PARAM_GROUP_CODE, groupCode);
        apiUrl = apiUrl.replace(ApiPath.API_PARAM_COMPANY_CODE, companyCode);
        return apiUrl;
    }

}
