package com.ntredize.redstop.support.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.db.model.AndroidDeviceAppPackageInfo;
import com.ntredize.redstop.db.model.AndroidDeviceCaCert;
import com.ntredize.redstop.db.model.AndroidDeviceSearchCriteria;
import com.ntredize.redstop.db.model.AndroidDeviceSearchResult;
import com.ntredize.redstop.support.webservice.CheckDeviceWebService;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CheckDeviceService {
	
	public final String TAG = this.getClass().getName();
	
	private final Context context;
	private final CheckDeviceWebService checkDeviceWebService;
	
	
	/* Init */
	public CheckDeviceService(Context context) {
		this.context = context;
		this.checkDeviceWebService = new CheckDeviceWebService(context);
	}
	
	
	/* Check Android Device on Server */
	public AndroidDeviceSearchResult checkAndroidDevice(List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos, List<AndroidDeviceCaCert> androidDeviceCaCerts) throws ApplicationException {
		String manufacturer = getDeviceManufacturer();
		List<String> androidPackages = getDeviceAppAndroidPackages(androidDeviceAppPackageInfos);
		List<String> caCertOrganizations = convertCaCertsToOrganizations(androidDeviceCaCerts);
		return checkDeviceWebService.checkAndroidDevice(new AndroidDeviceSearchCriteria(manufacturer, androidPackages, caCertOrganizations));
	}
	
	
	/* Get Device Manufacturer */
	private String getDeviceManufacturer() {
		return Build.MANUFACTURER;
	}
	
	
	/* Scan Device Package */
	public List<AndroidDeviceAppPackageInfo> getDeviceAppPackageInfos() {
		PackageManager pm = context.getPackageManager();
		@SuppressLint("QueryPermissionsNeeded") List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
		
		List<AndroidDeviceAppPackageInfo> list = new ArrayList<>();
		for (PackageInfo packageInfo : packageInfos) {
			if (packageInfo.packageName != null && packageInfo.applicationInfo.enabled) {
				String name = (String) pm.getApplicationLabel(packageInfo.applicationInfo);
				String androidPackage = packageInfo.packageName;
				list.add(new AndroidDeviceAppPackageInfo(name, androidPackage));
			}
		}
		return list;
	}
	
	private List<String> getDeviceAppAndroidPackages(List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos) {
		List<String> names = new ArrayList<>();
		if (androidDeviceAppPackageInfos != null) {
			for (AndroidDeviceAppPackageInfo androidDeviceAppPackageInfo : androidDeviceAppPackageInfos) {
				names.add(androidDeviceAppPackageInfo.getAndroidPackage());
			}
		}
		return names;
	}
	
	public List<AndroidDeviceAppPackageInfo> filterRedCompanyAndroidDeviceAppPackageInfos(List<AndroidDeviceAppPackageInfo> androidDeviceAppPackageInfos, List<String> redCompanyAndroidPackages) {
		List<AndroidDeviceAppPackageInfo> list = new ArrayList<>();
		if (androidDeviceAppPackageInfos != null) {
			for (AndroidDeviceAppPackageInfo androidDeviceAppPackageInfo : androidDeviceAppPackageInfos) {
				if (redCompanyAndroidPackages.contains(androidDeviceAppPackageInfo.getAndroidPackage()))
					list.add(androidDeviceAppPackageInfo);
			}
		}
		return list;
	}
	
	public Drawable getDeviceAppLogo(String androidPackage) {
		PackageManager pm = context.getPackageManager();
		try {
			return pm.getApplicationIcon(androidPackage);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/* Scan CA Cert */
	public List<AndroidDeviceCaCert> getDeviceCaCerts() {
		try {
			List<AndroidDeviceCaCert> list = new ArrayList<>();
			KeyStore ks = KeyStore.getInstance("AndroidCAStore");
			
			if (ks != null) {
				ks.load(null, null);
				Enumeration<String> aliases = ks.aliases();
				
				while (aliases.hasMoreElements()) {
					String alias = aliases.nextElement();
					X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
					String organization = null;
					String commonName = null;
					
					// subject
					if (cert.getSubjectX500Principal() != null) {
						String caCertName = cert.getSubjectX500Principal().getName();
						String caCertOrganization = extractOrganizationFromCaCert(caCertName);
						String caCertCommonName = extractCommonNameFromCaCert(caCertName);
						if (caCertOrganization != null && !caCertOrganization.isEmpty()) organization = caCertOrganization;
						if (caCertCommonName != null && !caCertCommonName.isEmpty()) commonName = caCertCommonName;
					}
					
					// issuer
					if (cert.getIssuerX500Principal() != null) {
						String caCertName = cert.getIssuerX500Principal().getName();
						String caCertOrganization = extractOrganizationFromCaCert(caCertName);
						String caCertCommonName = extractCommonNameFromCaCert(caCertName);
						if (organization == null && caCertOrganization != null && !caCertOrganization.isEmpty()) organization = caCertOrganization;
						if (commonName == null && caCertCommonName != null && !caCertCommonName.isEmpty()) commonName = caCertCommonName;
					}
					
					if (organization != null) list.add(new AndroidDeviceCaCert(organization, commonName));
				}
			}
			
			return list;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return new ArrayList<>();
		}
	}
	
	private String extractOrganizationFromCaCert(String caCertName) {
		// Key of Organization is O
		return extractItemFromCaCert(caCertName, "O");
	}
	
	private String extractCommonNameFromCaCert(String caCertName) {
		// Key of Organization is CN
		return extractItemFromCaCert(caCertName, "CN");
	}
	
	private String extractItemFromCaCert(String caCertName, String itemKey) {
		if (caCertName == null) return null;
		
		// format: key1=value1,key2=value2,key3=value3,...
		String[] keyValuePairs = caCertName.split(",");
		
		for (String keyValuePair : keyValuePairs) {
			String[] keyValuePairSplit = keyValuePair.split("=");
			
			if (keyValuePairSplit.length == 2) {
				String key = keyValuePairSplit[0].trim();
				String value = keyValuePairSplit[1].trim();
				
				if (itemKey.equalsIgnoreCase(key)) {
					// remove all \ in value to prevent break the json
					return value.replaceAll("\\\\", "");
				}
			}
		}
		
		return null;
	}
	
	private List<String> convertCaCertsToOrganizations(List<AndroidDeviceCaCert> androidDeviceCaCerts) {
		List<String> organizations = new ArrayList<>();
		if (androidDeviceCaCerts != null) {
			for (AndroidDeviceCaCert androidDeviceCaCert : androidDeviceCaCerts) {
				organizations.add(androidDeviceCaCert.getOrganization());
			}
		}
		return organizations;
	}
	
	public List<AndroidDeviceCaCert> filterRedCompanyAndroidDeviceCaCerts(List<AndroidDeviceCaCert> androidDeviceCaCerts, List<String> redCompanyCaCertOrganizations) {
		List<AndroidDeviceCaCert> list = new ArrayList<>();
		if (androidDeviceCaCerts != null) {
			for (AndroidDeviceCaCert androidDeviceCaCert : androidDeviceCaCerts) {
				if (redCompanyCaCertOrganizations.contains(androidDeviceCaCert.getOrganization()))
					list.add(androidDeviceCaCert);
			}
		}
		return list;
	}
	
}
