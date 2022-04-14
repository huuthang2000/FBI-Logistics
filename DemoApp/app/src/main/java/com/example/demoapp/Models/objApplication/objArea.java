package com.example.demoapp.Models.objApplication;

import com.example.demoapp.Models.objectFirebase.family.fb_area;

public class objArea extends fb_area {
    private String id;
    private String idFamily;

    public objArea()
    {
    }

    public objArea(String id, fb_area area) {
        super(area.getLatitude(), area.getLongitude(), area.getRadius(), area.getRegionName());
        this.id = id;
    }

    public objArea(String id, Double latitude, Double longitude, Integer radius, String regionName) {
        super(latitude, longitude, radius, regionName);
        this.id = id;
    }

    public objArea(String id,String idFamily, Double latitude, Double longitude, Integer radius, String regionName) {
        super(latitude, longitude, radius, regionName);
        this.id = id;
        this.idFamily = idFamily;
    }


    public objArea(Double latitude, Double longitude, Integer radius, String regionName) {
        super(latitude, longitude, radius, regionName);
    }

    public String getIdFamily() {
        return idFamily;
    }

    public void setIdFamily(String idFamily) {
        this.idFamily = idFamily;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Double getLatitude() {
        return super.getLatitude();
    }

    @Override
    public void setLatitude(Double latitude) {
        super.setLatitude(latitude);
    }

    @Override
    public Double getLongitude() {
        return super.getLongitude();
    }

    @Override
    public void setLongitude(Double longitude) {
        super.setLongitude(longitude);
    }

    @Override
    public Integer getRadius() {
        return super.getRadius();
    }

    @Override
    public void setRadius(Integer radius) {
        super.setRadius(radius);
    }

    @Override
    public String getRegionName() {
        return super.getRegionName();
    }

    @Override
    public void setRegionName(String regionName) {
        super.setRegionName(regionName);
    }
}
