package com.mahyco.cmr_app.model.vendordetailsforcontract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VDContractResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("records")
    @Expose
    private VendorRecord records;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public VendorRecord getRecords() {
        return records;
    }

    public void setRecords(VendorRecord records) {
        this.records = records;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
