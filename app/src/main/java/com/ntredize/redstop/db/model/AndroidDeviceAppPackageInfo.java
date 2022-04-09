package com.ntredize.redstop.db.model;

import java.io.Serializable;

public class AndroidDeviceAppPackageInfo implements Serializable {
    
    private String name;
    private String androidPackage;
    
    public AndroidDeviceAppPackageInfo(String name, String androidPackage) {
        this.name = name;
        this.androidPackage = androidPackage;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAndroidPackage() {
        return androidPackage;
    }
    
    public void setAndroidPackage(String androidPackage) {
        this.androidPackage = androidPackage;
    }
}
