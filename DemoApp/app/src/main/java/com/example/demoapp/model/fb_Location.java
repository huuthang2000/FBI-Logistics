package com.example.demoapp.model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class fb_Location {

    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("time_update")
    @Expose
    private long timeUpdate;

    public fb_Location() {
    }

    public fb_Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeUpdate = Calendar.getInstance().getTimeInMillis();
    }

    public fb_Location(double latitude, double longitude, long timeUpdate) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeUpdate = timeUpdate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(long timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

}