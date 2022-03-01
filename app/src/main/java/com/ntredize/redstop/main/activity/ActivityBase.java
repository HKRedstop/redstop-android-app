package com.ntredize.redstop.main.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.main.dialog.WarningDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public abstract class ActivityBase extends AppCompatActivity {
	
	public final String TAG = this.getClass().getName();
	
	
	/* Main */
	protected abstract void initService();
	
	protected abstract void initTheme();
	
	protected abstract void initData();
	
	protected abstract void initView();


	/* Close App */
	public void closeApp() {
		finishAffinity();
		System.exit(0);
	}
	
	
	/* Layout */
	public int getScreenWidth() {
		return getScreenSize().x;
	}
	
	public int getScreenHeight() {
		return getScreenSize().y - getStatusBarHeight() - getActionBarHeight();
	}
	
	private Point getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}
	
	public int getStatusBarHeight() {
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			return getResources().getDimensionPixelSize(resourceId);
		} else {
			return 0;
		}
	}
	
	public int getActionBarHeight() {
		if (getSupportActionBar() != null) {
			return getSupportActionBar().getHeight();
		} else if (getActionBar() != null) {
			return getActionBar().getHeight();
		} else {
			return 0;
		}
	}
	
	protected boolean isLandscape() {
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}
	
	
	/* Screen Image */
	public void setScreenImage(ImageView imageView, int id) {
		try {
			// change from resources to bitmap
			Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), id);
			
			// calculate crop size
			int screenWidth = getScreenWidth();
			int screenHeight = getScreenHeight();
			BigDecimal screenHeightToWidthRatio = new BigDecimal(screenHeight).divide(new BigDecimal(screenWidth), 4, RoundingMode.HALF_UP);
			
			int imageWidth = originalBitmap.getWidth();
			int imageHeight = originalBitmap.getHeight();
			BigDecimal imageHeightToWidthRatio = new BigDecimal(imageHeight).divide(new BigDecimal(imageWidth), 4, RoundingMode.HALF_UP);
			
			int croppedX;
			int croppedY;
			int croppedWidth;
			int croppedHeight;
			
			if (imageHeightToWidthRatio.compareTo(screenHeightToWidthRatio) < 0) {
				// crop x
				croppedWidth = new BigDecimal(imageHeight).divide(screenHeightToWidthRatio, 0, RoundingMode.HALF_UP).intValue();
				croppedX = (imageWidth - croppedWidth) / 2;
				
				croppedHeight = imageHeight;
				croppedY = 0;
			} else {
				// crop y
				croppedHeight = new BigDecimal(imageWidth).multiply(screenHeightToWidthRatio).intValue();
				croppedY = (imageHeight - croppedHeight) / 2;
				
				croppedWidth = imageWidth;
				croppedX = 0;
			}
			
			// crop image
			Bitmap croppedBitmap = Bitmap.createBitmap(originalBitmap, croppedX, croppedY, croppedWidth, croppedHeight);
			imageView.setImageBitmap(croppedBitmap);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			
			// have throw resource not found error, catch it if image cannot load
			try {
				imageView.setImageResource(id);
			} catch (Exception e2) {
				Log.e(TAG, e2.getMessage(), e2);
			}
		}
	}
	
	
	/* Keyboard */
	public void clearFocusAndHideKeyboard(View view) {
		view.clearFocus();
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	
	/* Action Bar */
	public void showBackMenuItem() {
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	protected void setHomeButtonEnable() {
		if (getSupportActionBar() != null) {
			getSupportActionBar().setHomeButtonEnabled(true);
		} else if (getActionBar() != null) {
			getActionBar().setHomeButtonEnabled(true);
		}
	}
	
	protected void setNavigatorBarColorById(int colorId) {
		if (getWindow() != null) getWindow().setStatusBarColor(ContextCompat.getColor(this, colorId));
	}
	
	
	/* Fragment */
	protected void showFragment(int fragmentContainerId, Fragment f) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(fragmentContainerId, f);
		transaction.commitAllowingStateLoss();
	}
	
	protected void refreshFragment(Fragment f) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.detach(f);
		transaction.attach(f);
		transaction.commitAllowingStateLoss();
	}
	
	
	/* Error Handling */
	public void errorHandling(Exception e) {
		errorHandling(e, null);
	}
	
	public void errorHandling(Exception e, Runnable action) {
		Log.e(TAG, e.getMessage(), e);
		
		String msg;
		if (e instanceof ApplicationException) {
			msg = e.getMessage();
		} else {
			msg = new ApplicationException(this, ApplicationException.SYSTEM_ERROR).getMessage();
		}
		
		AlertDialog warningDialog = new WarningDialog(this, msg, action).buildDialog();
		if (!isFinishing()) warningDialog.show();
	}
	
	
	/* Permission */
	public boolean checkPermission(String permission, int requestCode) {
		if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		// If request is cancelled, the result arrays are empty.
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
			returnFromPermission(requestCode);
		else failFromPermission(requestCode);
	}
	
	// for override
	protected void returnFromPermission(int requestCode) {
	}
	
	// for override
	protected void failFromPermission(int requestCode) {
	}
	
	
	/* Open URL */
	public void openUrl(String url) {
		try {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		} catch (Exception e) {
			Log.e(TAG, "Fail to open url");
			Log.e(TAG, e.getMessage(), e);
			errorHandling(new ApplicationException(this, ApplicationException.VIEW_URL_ERROR));
		}
	}
	
	
	/* Send Text or Image */
	public void sendTextMsg(String msg) {
		try {
			Intent i = new Intent();
			i.setAction(Intent.ACTION_SEND);
			i.putExtra(Intent.EXTRA_TEXT, msg);
			i.setType("text/plain");
			
			startActivity(Intent.createChooser(i, getString(R.string.share_intent_title)));
		} catch (Exception e) {
			Log.e(TAG, "Fail to share");
			Log.e(TAG, e.getMessage(), e);
			errorHandling(new ApplicationException(this, ApplicationException.SHARE_ERROR));
		}
	}
	
	public void sendImageMsg(Uri imageUri, String msg) {
		new Thread(() -> {
			
			try {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_SEND);
				i.setType("image/jpg");
				i.putExtra(Intent.EXTRA_STREAM, imageUri);
				i.putExtra(Intent.EXTRA_TEXT, msg);
				
				startActivity(Intent.createChooser(i, getString(R.string.share_intent_title)));
			} catch (Exception e) {
				Log.e(TAG, "Fail to send photo");
				Log.e(TAG, e.getMessage(), e);
				errorHandling(new ApplicationException(this, ApplicationException.SHARE_ERROR));
			}
			
		}).start();
	}
	
	
	/* Send Email */
	public void sendEmail(String to) {
		try {
			Intent i = new Intent(Intent.ACTION_SENDTO);
			i.setData(Uri.parse("mailto:" + to));
			startActivity(i);
		} catch (Exception e) {
			Log.e(TAG, "Fail to send email");
			Log.e(TAG, e.getMessage(), e);
			errorHandling(new ApplicationException(this, ApplicationException.SEND_EMAIL_ERROR));
		}
	}
	
}
