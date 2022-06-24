package com.mahyco.cmr_app.leegality;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invitee {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("organizationName")
    @Expose
    private String organizationName;
    @SerializedName("aadhaarConfig")
    @Expose
    private AadhaarConfig aadhaarConfig;
    @SerializedName("dscConfig")
    @Expose
    private DscConfig dscConfig;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public AadhaarConfig getAadhaarConfig() {
        return aadhaarConfig;
    }

    public void setAadhaarConfig(AadhaarConfig aadhaarConfig) {
        this.aadhaarConfig = aadhaarConfig;
    }

    public DscConfig getDscConfig() {
        return dscConfig;
    }

    public void setDscConfig(DscConfig dscConfig) {
        this.dscConfig = dscConfig;
    }
}
