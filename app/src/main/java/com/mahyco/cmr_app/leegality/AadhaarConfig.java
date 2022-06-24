package com.mahyco.cmr_app.leegality;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AadhaarConfig {

    @SerializedName("verifyName")
    @Expose
    private Boolean verifyName;
    @SerializedName("verifySmartName")
    @Expose
    private Boolean verifySmartName;
    @SerializedName("verifyPincode")
    @Expose
    private String verifyPincode;
    @SerializedName("verifyYob")
    @Expose
    private Integer verifyYob;
    @SerializedName("verifyTitle")
    @Expose
    private String verifyTitle;
    @SerializedName("verifyState")
    @Expose
    private String verifyState;
    @SerializedName("verifyGender")
    @Expose
    private String verifyGender;

    public Boolean getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(Boolean verifyName) {
        this.verifyName = verifyName;
    }

    public Boolean getVerifySmartName() {
        return verifySmartName;
    }

    public void setVerifySmartName(Boolean verifySmartName) {
        this.verifySmartName = verifySmartName;
    }

    public String getVerifyPincode() {
        return verifyPincode;
    }

    public void setVerifyPincode(String verifyPincode) {
        this.verifyPincode = verifyPincode;
    }

    public Integer getVerifyYob() {
        return verifyYob;
    }

    public void setVerifyYob(Integer verifyYob) {
        this.verifyYob = verifyYob;
    }

    public String getVerifyTitle() {
        return verifyTitle;
    }

    public void setVerifyTitle(String verifyTitle) {
        this.verifyTitle = verifyTitle;
    }

    public String getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(String verifyState) {
        this.verifyState = verifyState;
    }

    public String getVerifyGender() {
        return verifyGender;
    }

    public void setVerifyGender(String verifyGender) {
        this.verifyGender = verifyGender;
    }

}



