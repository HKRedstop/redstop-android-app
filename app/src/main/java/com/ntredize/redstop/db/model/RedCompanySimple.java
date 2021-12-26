package com.ntredize.redstop.db.model;

import java.io.Serializable;

public class RedCompanySimple implements Serializable {

    private String companyCode;
    private String displayName;
    private String displaySubName;
    private String redStarType;
    private Integer redStar;
    private Integer displayOrder;
    private String groupCode;

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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
