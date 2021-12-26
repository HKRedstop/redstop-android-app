package com.ntredize.redstop.db.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RedCompanyGroupSearchCriteria implements Serializable {

    private Integer page;
    private String groupCode;
    private String selfCompanyCode;

    public RedCompanyGroupSearchCriteria(String groupCode, String selfCompanyCode) {
        this.page = 1;
        this.groupCode = groupCode;
        this.selfCompanyCode = selfCompanyCode;
    }

    public Map<String, String> toUrlQueryItems() {
        String queryPage = page != null ? page.toString() : "1";

        Map<String, String> map = new HashMap<>();
        map.put("page", queryPage);
        map.put("groupCode", groupCode);
        map.put("selfCompanyCode", selfCompanyCode);
        return map;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getSelfCompanyCode() {
        return selfCompanyCode;
    }

    public void setSelfCompanyCode(String selfCompanyCode) {
        this.selfCompanyCode = selfCompanyCode;
    }
}
