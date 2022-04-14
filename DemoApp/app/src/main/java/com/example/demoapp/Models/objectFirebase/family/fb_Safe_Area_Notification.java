package com.example.demoapp.Models.objectFirebase.family;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class fb_Safe_Area_Notification {
    @SerializedName("name_area")
    @Expose
    private String name_area; // save name of safe area
    @SerializedName("name_device")
    @Expose
    private String name_device; // save name of device
    @SerializedName("status")
    @Expose
    private String Status; // save status of area: enter/exit

    public fb_Safe_Area_Notification() {
    }

    public String getName_area() {
        return name_area;
    }

    public void setName_area(String name_area) {
        this.name_area = name_area;
    }

    public String getName_device() {
        return name_device;
    }

    public void setName_device(String name_device) {
        this.name_device = name_device;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
