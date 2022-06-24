package com.mahyco.cmr_app.leegality.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LInvitee {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("signUrl")
    @Expose
    private String signUrl;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("expiryDate")
    @Expose
    private String expiryDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "LInvitee{" +
                "name='" + name + '\'' +
                ", email=" + email +
                ", phone='" + phone + '\'' +
                ", signUrl='" + signUrl + '\'' +
                ", active=" + active +
                ", expiryDate='" + expiryDate + '\'' +
                '}';
    }
}
