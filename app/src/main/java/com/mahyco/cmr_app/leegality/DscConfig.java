package com.mahyco.cmr_app.leegality;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DscConfig {

    @SerializedName("verifyName")
    @Expose
    private Boolean verifyName;
    @SerializedName("verifySmartName")
    @Expose
    private Boolean verifySmartName;
    @SerializedName("verifyPincode")
    @Expose
    private String verifyPincode;
    @SerializedName("verifyState")
    @Expose
    private String verifyState;

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

    public String getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(String verifyState) {
        this.verifyState = verifyState;
    }
}