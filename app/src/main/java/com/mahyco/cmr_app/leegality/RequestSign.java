package com.mahyco.cmr_app.leegality;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestSign {
    @SerializedName("profileId")
    @Expose
    private String profileId = "";
    @SerializedName("file")
    @Expose
    private File file = null;
    @SerializedName("stampSeries")
    @Expose
    private String stampSeries;
    @SerializedName("invitees")
    @Expose
    private List<Invitee> invitees = null;
    @SerializedName("irn")
    @Expose
    private String irn;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getStampSeries() {
        return stampSeries;
    }

    public void setStampSeries(String stampSeries) {
        this.stampSeries = stampSeries;
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Invitee> invitees) {
        this.invitees = invitees;
    }

    public String getIrn() {
        return irn;
    }

    public void setIrn(String irn) {
        this.irn = irn;
    }

}


