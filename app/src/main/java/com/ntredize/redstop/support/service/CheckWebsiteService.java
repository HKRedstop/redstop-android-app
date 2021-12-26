package com.ntredize.redstop.support.service;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.widget.ImageView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanySimple;
import com.ntredize.redstop.db.model.SearchResult;
import com.ntredize.redstop.db.model.WebsiteSearchCriteria;
import com.ntredize.redstop.support.utils.AttrUtils;
import com.ntredize.redstop.support.webservice.CheckWebsiteWebService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckWebsiteService {
	
	private final AttrUtils attrUtils;
	private final CheckWebsiteWebService checkWebsiteWebService;
	
	
	/* Init */
	public CheckWebsiteService(Context context) {
		this.attrUtils = new AttrUtils(context);
		this.checkWebsiteWebService = new CheckWebsiteWebService(context);
	}
	
	
	/* Extract Website */
	public WebsiteSearchCriteria extractWebsite(String website) {
		WebsiteSearchCriteria criteria = new WebsiteSearchCriteria();
		Pattern pattern = Pattern.compile("^(https?://)?([a-z0-9_\\-]+(\\.[a-z0-9_\\-]+)+)(:[0-9]+)?([/?#].*)?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(website);
		
		if (matcher.matches()) {
			String domain = matcher.group(2);
			String param = matcher.group(5);
			criteria.setWebsite(domain);
			if (param != null && !param.isEmpty()) param = param.substring(1);
			
			// wiki: {language}.wikipedia.org/wiki/{id}
			// 1. need to convert special char
			Pattern wikiPattern = Pattern.compile("^[a-z0-9_\\-]+/([^?#]+)([?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setWikiId(extractExtension(domain, param, "wikipedia.org", wikiPattern, 1));
			criteria.setWikiId(convertSpecialChar(criteria.getWikiId()));
			
			// facebook: {language}.facebook.com/(pg/|pages/category/{name}/)?{id}
			Pattern facebookPattern = Pattern.compile("^(pg/|pages/category/[^/]+/)?([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setFacebookId(extractExtension(domain, param, "facebook.com", facebookPattern, 2));
			
			// ig: www.instagram.com/{id}
			Pattern igPattern = Pattern.compile("^([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setIgId(extractExtension(domain, param, "instagram.com", igPattern, 1));
			
			// twitter: www.twitter.com/{id}
			Pattern twitterPattern = Pattern.compile("^([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setTwitterId(extractExtension(domain, param, "twitter.com", twitterPattern, 1));
			
			// openrice: www.openrice.com/{language}/hongkong/r-{name}-{id: r[0-9]+}
			Pattern openricePattern = Pattern.compile("^[a-z0-9_\\-]+/hongkong/r-.+-(r[0-9]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setOpenriceId(extractExtension(domain, param, "openrice.com", openricePattern, 1));
			
			// ios store: apps.apple.com/{language}?/developer/{name}?/{id: id[0-9]+}
			Pattern iosStorePattern = Pattern.compile("^([a-z0-9_\\-]+/)?developer/([^/]+/)?(id[0-9]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setIosStoreId(extractExtension(domain, param, "apps.apple.com", iosStorePattern, 3));
			
			// ios app: apps.apple.com/{language}?/(app|app-bundle)/{name}?/{id: id[0-9]+}
			Pattern iosAppPattern = Pattern.compile("^([a-z0-9_\\-]+/)?(app|app-bundle)/([^/]+/)?(id[0-9]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setIosAppId(extractExtension(domain, param, "apps.apple.com", iosAppPattern, 4));
			
			// android store: play.google.com/store/(apps/(dev|developer)|books/author)?{queryParam: id={id}}
			// 1. can contain /
			// 2. need to convert special char
			Pattern androidStorePattern = Pattern.compile("^store/(apps/(dev|developer)|books/author)\\?([^?#]+)(#.*)?$", Pattern.CASE_INSENSITIVE);
			String androidStoreQueryParamStr = extractExtension(domain, param, "play.google.com", androidStorePattern, 3);
			criteria.setAndroidStoreId(extractQueryParam(androidStoreQueryParamStr, "id"));
			criteria.setAndroidStoreId(convertSpecialChar(criteria.getAndroidStoreId()));
			
			// android package: play.google.com/store/apps/details?{queryParam: id={id}}
			Pattern androidPackagePattern = Pattern.compile("^store/apps/details\\?([^/?#]+)(#.*)?$", Pattern.CASE_INSENSITIVE);
			String androidPackageQueryParamStr = extractExtension(domain, param, "play.google.com", androidPackagePattern, 1);
			criteria.setAndroidPackage(extractQueryParam(androidPackageQueryParamStr, "id"));
			
			// steam store: store.steampowered.com/(developer|publisher|curator|franchise)/{id}
			// 1. need to convert special char
			Pattern steamStorePattern = Pattern.compile("^(developer|publisher|curator|franchise)/([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setSteamStoreId(extractExtension(domain, param, "store.steampowered.com", steamStorePattern, 2));
			criteria.setSteamStoreId(convertSpecialChar(criteria.getSteamStoreId()));
			
			// steam app: store.steampowered.com/agecheck?/app/{id: [0-9]}
			Pattern steamAppPattern = Pattern.compile("^(agecheck/)?app/([0-9]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			criteria.setSteamAppId(extractExtension(domain, param, "store.steampowered.com", steamAppPattern, 2));
			
			// ps app:
			// (www|store).playstation.com/{language}/(games|concept|editorial/this-month-on-playstation)/{id}
			// {www|store}.playstation.com/{language}/product/{id: [a-z0-9\-]+}_[a-z0-9\-]+
			Pattern psAppPattern1 = Pattern.compile("^[a-z0-9_\\-]+/(games|concept|editorial|this-month-on-playstation)/([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			Pattern psAppPattern2 = Pattern.compile("^[a-z0-9_\\-]+/product/([a-z0-9\\-]+)_[a-z0-9\\-]+([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			String psAppId = extractExtension(domain, param, "playstation.com", psAppPattern1, 2);
			if (psAppId == null) psAppId = extractExtension(domain, param, "playstation.com", psAppPattern2, 1);
			criteria.setPsAppId(psAppId);
			
			// xbox app:
			// (www|marketplace).xbox.com/{language}/(games|product)/store?/{id}
			// www.microsoft.com/{language}/p/{id}
			Pattern xboxAppPattern1 = Pattern.compile("^[a-z0-9_\\-]+/(games|product)/(store/)?([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			Pattern xboxAppPattern2 = Pattern.compile("^[a-z0-9_\\-]+/p/([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			String xboxAppId = extractExtension(domain, param, "xbox.com", xboxAppPattern1, 3);
			if (xboxAppId == null) xboxAppId = extractExtension(domain, param, "microsoft.com", xboxAppPattern2, 1);
			criteria.setXboxAppId(xboxAppId);
			
			// switch app:
			// (www|store).nintendo.com.hk/switch?/{id}
			// www.nintendo.com/{language}?/(games/detail|{name}/titles)/{id}
			// www.nintendo.co.uk/games/{systemType}/{id}.html
			// store-jp.nintendo.com/list/software/{id}.html
			Pattern switchAppPattern1 = Pattern.compile("^(switch/)?([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			Pattern switchAppPattern2 = Pattern.compile("^([a-z0-9_\\-]+/)?(games/detail|[^/]+/titles)/([^/?#]+)([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			Pattern switchAppPattern3 = Pattern.compile("^games/[^/]+/([^/?#.]+)\\.html([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			Pattern switchAppPattern4 = Pattern.compile("^list/software/([^/?#.]+)\\.html([/?#].*)?$", Pattern.CASE_INSENSITIVE);
			String switchAppId = extractExtension(domain, param, "nintendo.com.hk", switchAppPattern1, 2);
			if (switchAppId == null) switchAppId = extractExtension(domain, param, "nintendo.com", switchAppPattern2, 3);
			if (switchAppId == null) switchAppId = extractExtension(domain, param, "nintendo.co.uk", switchAppPattern3, 1);
			if (switchAppId == null) switchAppId = extractExtension(domain, param, "store-jp.nintendo.com", switchAppPattern4, 1);
			criteria.setSwitchAppId(switchAppId);
		}
		
		return criteria;
	}
	
	private String extractExtension(String domain, String param, String matchDomain, Pattern matchRegex, int matchGroup) {
		if (domain != null && !domain.isEmpty() && param != null && !param.isEmpty() && domain.endsWith(matchDomain)) {
			Matcher matcher = matchRegex.matcher(param);
			if (matcher.matches()) {
				String id = matcher.group(matchGroup);
				if (id != null && !id.isEmpty()) {
                    try {
                    	// special handle
	                    // 1. + to %2b since URLDecoder.decode will change + to space
	                    // 2. %26 to %2526 since URLDecoder.decode will change %26 to &
                    	id = id.replaceAll("\\+", "%2b");
                    	id = id.replaceAll("%26", "%2526");
						return URLDecoder.decode(id, StandardCharsets.UTF_8.name());
					} catch (Exception ignored) {}
				}
			}
		}
		return null;
	}
	
	private String extractQueryParam(String queryParamStr, String idKey) {
		if (queryParamStr != null && !queryParamStr.isEmpty() && idKey != null && !idKey.isEmpty()) {
            // split by &
            String[] queryParams = queryParamStr.split("&");
			for (String param : queryParams) {
				// split by =
				String[] keyValuePairs = param.split("=");
				if (keyValuePairs.length == 2 && keyValuePairs[0].equalsIgnoreCase(idKey)) {
					if (keyValuePairs[1] != null && !keyValuePairs[1].isEmpty()) return keyValuePairs[1];
				}
			}
		}
		return null;
	}
	
	private String convertSpecialChar(String value) {
		if (value != null && !value.isEmpty()) value = value.replaceAll("%26", "&");
		if (value != null && !value.isEmpty()) value = value.replaceAll("%C2%AE", "®");
		if (value != null && !value.isEmpty()) value = value.replaceAll("\\r?\\n", "");
		return value;
	}
	
	
	/* Website Icon */
	public void setWebsiteIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, true);
		else setIconInactive(imageView, true, R.attr.website_image_website_alpha);
	}
	
	public void setWikiIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, true);
		else setIconInactive(imageView, true, R.attr.website_image_wiki_alpha);
	}
	
	public void setFacebookIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_facebook_alpha);
	}
	
	public void setIgIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_ig_alpha);
	}
	
	public void setTwitterIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_twitter_alpha);
	}
	
	public void setOpenriceIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_openrice_alpha);
	}
	
	public void setIosIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, true);
		else setIconInactive(imageView, true, R.attr.website_image_ios_alpha);
	}
	
	public void setAndroidIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_android_alpha);
	}
	
	public void setSteamIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_steam_alpha);
	}
	
	public void setPsIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_ps_alpha);
	}
	
	public void setXboxIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_xbox_alpha);
	}
	
	public void setSwitchIcon(ImageView imageView, boolean active) {
		if (active) setIconActive(imageView, false);
		else setIconInactive(imageView, false, R.attr.website_image_switch_alpha);
	}
	
	private void setIconActive(ImageView imageView, boolean tint) {
		if (!tint) {
			ColorMatrix matrix = new ColorMatrix();
			matrix.setSaturation(1f);
			imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
		}
		imageView.setAlpha(1f);
	}
	
	private void setIconInactive(ImageView imageView, boolean tint, int alphaAttrId) {
		if (tint) {
			int tintColor = attrUtils.getAttrColorInt(R.attr.textNormalColor);
			imageView.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
		}
		else {
			ColorMatrix matrix = new ColorMatrix();
			matrix.setSaturation(0.1f);
			imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
		}
		
		float alpha = attrUtils.getAttrFloat(alphaAttrId);
		imageView.setAlpha(alpha);
	}
	
	
	/* Check Website on Server */
	public SearchResult<RedCompanySimple> checkWebsite(WebsiteSearchCriteria criteria) {
		return checkWebsiteWebService.checkWebsite(criteria);
	}
	
}
