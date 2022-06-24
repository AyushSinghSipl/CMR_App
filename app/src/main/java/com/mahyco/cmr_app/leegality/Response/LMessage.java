package com.mahyco.cmr_app.leegality.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LMessage {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LMessage{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
