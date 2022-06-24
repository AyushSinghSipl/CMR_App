
package com.mahyco.cmr_app.model.getVendorListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VendorListResponse implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("records")
    @Expose
    private Records records;
    @SerializedName("msg")
    @Expose
    private String msg;
    private final static long serialVersionUID = -1485273151299676810L;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Records getRecords() {
        return records;
    }

    public void setRecords(Records records) {
        this.records = records;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public class Records implements Serializable {

        @SerializedName("Table1")
        @Expose
        private List<Table1> table1 = null;
        private final static long serialVersionUID = 6551827533729849944L;

        public List<Table1> getTable1() {
            return table1;
        }

        public void setTable1(List<Table1> table1) {
            this.table1 = table1;
        }

    }

    public class Table1 implements Serializable {

        @SerializedName("grid")
        @Expose
        private Integer grid;
        @SerializedName("GrCode")
        @Expose
        private String grCode;
        @SerializedName("VillageName")
        @Expose
        private String villageName;
        @SerializedName("GrowerName")
        @Expose
        private String growerName;
        @SerializedName("VendorCode")
        @Expose
        private String vendorCode;
        private final static long serialVersionUID = 1269196599815845649L;

        public Integer getGrid() {
            return grid;
        }

        public void setGrid(Integer grid) {
            this.grid = grid;
        }

        public String getGrCode() {
            return grCode;
        }

        public void setGrCode(String grCode) {
            this.grCode = grCode;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public String getGrowerName() {
            return growerName;
        }

        public void setGrowerName(String growerName) {
            this.growerName = growerName;
        }

        public String getVendorCode() {
            return vendorCode;
        }

        public void setVendorCode(String vendorCode) {
            this.vendorCode = vendorCode;
        }


    }
}
