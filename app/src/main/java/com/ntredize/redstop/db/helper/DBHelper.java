package com.ntredize.redstop.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ntredize.redstop.db.contract.FriendSuggestionContract;
import com.ntredize.redstop.db.contract.RedCompanyCategoryContract;
import com.ntredize.redstop.db.contract.RedCompanySubCategoryContract;

public class DBHelper extends SQLiteOpenHelper {
	
	private final static int DATABASE_VERSION = 12;
	public final static String DATABASE_NAME = "redstop.db";
	
	private static DBHelper dbHelper;
	
	
	/* Instance */
	public static synchronized DBHelper getInstance(Context context) {
		if (dbHelper == null) openConnection(context);
		return dbHelper;
	}
	
	
	/* Init */
	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	
	/* On Create, Upgrade and Downgrade */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(RedCompanyCategoryContract.SQL_CREATE_TABLE);
		db.execSQL(RedCompanySubCategoryContract.SQL_CREATE_TABLE);
		db.execSQL(FriendSuggestionContract.SQL_CREATE_TABLE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 12) {
			db.execSQL(RedCompanyCategoryContract.SQL_CREATE_TABLE);
			db.execSQL(RedCompanySubCategoryContract.SQL_CREATE_TABLE);
			db.execSQL(FriendSuggestionContract.SQL_CREATE_TABLE);
		}
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	
	/* Open Connection */
	private static void openConnection(Context context) {
		dbHelper = new DBHelper(context);
	}
	
}
