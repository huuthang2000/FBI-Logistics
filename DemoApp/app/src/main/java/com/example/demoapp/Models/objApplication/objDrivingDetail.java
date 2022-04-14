package com.example.demoapp.Models.objApplication;


import com.example.demoapp.Models.objectFirebase.drivingDetail.fb_averageSpeed;
import com.example.demoapp.Models.objectFirebase.drivingDetail.fb_drivingDetail;

import java.util.List;

public class objDrivingDetail extends fb_drivingDetail {

    private String id;
    private int week;
    private int year;


    public objDrivingDetail(String id, int week, int year, List<Double> listAverageSpeed, String topSpeed) {
        super(listAverageSpeed, topSpeed);
        this.id = id;
        this.week = week;
        this.year = year;
    }

    public objDrivingDetail(String id, int week, int year, fb_drivingDetail drivingDetail) {
        super(drivingDetail.getAverageSpeed(), drivingDetail.getTimeUpdateTopSpeed(), drivingDetail.getTopSpeed());
        this.id = id;
        this.week = week;
        this.year = year;
    }

    public objDrivingDetail(String id, int week, int year, fb_averageSpeed averageSpeed, long timeUpdateTopSpeed, String topSpeed) {
        super(averageSpeed, timeUpdateTopSpeed, topSpeed);
        this.id = id;
        this.week = week;
        this.year = year;
    }


    @Override
    public void setAverageSpeed(fb_averageSpeed averageSpeed) {
        super.setAverageSpeed(averageSpeed);
    }

    @Override
    public long getTimeUpdateTopSpeed() {
        return super.getTimeUpdateTopSpeed();
    }

    @Override
    public void setTimeUpdateTopSpeed(long timeUpdateTopSpeed) {
        super.setTimeUpdateTopSpeed(timeUpdateTopSpeed);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String getTopSpeed() {
        return super.getTopSpeed();
    }

    @Override
    public void setTopSpeed(String topSpeed) {
        super.setTopSpeed(topSpeed);
    }
}
