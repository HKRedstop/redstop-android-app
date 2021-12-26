package com.ntredize.redstop.support.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanyDetail;
import com.ntredize.redstop.db.model.RedCompanyDetailDesc;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.utils.FileUtils;

import java.io.File;

import androidx.core.content.FileProvider;

public class ShareService {
	
	private static final int SHARE_IMAGE_FINAL_SIZE = 400;
	private static final float SHARE_IMAGE_BORDER_PERCENT = 0.15f;
	
	private final ActivityBase activity;
	private final FileUtils fileUtils;
	private final RedCompanyService redCompanyService;
	
	
	/* Init */
	public ShareService(Context context) {
		this.activity = (ActivityBase) context;
		this.fileUtils = new FileUtils(context);
		this.redCompanyService = new RedCompanyService(context);
	}
	
	
	/* Image */
	public Uri getShareImageUri(File file) {
		if (file != null && file.exists()) {
			try {
				File tempShareFile = buildShareImageFile(file);
				return FileProvider.getUriForFile(activity, "com.ntredize.redstop.provider", tempShareFile);
			} catch (Exception ignored) {}
		}
		
		return null;
	}
	
	private File buildShareImageFile(File file) {
		// create a temp share image which has border and in square shape
		Bitmap src = BitmapFactory.decodeFile(file.getAbsolutePath());
		
		int imageSize = Math.max(src.getWidth(), src.getHeight());
		int borderSize = (int) (imageSize * SHARE_IMAGE_BORDER_PERCENT);
		int x = borderSize + (imageSize - src.getWidth()) / 2;
		int y = borderSize + (imageSize - src.getHeight()) / 2;
		
		Bitmap squareBitmap = Bitmap.createBitmap(imageSize + borderSize * 2, imageSize + borderSize * 2, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(squareBitmap);
		canvas.drawRGB(255, 255, 255);
		canvas.drawBitmap(src, x, y, null);
		
		// resize
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(squareBitmap, SHARE_IMAGE_FINAL_SIZE, SHARE_IMAGE_FINAL_SIZE, false);
		
		// save to temp file
		File tempFile = fileUtils.getTempShareImageFile();
		fileUtils.saveBitmapJpg(tempFile, resizedBitmap, null);
		return tempFile;
	}
	
	
	/* Red Company */
	public String buildShareRedCompanyMsg(RedCompanyDetail redCompanyDetail) {
		StringBuilder sb = new StringBuilder();
		
		// name hk
		if (redCompanyDetail.getFullNameHk() != null && !redCompanyDetail.getFullNameHk().isEmpty()) {
			appendStr(sb, redCompanyDetail.getFullNameHk());
		}
		
		// name en
		if (redCompanyDetail.getFullNameEn() != null && !redCompanyDetail.getFullNameEn().isEmpty()) {
			appendStr(sb, redCompanyDetail.getFullNameEn());
		}
		
		// red star
		if (redCompanyDetail.getRedStar() != null) {
			appendChangeLine(sb);
			appendRedStar(sb, redCompanyDetail.getRedStar());
			
			if (redCompanyDetail.getRedStarType() != null) {
				appendStr(sb, "(" + redCompanyService.getRedStarDesc(redCompanyDetail.getRedStarType(), redCompanyDetail.getRedStar()) + ")");
			}
		}
		
		// desc
		if (redCompanyDetail.getDescs() != null && !redCompanyDetail.getDescs().isEmpty()) {
			StringBuilder descBuilder = new StringBuilder();
			for (RedCompanyDetailDesc desc : redCompanyDetail.getDescs()) {
				descBuilder.append(desc.getContent());
			}
			appendChangeLine(sb);
			appendStr(sb, descBuilder.toString());
		}
		
		// stock
		if (redCompanyDetail.getStockInfos() != null && !redCompanyDetail.getStockInfos().isEmpty()) {
			StringBuilder stockInfoBuilder = new StringBuilder();
			for (RedCompanyDetailDesc stockInfo : redCompanyDetail.getStockInfos()) {
				stockInfoBuilder.append(stockInfo.getContent());
			}
			appendChangeLine(sb);
			appendStr(sb, stockInfoBuilder.toString());
		}
		
		// footer
		appendFooter(sb, redCompanyDetail.getCompanyCode());
		
		return sb.toString();
	}
	
	
	/* Common */
	private void appendStr(StringBuilder sb, String str) {
		if (sb.length() > 0) appendChangeLine(sb);
		sb.append(str);
	}
	
	private void appendRedStar(StringBuilder sb, Integer redStar) {
		if (sb.length() > 0) appendChangeLine(sb);
		for (int i = 0; i < 5; i++) {
			if (redStar > i) sb.append(getMsg(R.string.share_red_star_fill));
			else sb.append(getMsg(R.string.share_red_star_empty));
		}
	}
	
	private void appendChangeLine(StringBuilder sb) {
		sb.append("\n");
	}
	
	private void appendFooter(StringBuilder sb, String companyCode) {
		sb.append(getMsg(R.string.share_footer_separator));
		sb.append(getMsg(R.string.share_footer_1, getMsg(R.string.app_name)));
		sb.append(getMsg(R.string.share_footer_2, companyCode));
	}
	
	private String getMsg(int id, String... args) {
		String msg = activity.getString(id);
		
		for (int i = 0; i < args.length; i++) {
			msg = msg.replace("{" + i + "}", args[i]);
		}
		
		return msg;
	}
	
}
