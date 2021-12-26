package com.ntredize.redstop.db.dao.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ntredize.redstop.db.contract.RedCompanyCategoryContract;
import com.ntredize.redstop.db.dao.DAOBase;
import com.ntredize.redstop.db.helper.DBHelper;
import com.ntredize.redstop.db.model.RedCompanyCategory;

import java.util.ArrayList;
import java.util.List;

public class RedCompanyCategoryDAO extends DAOBase {
	
	/* Init */
	public RedCompanyCategoryDAO(Context context) {
		super(context, RedCompanyCategoryContract.TABLE_NAME);
	}


	/* Insert */
	public void insert(List<RedCompanyCategory> redCompanyCategoryList) {
		SQLiteDatabase writableDB = DBHelper.getInstance(context).getWritableDatabase();

		for (RedCompanyCategory redCompanyCategory : redCompanyCategoryList) {
			ContentValues values = new ContentValues();
			values.put(RedCompanyCategoryContract.COLUMN_CATEGORY_CODE, redCompanyCategory.getCategoryCode());
			values.put(RedCompanyCategoryContract.COLUMN_NAME, redCompanyCategory.getName());
			values.put(RedCompanyCategoryContract.COLUMN_DISPLAY_ORDER, redCompanyCategory.getDisplayOrder());

			writableDB.insert(RedCompanyCategoryContract.TABLE_NAME, null, values);
		}
	}


	/* Delete */
	public void deleteAll() {
		SQLiteDatabase writableDB = DBHelper.getInstance(context).getWritableDatabase();
		writableDB.delete(RedCompanyCategoryContract.TABLE_NAME, null, null);
	}
	
	
	/* Select */
	public RedCompanyCategory select(String categoryCode) {
		// where case
		String selection = RedCompanyCategoryContract.COLUMN_CATEGORY_CODE + " = ?";
		String[] selectionArgs = { categoryCode };

		// db query
		SQLiteDatabase readableDB = DBHelper.getInstance(context).getReadableDatabase();
		Cursor cursor = readableDB.query(
				tableName, RedCompanyCategoryContract.ALL_COLUMNS,
				selection, selectionArgs, null, null, null
		);

		// result
		RedCompanyCategory category = null;

		if (cursor.moveToFirst()) {
			category = getRedCompanyCategoryModelFromCursor(cursor);
		}

		cursor.close();
		return category;
	}

	public List<RedCompanyCategory> selectAll() {
		// set sort order
		String sortOrder = RedCompanyCategoryContract.COLUMN_DISPLAY_ORDER;
		
		// db query
		SQLiteDatabase readableDB = DBHelper.getInstance(context).getReadableDatabase();
		Cursor cursor = readableDB.query(
				tableName, RedCompanyCategoryContract.ALL_COLUMNS,
				null, null, null, null, sortOrder
		);
		
		// result
		List<RedCompanyCategory> list = new ArrayList<>();
		
		while (cursor.moveToNext()) {
			list.add(getRedCompanyCategoryModelFromCursor(cursor));
		}
		
		cursor.close();
		return list;
	}
	
	
	/* Build Model */
	private RedCompanyCategory getRedCompanyCategoryModelFromCursor(Cursor cursor) {
		RedCompanyCategory redCompanyCategory = new RedCompanyCategory();
		
		redCompanyCategory.setCategoryCode(getString(cursor, RedCompanyCategoryContract.COLUMN_CATEGORY_CODE));
		redCompanyCategory.setName(getString(cursor, RedCompanyCategoryContract.COLUMN_NAME));
		redCompanyCategory.setDisplayOrder(getInt(cursor, RedCompanyCategoryContract.COLUMN_DISPLAY_ORDER));
		
		return redCompanyCategory;
	}
	
}
