package com.mahyco.cmr_app.model.getcenterlistresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordCenter {

    @SerializedName("user_code")
    @Expose
    private String userCode;
    @SerializedName("cntr_code")
    @Expose
    private String cntrCode;
    @SerializedName("cntr_Desc")
    @Expose
    private String cntrDesc;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCntrCode() {
        return cntrCode;
    }

    public void setCntrCode(String cntrCode) {
        this.cntrCode = cntrCode;
    }

    public String getCntrDesc() {
        return cntrDesc;
    }

    public void setCntrDesc(String cntrDesc) {
        this.cntrDesc = cntrDesc;
    }

}
