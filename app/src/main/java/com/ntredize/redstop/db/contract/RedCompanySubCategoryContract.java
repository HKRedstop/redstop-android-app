package com.ntredize.redstop.db.contract;

public class RedCompanySubCategoryContract {
	
	/* Table Name */
	public final static String TABLE_NAME = "RED_COMPANY_SUB_CATEGORY";
	
	
	/* Column */
	public final static String COLUMN_SUB_CATEGORY_CODE = "SUB_CATEGORY_CODE";
	public final static String COLUMN_CATEGORY_CODE = "CATEGORY_CODE";
	public final static String COLUMN_NAME = "NAME";
	public final static String COLUMN_DISPLAY_ORDER = "DISPLAY_ORDER";


	/* SQL for Create Table */
	public final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
			+ COLUMN_SUB_CATEGORY_CODE + " TEXT PRIMARY KEY"
			+ ", " + COLUMN_CATEGORY_CODE + " TEXT"
			+ ", " + COLUMN_NAME + " TEXT"
			+ ", " + COLUMN_DISPLAY_ORDER + " INTEGER"
			+ ")";
	
	
	/* All Columns */
	public final static String[] ALL_COLUMNS = {
			COLUMN_SUB_CATEGORY_CODE,
			COLUMN_CATEGORY_CODE,
			COLUMN_NAME,
			COLUMN_DISPLAY_ORDER
	};
	
}
