package com.ntredize.redstop.db.dao.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ntredize.redstop.db.contract.RedCompanySubCategoryContract;
import com.ntredize.redstop.db.dao.DAOBase;
import com.ntredize.redstop.db.helper.DBHelper;
import com.ntredize.redstop.db.model.RedCompanySubCategory;

import java.util.ArrayList;
import java.util.List;

public class RedCompanySubCategoryDAO extends DAOBase {
	
	/* Init */
	public RedCompanySubCategoryDAO(Context context) {
		super(context, RedCompanySubCategoryContract.TABLE_NAME);
	}


	/* Insert */
	public void insert(List<RedCompanySubCategory> redCompanySubCategoryList) {
		SQLiteDatabase writableDB = DBHelper.getInstance(context).getWritableDatabase();

		for (RedCompanySubCategory redCompanySubCategory : redCompanySubCategoryList) {
			ContentValues values = new ContentValues();
			values.put(RedCompanySubCategoryContract.COLUMN_SUB_CATEGORY_CODE, redCompanySubCategory.getSubCategoryCode());
			values.put(RedCompanySubCategoryContract.COLUMN_CATEGORY_CODE, redCompanySubCategory.getCategoryCode());
			values.put(RedCompanySubCategoryContract.COLUMN_NAME, redCompanySubCategory.getName());
			values.put(RedCompanySubCategoryContract.COLUMN_DISPLAY_ORDER, redCompanySubCategory.getDisplayOrder());

			writableDB.insert(RedCompanySubCategoryContract.TABLE_NAME, null, values);
		}
	}


	/* Delete */
	public void deleteAll() {
		SQLiteDatabase writableDB = DBHelper.getInstance(context).getWritableDatabase();
		writableDB.delete(RedCompanySubCategoryContract.TABLE_NAME, null, null);
	}
	
	
	/* Select */
	public RedCompanySubCategory select(String subCategoryCode) {
		// where case
		String selection = RedCompanySubCategoryContract.COLUMN_SUB_CATEGORY_CODE + " = ?";
		String[] selectionArgs = { subCategoryCode };

		// db query
		SQLiteDatabase readableDB = DBHelper.getInstance(context).getReadableDatabase();
		Cursor cursor = readableDB.query(
				tableName, RedCompanySubCategoryContract.ALL_COLUMNS,
				selection, selectionArgs, null, null, null
		);

		// result
		RedCompanySubCategory subCategory = null;

		if (cursor.moveToFirst()) {
			subCategory = getRedCompanySubCategoryModelFromCursor(cursor);
		}

		cursor.close();
		return subCategory;
	}

	public List<RedCompanySubCategory> selectAll(String categoryCode) {
		// set sort order
		String sortOrder = RedCompanySubCategoryContract.COLUMN_DISPLAY_ORDER;
		
		// where case
		String selection = RedCompanySubCategoryContract.COLUMN_CATEGORY_CODE + " = ?";
		String[] selectionArgs = {categoryCode};
		
		// db query
		SQLiteDatabase readableDB = DBHelper.getInstance(context).getReadableDatabase();
		Cursor cursor = readableDB.query(
				tableName, RedCompanySubCategoryContract.ALL_COLUMNS,
				selection, selectionArgs, null, null, sortOrder
		);
		
		// result
		List<RedCompanySubCategory> list = new ArrayList<>();
		
		while (cursor.moveToNext()) {
			list.add(getRedCompanySubCategoryModelFromCursor(cursor));
		}
		
		cursor.close();
		return list;
	}
	
	
	/* Build Model */
	private RedCompanySubCategory getRedCompanySubCategoryModelFromCursor(Cursor cursor) {
		RedCompanySubCategory redCompanySubCategory = new RedCompanySubCategory();
		
		redCompanySubCategory.setSubCategoryCode(getString(cursor, RedCompanySubCategoryContract.COLUMN_SUB_CATEGORY_CODE));
		redCompanySubCategory.setCategoryCode(getString(cursor, RedCompanySubCategoryContract.COLUMN_CATEGORY_CODE));
		redCompanySubCategory.setName(getString(cursor, RedCompanySubCategoryContract.COLUMN_NAME));
		redCompanySubCategory.setDisplayOrder(getInt(cursor, RedCompanySubCategoryContract.COLUMN_DISPLAY_ORDER));
		
		return redCompanySubCategory;
	}
	
}
