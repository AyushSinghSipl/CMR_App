package com.mahyco.cmr_app.leegality.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeegalityMainResponse {
    @SerializedName("data")
    @Expose
    private LData data;
    @SerializedName("messages")
    @Expose
    private List<LMessage> messages = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public LData getData() {
        return data;
    }

    public void setData(LData data) {
        this.data = data;
    }

    public List<LMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<LMessage> messages) {
        this.messages = messages;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LeegalityMainResponse{" +
                "data=" + data +
                ", messages=" + messages +
                ", status=" + status +
                '}';
    }
}
