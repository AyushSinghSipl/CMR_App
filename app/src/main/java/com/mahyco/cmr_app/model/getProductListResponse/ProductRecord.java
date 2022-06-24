package com.mahyco.cmr_app.model.getProductListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductRecord {

    @SerializedName("prd_code")
    @Expose
    private String prdCode;
    @SerializedName("Prd_desc1")
    @Expose
    private String prdDesc1;

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getPrdDesc1() {
        return prdDesc1;
    }

    public void setPrdDesc1(String prdDesc1) {
        this.prdDesc1 = prdDesc1;
    }
}
