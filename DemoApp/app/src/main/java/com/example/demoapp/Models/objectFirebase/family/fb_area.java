package com.example.demoapp.Models.objectFirebase.family;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class fb_area implements Serializable {

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("radius")
    @Expose
    private Integer radius;
    @SerializedName("regionName")
    @Expose
    private String regionName;

    public fb_area() {
    }

    public fb_area(Double latitude, Double longitude, Integer radius, String regionName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.regionName = regionName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
