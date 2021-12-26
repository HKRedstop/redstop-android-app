package com.ntredize.redstop.db.model;

import java.util.List;

public class AndroidDeviceSearchCriteria {
	
	private String manufacturer;
	private List<String> androidPackageList;
	private List<String> caCertOrganizationList;
	
	public AndroidDeviceSearchCriteria(String manufacturer, List<String> androidPackageList, List<String> caCertOrganizationList) {
		this.manufacturer = manufacturer;
		this.androidPackageList = androidPackageList;
		this.caCertOrganizationList = caCertOrganizationList;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public List<String> getAndroidPackageList() {
		return androidPackageList;
	}
	
	public void setAndroidPackageList(List<String> androidPackageList) {
		this.androidPackageList = androidPackageList;
	}
	
	public List<String> getCaCertOrganizationList() {
		return caCertOrganizationList;
	}
	
	public void setCaCertOrganizationList(List<String> caCertOrganizationList) {
		this.caCertOrganizationList = caCertOrganizationList;
	}
}
