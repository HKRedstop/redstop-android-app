package com.ntredize.redstop.support.service;

import android.content.Context;

import com.ntredize.redstop.db.dao.app.FriendSuggestionDAO;
import com.ntredize.redstop.db.model.FriendSuggestion;

import java.util.List;

public class FriendSuggestionService {

    private final FriendSuggestionDAO friendSuggestionDAO;


    /* Init */
    public FriendSuggestionService(Context context) {
        this.friendSuggestionDAO = new FriendSuggestionDAO(context);
    }


    /* Friends */
    void insertFriendSuggestion(List<FriendSuggestion> list) {
        friendSuggestionDAO.insert(list);
    }

    void deleteAllFriendSuggestion() {
        friendSuggestionDAO.deleteAll();
    }

    public List<FriendSuggestion> getAllFriendSuggestion() {
        return friendSuggestionDAO.selectAll();
    }

}
