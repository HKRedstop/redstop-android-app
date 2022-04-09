package com.ntredize.redstop.db.model;

import java.io.Serializable;
import java.util.List;

public class RedCompanyDetail implements Serializable {

    private String companyCode;
    private String displayName;
    private String displaySubName;
    private String fullNameHk;
    private String fullNameEn;
    private List<RedCompanyDetailDesc> descs;
    private List<RedCompanyDetailDesc> stockInfos;
    private String redStarType;
    private Integer redStar;
    private String motherCode;
    private String groupCode;
    private Integer displayOrder;
    private String updateDate;
    private List<String> androidPackages;
    private List<String> caCertOrganizations;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplaySubName() {
        return displaySubName;
    }

    public void setDisplaySubName(String displaySubName) {
        this.displaySubName = displaySubName;
    }

    public String getFullNameHk() {
        return fullNameHk;
    }

    public void setFullNameHk(String fullNameHk) {
        this.fullNameHk = fullNameHk;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }
    
    public List<RedCompanyDetailDesc> getDescs() {
        return descs;
    }
    
    public void setDescs(List<RedCompanyDetailDesc> descs) {
        this.descs = descs;
    }
    
    public List<RedCompanyDetailDesc> getStockInfos() {
        return stockInfos;
    }
    
    public void setStockInfos(List<RedCompanyDetailDesc> stockInfos) {
        this.stockInfos = stockInfos;
    }
    
    public String getRedStarType() {
        return redStarType;
    }
    
    public void setRedStarType(String redStarType) {
        this.redStarType = redStarType;
    }
    
    public Integer getRedStar() {
        return redStar;
    }

    public void setRedStar(Integer redStar) {
        this.redStar = redStar;
    }

    public String getMotherCode() {
        return motherCode;
    }

    public void setMotherCode(String motherCode) {
        this.motherCode = motherCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public String getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    
    public List<String> getAndroidPackages() {
        return androidPackages;
    }
    
    public void setAndroidPackages(List<String> androidPackages) {
        this.androidPackages = androidPackages;
    }
    
    public List<String> getCaCertOrganizations() {
        return caCertOrganizations;
    }
    
    public void setCaCertOrganizations(List<String> caCertOrganizations) {
        this.caCertOrganizations = caCertOrganizations;
    }
}
