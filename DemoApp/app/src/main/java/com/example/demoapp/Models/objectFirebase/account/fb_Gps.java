package com.example.demoapp.Models.objectFirebase.account;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class fb_Gps {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("timeUpdate")
    @Expose
    private long timeUpdate;

    public fb_Gps() {
    }

    public fb_Gps(Boolean status, long timeUpdate) {
        this.status = status;
        this.timeUpdate = timeUpdate;
    }

    public fb_Gps(Boolean status) {
        this.status = status;
        this.timeUpdate = Calendar.getInstance().getTimeInMillis();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(long timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

}