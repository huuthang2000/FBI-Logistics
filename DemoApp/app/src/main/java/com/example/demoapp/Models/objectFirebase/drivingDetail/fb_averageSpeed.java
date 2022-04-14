package com.example.demoapp.Models.objectFirebase.drivingDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class fb_averageSpeed {

    @SerializedName("listAverageSpeed")
    @Expose
    private List<Double> listAverageSpeed = null;
    @SerializedName("timeUpdateAverageSpeed")
    @Expose
    private long timeUpdateAverageSpeed;

    public fb_averageSpeed() {
    }



    public fb_averageSpeed(List<Double> listAverageSpeed, long timeUpdateAverageSpeed) {
        this.listAverageSpeed = listAverageSpeed;
        this.timeUpdateAverageSpeed = timeUpdateAverageSpeed;
    }

    public List<Double> getListAverageSpeed() {
        return listAverageSpeed;
    }

    public void setListAverageSpeed(List<Double> listAverageSpeed) {
        this.listAverageSpeed = listAverageSpeed;
    }

    public long getTimeUpdateAverageSpeed() {
        return timeUpdateAverageSpeed;
    }

    public void setTimeUpdateAverageSpeed(long timeUpdateAverageSpeed) {
        this.timeUpdateAverageSpeed = timeUpdateAverageSpeed;
    }

}
