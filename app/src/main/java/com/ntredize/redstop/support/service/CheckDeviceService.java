package com.ntredize.redstop.support.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.db.model.AndroidDeviceSearchCriteria;
import com.ntredize.redstop.db.model.AndroidDeviceSearchResult;
import com.ntredize.redstop.support.webservice.CheckDeviceWebService;

import java.security.KeyStore;
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
	public AndroidDeviceSearchResult checkAndroidDevice() throws ApplicationException {
		String manufacturer = getDeviceManufacturer();
		List<String> androidPackageList = getDeviceAppPackageNames();
		List<String> caCertOrganizationList = getDeviceCaCertOrganizations();
		return checkDeviceWebService.checkAndroidDevice(new AndroidDeviceSearchCriteria(manufacturer, androidPackageList, caCertOrganizationList));
	}
	
	
	/* Get Device Manufacturer */
	private String getDeviceManufacturer() {
		return Build.MANUFACTURER;
	}
	
	
	/* Scan Device Package */
	private List<String> getDeviceAppPackageNames() {
		@SuppressLint("QueryPermissionsNeeded") List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		
		List<String> names = new ArrayList<>();
		for (PackageInfo info : packages) {
			if (info.packageName != null && info.applicationInfo.enabled) names.add(info.packageName);
		}
		return names;
	}
	
	
	/* Scan CA Cert */
	private List<String> getDeviceCaCertOrganizations() {
		try {
			List<String> caCertOrganizations = new ArrayList<>();
			KeyStore ks = KeyStore.getInstance("AndroidCAStore");
			
			if (ks != null) {
				ks.load(null, null);
				Enumeration<String> aliases = ks.aliases();
				
				while (aliases.hasMoreElements()) {
					String alias = aliases.nextElement();
					java.security.cert.X509Certificate cert = (java.security.cert.X509Certificate) ks.getCertificate(alias);
					
					// subject
					if (cert != null && cert.getSubjectX500Principal() != null) {
						String organization = extractOrganizationFromCaCert(cert.getSubjectX500Principal().getName());
						if (organization != null && !organization.isEmpty() && !caCertOrganizations.contains(organization)) caCertOrganizations.add(organization);
					}
					
					// issuer
					if (cert != null && cert.getIssuerX500Principal() != null) {
						String organization = extractOrganizationFromCaCert(cert.getIssuerX500Principal().getName());
						if (organization != null && !organization.isEmpty() && !caCertOrganizations.contains(organization)) caCertOrganizations.add(organization);
					}
				}
			}
			
			return caCertOrganizations;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
	}
	
	private String extractOrganizationFromCaCert(String caCertName) {
		if (caCertName == null) return null;
		
		// format: key1=value1,key2=value2,key3=value3,...
		// Key of Organization is O
		String[] keyValuePairs = caCertName.split(",");
		
		for (String keyValuePair : keyValuePairs) {
			String[] keyValuePairSplit = keyValuePair.split("=");
			
			if (keyValuePairSplit.length == 2) {
				String key = keyValuePairSplit[0].trim();
				String value = keyValuePairSplit[1].trim();
				
				if ("O".equalsIgnoreCase(key)) {
					// remove all \ in value to prevent break the json
					return value.replaceAll("\\\\", "");
				}
			}
		}
		
		return null;
	}
	
}
