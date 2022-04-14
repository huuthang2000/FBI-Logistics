package com.example.demoapp.Models.objectFirebase.drivingDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class fb_drivingDetail implements Serializable {

    @SerializedName("averageSpeed")
    @Expose
    private fb_averageSpeed averageSpeed;
    @SerializedName("timeUpdateTopSpeed")
    @Expose
    private long timeUpdateTopSpeed;
    @SerializedName("topSpeed")
    @Expose
    private String topSpeed;

    public fb_drivingDetail() {
    }

    public fb_drivingDetail(fb_averageSpeed averageSpeed, long timeUpdateTopSpeed, String topSpeed) {
        this.averageSpeed = averageSpeed;
        this.timeUpdateTopSpeed = timeUpdateTopSpeed;
        this.topSpeed = topSpeed;
    }

    public fb_drivingDetail(List<Double> listAverageSpeed, long timeUpdateTopSpeed, String topSpeed) {
        this.averageSpeed = new fb_averageSpeed(listAverageSpeed,Calendar.getInstance().getTimeInMillis());
        this.timeUpdateTopSpeed = timeUpdateTopSpeed;
        this.topSpeed = topSpeed;
    }

    public fb_drivingDetail(List<Double> listAverageSpeed, long timeUpdateAverageSpeed, long timeUpdateTopSpeed, String topSpeed) {
        this.averageSpeed = new fb_averageSpeed(listAverageSpeed,timeUpdateAverageSpeed);
        this.timeUpdateTopSpeed = timeUpdateTopSpeed;
        this.topSpeed = topSpeed;
    }


    public fb_drivingDetail(List<Double> listAverageSpeed, String topSpeed) {
        this.averageSpeed = new fb_averageSpeed(listAverageSpeed,Calendar.getInstance().getTimeInMillis());
        this.timeUpdateTopSpeed = Calendar.getInstance().getTimeInMillis();
        this.topSpeed = topSpeed;
    }

    public fb_averageSpeed getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(fb_averageSpeed averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public long getTimeUpdateTopSpeed() {
        return timeUpdateTopSpeed;
    }

    public void setTimeUpdateTopSpeed(long timeUpdateTopSpeed) {
        this.timeUpdateTopSpeed = timeUpdateTopSpeed;
    }

    public String getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(String topSpeed) {
        this.topSpeed = topSpeed;
    }
}
