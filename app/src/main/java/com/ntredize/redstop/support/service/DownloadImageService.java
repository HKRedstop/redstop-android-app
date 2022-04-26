package com.ntredize.redstop.support.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ntredize.redstop.support.webservice.DownloadImageWebService;
import com.ntredize.redstop.support.utils.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DownloadImageService {

    private static final String FRIEND_FOLDER = "friend";
    private static final String FRIEND_EXT = "png";

    private static final String RED_COMPANY_FOLDER = "redCompany";
    private static final String RED_COMPANY_EXT = "jpg";

    private final FileUtils fileUtils;
    private final DownloadImageWebService downloadImageWebService;


    /* Init */
    public DownloadImageService(Context context) {
        this.fileUtils = new FileUtils(context);
        this.downloadImageWebService = new DownloadImageWebService(context);
    }


    /* Common */
    public Bitmap getImageByUrl(String url) {
        return downloadImageWebService.getImageByUrl(url);
    }

    private File getCacheImageFile(List<String> folderNameList, String fileName, String ext) {
        File folder = fileUtils.getCacheImageFolder();
        for (String folderName : folderNameList) {
            folder = new File(folder, folderName);
            fileUtils.createFolder(folder);
        }
        return new File(folder, fileName + "." + ext);
    }


    /* URL: Friend */
	public String getFriendThumbnailImageUrl(String friendCode) {
		return downloadImageWebService.getFriendImageUrl(friendCode, true);
	}
    
    
    /* URL: Red Company */
    public String getRedCompanyImageUrl(String groupCode, String companyCode) {
	    return downloadImageWebService.getRedCompanyImageUrl(groupCode, companyCode, false);
    }
	
	public String getRedCompanyThumbnailImageUrl(String groupCode, String companyCode) {
		return downloadImageWebService.getRedCompanyImageUrl(groupCode, companyCode, true);
	}

	
	/* Cache File: Friend */
	private File getFriendCacheImageFile(String friendCode) {
		return getCacheImageFile(Collections.singletonList(FRIEND_FOLDER), friendCode, FRIEND_EXT);
	}
	
	public Bitmap getCacheFriendImage(String friendCode) {
		File file = getFriendCacheImageFile(friendCode);
		if (fileUtils.isFileExist(file)) {
			return BitmapFactory.decodeFile(file.getPath());
		}
		return null;
	}
	
	public void saveFriendImageAsCache(String friendCode, Bitmap bitmap) {
		File file = getFriendCacheImageFile(friendCode);
		if (fileUtils.isFileExist(file)) {
			fileUtils.deleteFile(file);
		}
		fileUtils.saveBitmapPng(file, bitmap, null);
	}


    /* Cache File Red Company */
    private File getRedCompanyCacheImageFile(String groupCode, String companyCode) {
        return getCacheImageFile(Arrays.asList(RED_COMPANY_FOLDER, groupCode), companyCode, RED_COMPANY_EXT);
    }

    public Bitmap getCacheRedCompanyImage(String groupCode, String companyCode) {
        File file = getRedCompanyCacheImageFile(groupCode, companyCode);
        if (fileUtils.isFileExist(file)) {
            return BitmapFactory.decodeFile(file.getPath());
        }
        return null;
    }

    public void saveRedCompanyImageAsCache(String groupCode, String companyCode, Bitmap bitmap) {
        File file = getRedCompanyCacheImageFile(groupCode, companyCode);
        if (fileUtils.isFileExist(file)) {
            fileUtils.deleteFile(file);
        }
        fileUtils.saveBitmapPng(file, bitmap, null);
    }
    
    
    /* Temp File: Red Company */
    public String getTempRedCompanyDetailImageFileName() {
    	return fileUtils.getTempRedCompanyDetailImageFileName();
    }
	
	public String getTempRedCompanyRelatedDetailImageFileName() {
		return fileUtils.getTempRedCompanyRelatedDetailImageFileName();
	}
    
    public void saveTempRedCompanyImage(String fileName, Bitmap bitmap) {
        File file = fileUtils.getTempRedCompanyImageFile(fileName);
        fileUtils.deleteFile(file);
        if (bitmap != null) fileUtils.saveBitmapJpg(file, bitmap, null);
    }
    
    public File getTempRedCompanyImageFile(String fileName) {
	    return fileUtils.getTempRedCompanyImageFile(fileName);
    }
    
    public Bitmap getTempRedCompanyImage(String fileName) {
	    File file = getTempRedCompanyImageFile(fileName);
	    if (fileUtils.isFileExist(file)) {
		    return BitmapFactory.decodeFile(file.getPath());
	    }
	    return null;
    }

    

}
