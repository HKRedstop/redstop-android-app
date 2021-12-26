package com.ntredize.redstop.db.model;

import java.io.Serializable;

public class RedCompanyDetailDesc implements Serializable {

	private String content;
	private Integer color;
	private String companyCode;
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCompanyCode() {
		return companyCode;
	}
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	public Integer getColor() {
		return color;
	}
	
	public void setColor(Integer color) {
		this.color = color;
	}
}