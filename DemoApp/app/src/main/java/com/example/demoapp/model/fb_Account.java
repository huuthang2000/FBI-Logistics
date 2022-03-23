package com.example.demoapp.model;

import com.example.demoapp.Utils.keyUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;


public class    fb_Account {

    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gps")
    @Expose
    private com.example.demoapp.model.fb_Gps gps;

    @SerializedName("keyToken")
    @Expose
    private String keyToken;
    @SerializedName("location")
    @Expose
    private fb_Location location;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("batteryPercent")
    @Expose
    private String batteryPercent;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("network")
    @Expose
    private String network;

    @SerializedName("timeCreate")
    @Expose
    private long timeCreate;

    @SerializedName("bought")
    @Expose
    private boolean bought;

    @SerializedName("type")
    @Expose
    private String type;

    public fb_Account(String avatar, String email,com.example.demoapp.model.fb_Gps gps, String keyToken, fb_Location location, String name, String phone, String batteryPercent, String gender, String status, String network, long timeCreate, boolean bought, String type) {
        this.avatar = avatar;
        this.email = email;
        this.gps = gps;
        this.keyToken = keyToken;
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.batteryPercent = batteryPercent;
        this.gender = gender;
        this.status = status;
        this.network = network;
        this.timeCreate = timeCreate;
        this.bought = bought;
        this.type = type;
    }

    public fb_Account(String avatar, String email, com.example.demoapp.model.fb_Gps gps, String keyToken, fb_Location location, String name, String phone, String batteryPercent, String gender, String status, String network, String type) {
        this.avatar = avatar;
        this.email = email;
        this.gps = gps;
        this.keyToken = keyToken;
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.batteryPercent = batteryPercent;
        this.gender = gender;
        this.status = status;
        this.network = network;
        this.type = type;
        this.bought = false;
        this.timeCreate = Calendar.getInstance().getTimeInMillis();
    }

    public fb_Account(String email, String name, String type) {
        this.avatar = keyUtils.NULL;
        this.email = email;
        this.name = name;
        this.type = type;
        this.gps = new com.example.demoapp.model.fb_Gps(true, Calendar.getInstance().getTimeInMillis());
        this.location = new fb_Location(0.0, 0.0, Calendar.getInstance().getTimeInMillis());
        this.status = keyUtils.ONLINE;
        this.gender = keyUtils.OTHER;
        this.keyToken = keyUtils.NULL;
        this.phone = keyUtils.NULL;
        this.network = System.currentTimeMillis()+"";
        this.batteryPercent = "100%";
        this.bought = false;
        this.timeCreate = Calendar.getInstance().getTimeInMillis();
    }

    public fb_Account(String email, String type) {
        this.email = email;
        this.name = email;
        this.status = keyUtils.ONLINE;
        this.gender = keyUtils.OTHER;
        this.keyToken = keyUtils.NULL;
        this.phone = keyUtils.NULL;
        this.batteryPercent = "100%";
        this.avatar = keyUtils.NULL;
        this.gps = new com.example.demoapp.model.fb_Gps(true, Calendar.getInstance().getTimeInMillis());
        this.location = new fb_Location(0.0, 0.0, Calendar.getInstance().getTimeInMillis());
        this.network = System.currentTimeMillis()+"";
        this.type = type;
        this.bought = false;
        this.timeCreate = Calendar.getInstance().getTimeInMillis();
    }

    public fb_Account() {
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public com.example.demoapp.model.fb_Gps getGps() {
        return gps;
    }

    public void setGps(com.example.demoapp.model.fb_Gps fbGps) {
        this.gps = fbGps;
    }

    public String getKeyToken() {
        return keyToken;
    }

    public void setKeyToken(String keyToken) {
        this.keyToken = keyToken;
    }

    public fb_Location getLocation() {
        return location;
    }

    public void setLocation(fb_Location fbLocation) {
        this.location = fbLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBatteryPercent() {
        return batteryPercent;
    }

    public void setBatteryPercent(String batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
