package com.ntredize.redstop.db.model;

import java.io.Serializable;

public class AndroidDeviceCaCert implements Serializable {

    private String organization;
    private String commonName;
    
    public AndroidDeviceCaCert(String organization, String commonName) {
        this.organization = organization;
        this.commonName = commonName;
    }
    
    public String getOrganization() {
        return organization;
    }
    
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    public String getCommonName() {
        return commonName;
    }
    
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
}
