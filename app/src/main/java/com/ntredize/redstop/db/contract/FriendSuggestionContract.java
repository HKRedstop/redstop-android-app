package com.ntredize.redstop.db.contract;

public class FriendSuggestionContract {
	
	/* Table */
	public final static String TABLE_NAME = "FRIEND_SUGGESTION";
	
	
	/* Column */
	public final static String COLUMN_FRIEND_CODE = "FRIEND_CODE";
	public final static String COLUMN_CATEGORY_NAME = "CATEGORY_NAME";
	public final static String COLUMN_NAME = "NAME";
	public final static String COLUMN_REMARK = "REMARK";
	public final static String COLUMN_URL = "URL";
	public final static String COLUMN_OPEN_STORE = "OPEN_STORE";
	public final static String COLUMN_DISPLAY_ORDER = "DISPLAY_ORDER";


	/* SQL for Create Table */
	public final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
			+ COLUMN_FRIEND_CODE + " TEXT PRIMARY KEY"
			+ ", " + COLUMN_CATEGORY_NAME + " TEXT"
			+ ", " + COLUMN_NAME + " TEXT"
			+ ", " + COLUMN_REMARK + " TEXT"
			+ ", " + COLUMN_URL + " TEXT"
			+ ", " + COLUMN_OPEN_STORE + " TEXT"
			+ ", " + COLUMN_DISPLAY_ORDER + " INTEGER"
			+ ")";
	
	
	/* All Columns */
	public final static String[] ALL_COLUMNS = {
			COLUMN_FRIEND_CODE,
			COLUMN_CATEGORY_NAME,
			COLUMN_NAME,
			COLUMN_REMARK,
			COLUMN_URL,
			COLUMN_OPEN_STORE,
			COLUMN_DISPLAY_ORDER
	};
	
}
