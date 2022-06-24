package com.mahyco.cmr_app.leegality.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LData {
    @SerializedName("documentId")
    @Expose
    private String documentId;
    @SerializedName("irn")
    @Expose
    private Object irn;
    @SerializedName("invitees")
    @Expose
    private List<LInvitee> invitees = null;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Object getIrn() {
        return irn;
    }

    public void setIrn(Object irn) {
        this.irn = irn;
    }

    public List<LInvitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<LInvitee> invitees) {
        this.invitees = invitees;
    }

    @Override
    public String toString() {
        return "LData{" +
                "documentId='" + documentId + '\'' +
                ", irn=" + irn +
                ", invitees=" + invitees +
                '}';
    }
}
