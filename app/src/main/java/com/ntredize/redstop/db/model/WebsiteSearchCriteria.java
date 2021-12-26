package com.ntredize.redstop.db.model;

import java.util.HashMap;
import java.util.Map;

public class WebsiteSearchCriteria {
	
	private String website;
	private String wikiId;
	private String facebookId;
	private String igId;
	private String twitterId;
	private String openriceId;
	private String iosStoreId;
	private String iosAppId;
	private String androidStoreId;
	private String androidPackage;
	private String steamStoreId;
	private String steamAppId;
	private String psAppId;
	private String xboxAppId;
	private String switchAppId;
	
	public boolean hasValue() {
		return hasValue(website) || hasValue(wikiId) || hasValue(facebookId) || hasValue(igId) || hasValue(twitterId) || hasValue(openriceId)
				|| hasValue(iosStoreId) || hasValue(iosAppId) || hasValue(androidStoreId) || hasValue(androidPackage)
				|| hasValue(steamStoreId) || hasValue(steamAppId) || hasValue(psAppId) || hasValue(xboxAppId) || hasValue(switchAppId);
	}
	
	private boolean hasValue(String value) {
		return value != null && !value.isEmpty();
	}
	
	public Map<String, String> toUrlQueryItems() {
		Map<String, String> map = new HashMap<>();
		toUrlQueryItem(map, "website", website);
		toUrlQueryItem(map, "wikiId", wikiId);
		toUrlQueryItem(map, "facebookId", facebookId);
		toUrlQueryItem(map, "igId", igId);
		toUrlQueryItem(map, "twitterId", twitterId);
		toUrlQueryItem(map, "openriceId", openriceId);
		toUrlQueryItem(map, "iosStoreId", iosStoreId);
		toUrlQueryItem(map, "iosAppId", iosAppId);
		toUrlQueryItem(map, "androidStoreId", androidStoreId);
		toUrlQueryItem(map, "androidPackage", androidPackage);
		toUrlQueryItem(map, "steamStoreId", steamStoreId);
		toUrlQueryItem(map, "steamAppId", steamAppId);
		toUrlQueryItem(map, "psAppId", psAppId);
		toUrlQueryItem(map, "xboxAppId", xboxAppId);
		toUrlQueryItem(map, "switchAppId", switchAppId);
		return map;
	}
	
	private void toUrlQueryItem(Map<String, String> map, String key, String value) {
		if (hasValue(value)) map.put(key, value);
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public String getWikiId() {
		return wikiId;
	}
	
	public void setWikiId(String wikiId) {
		this.wikiId = wikiId;
	}
	
	public String getFacebookId() {
		return facebookId;
	}
	
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getIgId() {
		return igId;
	}
	
	public void setIgId(String igId) {
		this.igId = igId;
	}
	
	public String getTwitterId() {
		return twitterId;
	}
	
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	
	public String getOpenriceId() {
		return openriceId;
	}
	
	public void setOpenriceId(String openriceId) {
		this.openriceId = openriceId;
	}
	
	public String getIosStoreId() {
		return iosStoreId;
	}
	
	public void setIosStoreId(String iosStoreId) {
		this.iosStoreId = iosStoreId;
	}
	
	public String getIosAppId() {
		return iosAppId;
	}
	
	public void setIosAppId(String iosAppId) {
		this.iosAppId = iosAppId;
	}
	
	public String getAndroidStoreId() {
		return androidStoreId;
	}
	
	public void setAndroidStoreId(String androidStoreId) {
		this.androidStoreId = androidStoreId;
	}
	
	public String getAndroidPackage() {
		return androidPackage;
	}
	
	public void setAndroidPackage(String androidPackage) {
		this.androidPackage = androidPackage;
	}
	
	public String getSteamStoreId() {
		return steamStoreId;
	}
	
	public void setSteamStoreId(String steamStoreId) {
		this.steamStoreId = steamStoreId;
	}
	
	public String getSteamAppId() {
		return steamAppId;
	}
	
	public void setSteamAppId(String steamAppId) {
		this.steamAppId = steamAppId;
	}
	
	public String getPsAppId() {
		return psAppId;
	}
	
	public void setPsAppId(String psAppId) {
		this.psAppId = psAppId;
	}
	
	public String getXboxAppId() {
		return xboxAppId;
	}
	
	public void setXboxAppId(String xboxAppId) {
		this.xboxAppId = xboxAppId;
	}
	
	public String getSwitchAppId() {
		return switchAppId;
	}
	
	public void setSwitchAppId(String switchAppId) {
		this.switchAppId = switchAppId;
	}
}
