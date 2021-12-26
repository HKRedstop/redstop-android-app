package com.ntredize.redstop.main.drawer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class DrawerItem {
	
	public final static String TYPE_HEADER = "TYPE_HEADER";
	public final static String TYPE_ITEM = "TYPE_ITEM";
	
	private String type;
	
	private String headerTitle;

	private String imageCacheKey;
	private String imageUrl;
	private Drawable imageDrawable;
	private Bitmap imageBitmap;
	private boolean tintImage;
	private String name;
	private String desc;
	private Runnable action;
	
	private boolean enabled;
	
	public DrawerItem() {
		type = TYPE_ITEM;
		tintImage = false;
		enabled = true;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getHeaderTitle() {
		return headerTitle;
	}
	
	public void setHeaderTitle(String headerTitle) {
		this.headerTitle = headerTitle;
	}

	public Drawable getImageDrawable() {
		return imageDrawable;
	}

	public void setImageDrawable(Drawable imageDrawable) {
		this.imageDrawable = imageDrawable;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Runnable getAction() {
		return action;
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageCacheKey() {
		return imageCacheKey;
	}

	public void setImageCacheKey(String imageCacheKey) {
		this.imageCacheKey = imageCacheKey;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public boolean isTintImage() {
		return tintImage;
	}
	
	public void setTintImage(boolean tintImage) {
		this.tintImage = tintImage;
	}
}
