package com.ntredize.redstop.main.activity.redcompany;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ntredize.redstop.R;
import com.ntredize.redstop.db.model.RedCompanyCategory;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.adapter.MyFormListDivider;
import com.ntredize.redstop.main.adapter.redcompany.RedCompanyCategoryAdapter;
import com.ntredize.redstop.main.view.recyclerview.MyRecyclerView;
import com.ntredize.redstop.support.service.RedCompanyService;
import com.ntredize.redstop.support.service.ThemeService;

import java.util.List;

public class RedCompanyCategoryListActivity extends ActivityBase {
	
	// service
	private ThemeService themeService;
	private RedCompanyService redCompanyService;
	
	// data
	private List<RedCompanyCategory> list;
	
	
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
		// category list
		list = redCompanyService.getAllRedCompanyCategoryList();
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_red_company_category_list);
		
		// title
		setTitle(R.string.label_category_company);
		
		// recycler view
		MyRecyclerView recyclerView = findViewById(R.id.red_company_category_recycler_view);
		recyclerView.setAdapter(new RedCompanyCategoryAdapter(this, list));
		
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
	
	
	/* Open Sub Category List */
	public void openRedCompanySubCategoryList(RedCompanyCategory category) {
		Intent i = new Intent(this, RedCompanySubCategoryListActivity.class);
		i.putExtra(RedCompanySubCategoryListActivity.KEY_RED_COMPANY_CATEGORY_CODE, category.getCategoryCode());
		startActivity(i);
	}
	
}
