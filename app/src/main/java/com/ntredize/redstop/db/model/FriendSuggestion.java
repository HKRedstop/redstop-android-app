package com.ntredize.redstop.db.model;

import java.io.Serializable;

public class FriendSuggestion implements Serializable {

    private String friendCode;
    private String name;
    private String categoryName;
    private String remark;
    private String url;
    private Boolean openStore;
    private Integer displayOrder;

    public String getFriendCode() {
        return friendCode;
    }

    public void setFriendCode(String friendCode) {
        this.friendCode = friendCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getOpenStore() {
        return openStore;
    }

    public void setOpenStore(Boolean openStore) {
        this.openStore = openStore;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
