package com.ntredize.redstop.db.model;

import java.io.Serializable;
import java.util.List;

public class PreloadData implements Serializable {

    private List<RedCompanyCategory> redCompanyCategories;
    private List<RedCompanySubCategory> redCompanySubCategories;
    private List<FriendSuggestion> friends;

    public List<RedCompanyCategory> getRedCompanyCategories() {
        return redCompanyCategories;
    }

    public void setRedCompanyCategories(List<RedCompanyCategory> redCompanyCategories) {
        this.redCompanyCategories = redCompanyCategories;
    }

    public List<RedCompanySubCategory> getRedCompanySubCategories() {
        return redCompanySubCategories;
    }

    public void setRedCompanySubCategories(List<RedCompanySubCategory> redCompanySubCategories) {
        this.redCompanySubCategories = redCompanySubCategories;
    }

    public List<FriendSuggestion> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendSuggestion> friends) {
        this.friends = friends;
    }
}
