package com.ntredize.redstop.support.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FileUtils {
	
	private static final String TEMP_FOLDER_NAME = "temp";
	private static final String TEMP_RED_COMPANY_DETAIL_IMAGE_FILE_NAME = "redCompanyDetail.jpg";
	private static final String TEMP_RED_COMPANY_RELATED_DETAIL_IMAGE_FILE_NAME = "redCompanyRelatedDetail.jpg";
	private static final String TEMP_SHARE_IMAGE_FILE_NAME = "tempShare.jpg";

	private static final String CACHE_FOLDER_NAME = "cache";
	private static final String CACHE_IMAGE_FOLDER_NAME = "image";
	
	private final Context context;
	
	
	/* Init */
	public FileUtils(Context context) {
		this.context = context;
	}


	/* Folder / File */
	private File getExternalFileFolder() {
		return context.getExternalFilesDir(null);
	}
	
	private File getTempFolder() {
		File tempFolder = new File(getExternalFileFolder(), TEMP_FOLDER_NAME);
		createFolder(tempFolder);
		return tempFolder;
	}
	
	public String getTempRedCompanyDetailImageFileName() {
		return TEMP_RED_COMPANY_DETAIL_IMAGE_FILE_NAME;
	}
	
	public String getTempRedCompanyRelatedDetailImageFileName() {
		return TEMP_RED_COMPANY_RELATED_DETAIL_IMAGE_FILE_NAME;
	}
	
	public File getTempRedCompanyImageFile(String fileName) {
		return new File(getTempFolder(), fileName);
	}
	
	public File getTempShareImageFile() {
		return new File(getTempFolder(), TEMP_SHARE_IMAGE_FILE_NAME);
	}

	public File getCacheFolder() {
		File cacheFolder = new File(getExternalFileFolder(), CACHE_FOLDER_NAME);
		createFolder(cacheFolder);
		return cacheFolder;
	}

	public File getCacheImageFolder() {
		File cacheImageFolder = new File(getCacheFolder(), CACHE_IMAGE_FOLDER_NAME);
		createFolder(cacheImageFolder);
		return cacheImageFolder;
	}


	/* Create Folder */
	public void createFolder(File folder) {
		if (folder != null && !folder.exists()) {
			folder.mkdirs();
		}
	}


	/* List Folder */
	public List<File> listFolder(File folder) {
		try {
			return Arrays.asList(Objects.requireNonNull(folder.listFiles()));
		} catch (Exception ignored) {
			return new ArrayList<>();
		}
	}


	/* File Exist */
	public boolean isFileExist(File file) {
		return file != null && file.exists();
	}

	public boolean isFolder(File file) {
		return file != null && file.isDirectory();
	}


	/* Created */
	public Date getFileLastModified(File file) {
		return new Date(file.lastModified());
	}
	
	
	/* File Size */
	private long getFileSize(File file) {
		return file.length();
	}


	/* Save / Delete */
	public void saveBitmapJpg(File file, Bitmap bitmap, Long maxSize) {
		saveBitmap(file, bitmap, maxSize, Bitmap.CompressFormat.JPEG);
	}

	public void saveBitmapPng(File file, Bitmap bitmap, Long maxSize) {
		saveBitmap(file, bitmap, maxSize, Bitmap.CompressFormat.PNG);
	}
	
	private void saveBitmap(File file, Bitmap bitmap, Long maxSize, Bitmap.CompressFormat compressFormat) {
		int quality = 100;
		do {
			if (file.exists()) deleteFile(file);
			saveBitmap(file, bitmap, compressFormat, quality);
			quality -= 10;
		} while (maxSize != null && quality > 0 && getFileSize(file) > maxSize);
	}

	private void saveBitmap(File file, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
		try (FileOutputStream out = new FileOutputStream(file)) {
			bitmap.compress(compressFormat, quality, out);
		} catch (Exception ignored) {
		}
	}

	public void deleteFile(File file) {
		file.delete();
	}
	
}
