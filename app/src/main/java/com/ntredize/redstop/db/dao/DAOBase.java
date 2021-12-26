package com.ntredize.redstop.db.dao;

import android.content.Context;
import android.database.Cursor;

public class DAOBase {
	
	private final static String STR_TRUE = "TRUE";
	private final static String STR_FALSE = "FALSE";
	
	protected Context context;
	protected String tableName;
	
	
	/* Init */
	protected DAOBase(Context context, String tableName) {
		this.context = context;
		this.tableName = tableName;
	}
	
	
	/* Get Value from Select */
	protected String getString(Cursor cursor, String column) {
		int index = cursor.getColumnIndexOrThrow(column);
		if (cursor.isNull(index)) return null;
		else return cursor.getString(index);
	}
	
	protected Integer getInt(Cursor cursor, String column) {
		int index = cursor.getColumnIndexOrThrow(column);
		if (cursor.isNull(index)) return null;
		else return cursor.getInt(index);
	}
	
	protected Boolean getBoolean(Cursor cursor, String column) {
		int index = cursor.getColumnIndexOrThrow(column);
		if (cursor.isNull(index)) return null;
		else return STR_TRUE.equalsIgnoreCase(cursor.getString(index));
	}
	
	
	/* Convert Data for DB */
	protected String booleanToStr(Boolean booleanValue) {
		return booleanValue ? STR_TRUE : STR_FALSE;
	}
	
}
