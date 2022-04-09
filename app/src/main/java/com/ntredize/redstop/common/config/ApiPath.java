package com.ntredize.redstop.common.config;

public class ApiPath {
	
	/* API Query Key */
	public final static String API_QUERY_KEY_ANDROID_PACKAGE = "androidPackage";
	public final static String API_QUERY_KEY_CA_CERT = "caCert";
	

	/* API Param */
	public final static String API_PARAM_FRIEND_CODE = "{friendCode}";
	public final static String API_PARAM_COMPANY_CODE = "{companyCode}";
	public final static String API_PARAM_GROUP_CODE = "{groupCode}";


	/* API Path */
	// preload
	public final static String API_RES_PRELOAD = "/preload";
	public final static String API_PATH_PRELOAD_ANDROID_DATA = "/data/android/v1";

	// red company
	public final static String API_RES_RED_COMPANY = "/redCompany";
	public final static String API_PATH_RED_COMPANY_SEARCH = "/search/v3";
	public final static String API_PATH_RED_COMPANY_LIST_BY_SUB_CATEGORY = "/subCategory/v3";
	public final static String API_PATH_RED_COMPANY_DETAIL = "/detail/v4/" + API_PARAM_COMPANY_CODE;
	public final static String API_PATH_RED_COMPANY_LIST_BY_GROUP = "/group/v3";
	
	// device
	public final static String API_RES_DEVICE = "/device";
	public final static String API_PATH_DEVICE_CHECK_ANDROID = "/android/check/v4";
	
	// extension
	public final static String API_RES_EXTENSION = "/extension";
	public final static String API_PATH_EXTENSION_CHECK = "/check/v3";

	// image
	private final static String SUFFIX_FRIEND_CODE_DARK = "_d";
	
	private final static String SUFFIX_IMAGE_FRIEND = ".png";
	private final static String SUFFIX_IMAGE_RED_COMPANY = ".jpg";
	
	public final static String API_PATH_IMAGE_FRIEND = "/friend/" + API_PARAM_FRIEND_CODE + SUFFIX_IMAGE_FRIEND;
	public final static String API_PATH_IMAGE_FRIEND_DARK = "/friend/" + API_PARAM_FRIEND_CODE + SUFFIX_FRIEND_CODE_DARK + SUFFIX_IMAGE_FRIEND;
	public final static String API_PATH_IMAGE_FRIEND_THUMBNAIL = "/friend-thumbnail/" + API_PARAM_FRIEND_CODE + SUFFIX_IMAGE_FRIEND;
	public final static String API_PATH_IMAGE_FRIEND_THUMBNAIL_DARK = "/friend-thumbnail/" + API_PARAM_FRIEND_CODE + SUFFIX_FRIEND_CODE_DARK + SUFFIX_IMAGE_FRIEND;
	public final static String API_PATH_IMAGE_RED_COMPANY = "/red-company/" + API_PARAM_GROUP_CODE + "/" + API_PARAM_COMPANY_CODE + SUFFIX_IMAGE_RED_COMPANY;
	public final static String API_PATH_IMAGE_RED_COMPANY_THUMBNAIL = "/red-company-thumbnail/" + API_PARAM_GROUP_CODE + "/" + API_PARAM_COMPANY_CODE + SUFFIX_IMAGE_RED_COMPANY;
	
}
