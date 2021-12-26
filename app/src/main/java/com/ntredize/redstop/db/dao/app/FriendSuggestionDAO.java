package com.ntredize.redstop.db.dao.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ntredize.redstop.db.contract.FriendSuggestionContract;
import com.ntredize.redstop.db.dao.DAOBase;
import com.ntredize.redstop.db.helper.DBHelper;
import com.ntredize.redstop.db.model.FriendSuggestion;

import java.util.ArrayList;
import java.util.List;

public class FriendSuggestionDAO extends DAOBase {

	/* Init */
	public FriendSuggestionDAO(Context context) {
		super(context, FriendSuggestionContract.TABLE_NAME);
	}


	/* Insert */
	public void insert(List<FriendSuggestion> friendSuggestionList) {
		SQLiteDatabase writableDB = DBHelper.getInstance(context).getWritableDatabase();

		for (FriendSuggestion friendSuggestion : friendSuggestionList) {
			ContentValues values = new ContentValues();
			values.put(FriendSuggestionContract.COLUMN_FRIEND_CODE, friendSuggestion.getFriendCode());
			values.put(FriendSuggestionContract.COLUMN_NAME, friendSuggestion.getName());
			values.put(FriendSuggestionContract.COLUMN_CATEGORY_NAME, friendSuggestion.getCategoryName());
			values.put(FriendSuggestionContract.COLUMN_REMARK, friendSuggestion.getRemark());
			values.put(FriendSuggestionContract.COLUMN_URL, friendSuggestion.getUrl());
			values.put(FriendSuggestionContract.COLUMN_OPEN_STORE, booleanToStr(friendSuggestion.getOpenStore()));
			values.put(FriendSuggestionContract.COLUMN_DISPLAY_ORDER, friendSuggestion.getDisplayOrder());

			writableDB.insert(FriendSuggestionContract.TABLE_NAME, null, values);
		}
	}


	/* Delete */
	public void deleteAll() {
		SQLiteDatabase writableDB = DBHelper.getInstance(context).getWritableDatabase();
		writableDB.delete(FriendSuggestionContract.TABLE_NAME, null, null);
	}
	
	
	/* Select */
	public List<FriendSuggestion> selectAll() {
		// set sort order
		String sortOrder = FriendSuggestionContract.COLUMN_DISPLAY_ORDER;
		
		// db query
		SQLiteDatabase readableDB = DBHelper.getInstance(context).getReadableDatabase();
		Cursor cursor = readableDB.query(
				tableName, FriendSuggestionContract.ALL_COLUMNS,
				null, null, null, null, sortOrder
		);
		
		// result
		List<FriendSuggestion> list = new ArrayList<>();
		
		while (cursor.moveToNext()) {
			list.add(getFriendSuggestionModelFromCursor(cursor));
		}
		
		cursor.close();
		return list;
	}
	
	
	/* Build Model */
	private FriendSuggestion getFriendSuggestionModelFromCursor(Cursor cursor) {
		FriendSuggestion friendSuggestion = new FriendSuggestion();

		friendSuggestion.setFriendCode(getString(cursor, FriendSuggestionContract.COLUMN_FRIEND_CODE));
		friendSuggestion.setName(getString(cursor, FriendSuggestionContract.COLUMN_NAME));
		friendSuggestion.setCategoryName(getString(cursor, FriendSuggestionContract.COLUMN_CATEGORY_NAME));
		friendSuggestion.setRemark(getString(cursor, FriendSuggestionContract.COLUMN_REMARK));
		friendSuggestion.setUrl(getString(cursor, FriendSuggestionContract.COLUMN_URL));
		friendSuggestion.setOpenStore(getBoolean(cursor, FriendSuggestionContract.COLUMN_OPEN_STORE));
		friendSuggestion.setDisplayOrder(getInt(cursor, FriendSuggestionContract.COLUMN_DISPLAY_ORDER));

		return friendSuggestion;
	}
	
}
