package com.ntredize.redstop.main.activity.redcompany;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanyCategory;
import com.ntredize.redstop.db.model.RedCompanySubCategory;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.adapter.MyFormListDivider;
import com.ntredize.redstop.main.adapter.redcompany.RedCompanySubCategoryAdapter;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;
import com.ntredize.redstop.support.service.RedCompanyService;
import com.ntredize.redstop.support.service.ThemeService;

import java.util.List;

public class RedCompanySubCategoryListActivity extends ActivityBase {
	
	public static final String KEY_RED_COMPANY_CATEGORY_CODE = "KEY_RED_COMPANY_CATEGORY_CODE";
	
	// service
	private ThemeService themeService;
	private RedCompanyService redCompanyService;
	
	// data
	private RedCompanyCategory category;
	private List<RedCompanySubCategory> list;
	
	
	/* Init */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Start Activity");
		
		initService();
		initTheme();
		initData();
		initView();
	}
	
	@Override
	protected void initService() {
		themeService = new ThemeService(this);
		redCompanyService = new RedCompanyService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(true);
	}
	
	@Override
	protected void initData() {
		// category
		String categoryCode = getIntent().getStringExtra(KEY_RED_COMPANY_CATEGORY_CODE);
		category = redCompanyService.getRedCompanyCategoryByCategoryCode(categoryCode);
		
		// sub category list
		list = redCompanyService.getRedCompanySubCategoryListByCategoryCode(categoryCode);
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_red_company_sub_category_list);
		
		// title
		setTitle(category.getName());
		
		// recycler view
		MyRecyclerView recyclerView = findViewById(R.id.red_company_sub_category_recycler_view);
		recyclerView.setAdapter(new RedCompanySubCategoryAdapter(this, list));
		
		MyFormListDivider myFormListDivider = new MyFormListDivider(this, MyFormListDivider.VERTICAL);
		recyclerView.addItemDecoration(myFormListDivider);
	}
	
	
	/* Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		showBackMenuItem();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	
	/* Open Company List */
	public void openRedCompanyList(RedCompanySubCategory subCategory) {
		Intent i = new Intent(this, RedCompanyListActivity.class);
		i.putExtra(RedCompanyListActivity.KEY_RED_COMPANY_SUB_CATEGORY_CODE, subCategory.getSubCategoryCode());
		startActivity(i);
	}
	
}
