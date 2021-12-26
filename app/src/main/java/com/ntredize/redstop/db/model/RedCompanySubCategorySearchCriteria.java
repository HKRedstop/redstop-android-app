package com.ntredize.redstop.db.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RedCompanySubCategorySearchCriteria implements Serializable {

    private Integer page;
    private String subCategoryCode;
    private String sorting;

    public RedCompanySubCategorySearchCriteria(String subCategoryCode, String sorting) {
        this.page = 1;
        this.subCategoryCode = subCategoryCode;
        this.sorting = sorting;
    }

    public Map<String, String> toUrlQueryItems() {
        String queryPage = page != null ? page.toString() : "1";

        Map<String, String> map = new HashMap<>();
        map.put("page", queryPage);
        map.put("subCategoryCode", subCategoryCode);
        map.put("sorting", sorting);
        return map;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }
    
    public String getSorting() {
        return sorting;
    }
    
    public void setSorting(String sorting) {
        this.sorting = sorting;
    }
}
