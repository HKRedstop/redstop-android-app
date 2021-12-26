package com.ntredize.redstop.db.model;

public class AndroidDeviceSearchResult {
	
	private SearchResult<RedCompanySimple> deviceRedCompanyList;
	private SearchResult<RedCompanySimple> appRedCompanyList;
	private SearchResult<RedCompanySimple> caCertRedCompanyList;
	
	public SearchResult<RedCompanySimple> getDeviceRedCompanyList() {
		return deviceRedCompanyList;
	}
	
	public void setDeviceRedCompanyList(SearchResult<RedCompanySimple> deviceRedCompanyList) {
		this.deviceRedCompanyList = deviceRedCompanyList;
	}
	
	public SearchResult<RedCompanySimple> getAppRedCompanyList() {
		return appRedCompanyList;
	}
	
	public void setAppRedCompanyList(SearchResult<RedCompanySimple> appRedCompanyList) {
		this.appRedCompanyList = appRedCompanyList;
	}
	
	public SearchResult<RedCompanySimple> getCaCertRedCompanyList() {
		return caCertRedCompanyList;
	}
	
	public void setCaCertRedCompanyList(SearchResult<RedCompanySimple> caCertRedCompanyList) {
		this.caCertRedCompanyList = caCertRedCompanyList;
	}
}
