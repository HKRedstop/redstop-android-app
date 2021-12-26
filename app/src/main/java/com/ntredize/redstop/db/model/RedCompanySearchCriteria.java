package com.ntredize.redstop.db.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RedCompanySearchCriteria implements Serializable {

    private Integer page;
    private String keyword;
    private String sorting;

    public RedCompanySearchCriteria(String keyword, String sorting) {
        this.page = 1;
        this.keyword = keyword;
        this.sorting = sorting;
    }

    public Map<String, String> toUrlQueryItems() {
        String queryPage = page != null ? page.toString() : "1";

        Map<String, String> map = new HashMap<>();
        map.put("page", queryPage);
        map.put("keyword", keyword);
        map.put("sorting", sorting);
        return map;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getSorting() {
        return sorting;
    }
    
    public void setSorting(String sorting) {
        this.sorting = sorting;
    }
}
