package com.mahyco.cmr_app.model.getcenterlistresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCenterListResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("records")
    @Expose
    private List<RecordCenter> records = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<RecordCenter> getRecords() {
        return records;
    }

    public void setRecords(List<RecordCenter> records) {
        this.records = records;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
