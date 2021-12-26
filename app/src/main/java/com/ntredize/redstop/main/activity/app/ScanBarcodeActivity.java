package com.ntredize.redstop.main.activity.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.ntredize.redstop.R;
import com.ntredize.redstop.common.config.PermissionRequestCode;
import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.main.dialog.ScanBarcodeResultDialog;
import com.ntredize.redstop.support.service.ScanBarcodeService;
import com.ntredize.redstop.support.service.ThemeService;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

public class ScanBarcodeActivity extends ActivityBase {
	
	// service
	private ThemeService themeService;
	private ScanBarcodeService scanBarcodeService;
	
	// view
	private PreviewView previewView;
	
	// data
	private boolean canScan;
	
	// camera
	private CameraSelector cameraSelector;
	private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
	private Camera camera;
	private BarcodeScanner detector;
	
	
	/* Init */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Start Activity");
		
		initService();
		initTheme();
		initData();
		initView();
		checkCameraPermission();
	}
	
	@Override
	protected void initService() {
		themeService = new ThemeService(this);
		scanBarcodeService = new ScanBarcodeService(this);
	}
	
	@Override
	protected void initTheme() {
		themeService.setupTheme(true);
	}
	
	@Override
	protected void initData() {}
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_scan_barcode);
		
		// title
		setTitle(R.string.label_scan_barcode);
		
		// preview view
		this.previewView = findViewById(R.id.scan_barcode_preview_view);
	}
	
	
	/* On Pause and Resume */
	@SuppressLint({"ClickableViewAccessibility", "RestrictedApi"})
	@Override
	public void onPause() {
		super.onPause();
		canScan = false;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		canScan = true;
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
	
	
	/* Camera Permission */
	private void checkCameraPermission() {
		if (this.checkPermission(Manifest.permission.CAMERA, PermissionRequestCode.SCAN_BARCODE_CAMERA)) initCamera();
	}
	
	protected void failFromPermission(int requestCode) {
		if (requestCode == PermissionRequestCode.SCAN_BARCODE_CAMERA) {
			Runnable backAction = this::finish;
			errorHandling(new ApplicationException(this, ApplicationException.SCAN_ERROR), backAction);
		}
	}
	
	protected void returnFromPermission(int requestCode) {
		if (requestCode == PermissionRequestCode.SCAN_BARCODE_CAMERA) initCamera();
	}
	
	
	/* Handle Camera */
	@SuppressLint("ClickableViewAccessibility")
	private void initCamera() {
		// camera selector
		cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
		
		// barcode detector
		BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
				.setBarcodeFormats(Barcode.FORMAT_EAN_13, Barcode.FORMAT_UPC_A)
				.build();
		detector = BarcodeScanning.getClient(options);
		
		// camera provider
		cameraProviderFuture = ProcessCameraProvider.getInstance(this);
		cameraProviderFuture.addListener(() -> {
			try {
				ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
				
				// camera preview
				Preview preview = new Preview.Builder().build();
				
				// barcode analyzer
				ImageAnalysis imageAnalysis = null;
				if (canScan) {
					imageAnalysis = new ImageAnalysis.Builder()
							.setTargetResolution(new Size(640, 480))
							.setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
							.build();
					imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), buildCustomBarcodeAnalyzer());
				}
				
				// life cycle
				camera = (imageAnalysis != null) ? cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview) :
						cameraProvider.bindToLifecycle(this, cameraSelector, preview);
				preview.setSurfaceProvider(previewView.getSurfaceProvider());
			} catch (Exception e) {
				Log.e(TAG, Objects.requireNonNull(e.getMessage()));
			}
		}, ContextCompat.getMainExecutor(this));
		
		// tap to focus
		previewView.setOnTouchListener((view, motionEvent) -> {
			if (camera == null || motionEvent.getAction() != MotionEvent.ACTION_DOWN) return false;
			
			MeteringPointFactory factory = previewView.getMeteringPointFactory();
			MeteringPoint point = factory.createPoint(motionEvent.getX(), motionEvent.getY());
			FocusMeteringAction action = new FocusMeteringAction.Builder(point).build();
			camera.getCameraControl().startFocusAndMetering(action);
			
			return true;
		});
	}
	
	private ImageAnalysis.Analyzer buildCustomBarcodeAnalyzer() {
		return imageProxy -> {
			try {
				if (!canScan) {
					imageProxy.close();
					return;
				}

				// image
				@SuppressLint("UnsafeOptInUsageError") Image mediaImage = imageProxy.getImage();
				if (mediaImage == null) {
					imageProxy.close();
					return;
				}
				InputImage visionImage = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

				// detector
				if (detector != null) {
					detector.process(visionImage).addOnSuccessListener(barcodes -> {
						if (canScan && barcodes.size() > 0) {
							canScan = false;
							handleBarcode(barcodes.get(0));
						}
					}).addOnCompleteListener(tasks -> {
						mediaImage.close();
						imageProxy.close();
					});
				}
			} catch (Exception e) {
				Log.e(TAG, Objects.requireNonNull(e.getMessage()));
			}
		};
	}
	
	private void handleBarcode(Barcode firebaseBarcode) {
		// barcode
		String barcode = firebaseBarcode.getRawValue();
		
		// valid barcode
		if (scanBarcodeService.validateBarcodeFormat(barcode)) {
			// dialog
			String country = scanBarcodeService.findCountryByBarcode(barcode);
			AlertDialog scanResultDialog = new ScanBarcodeResultDialog(this, country).buildDialog();
			scanResultDialog.setOnDismissListener(dialogInterface -> canScan = true);
			if (!isFinishing()) scanResultDialog.show();
		}
		
		// invalid barcode
		else this.errorHandling(new ApplicationException(this, ApplicationException.INVALID_BARCODE_FORMAT));
	}
	
}
