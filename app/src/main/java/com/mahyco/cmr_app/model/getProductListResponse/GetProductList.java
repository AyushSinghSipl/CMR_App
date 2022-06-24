package com.mahyco.cmr_app.model.getProductListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductList {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("records")
    @Expose
    private List<ProductRecord> records = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ProductRecord> getRecords() {
        return records;
    }

    public void setRecords(List<ProductRecord> records) {
        this.records = records;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
