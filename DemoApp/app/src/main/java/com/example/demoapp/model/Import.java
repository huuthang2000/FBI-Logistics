package com.example.demoapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Import implements Serializable {
    @SerializedName("stt")
    @Expose
    private String stt;

    @SerializedName("pol")
    @Expose
    private String pol;

    @SerializedName("pod")
    @Expose
    private String pod;

    @SerializedName("of20")
    @Expose
    private String of20;

    @SerializedName("of40")
    @Expose
    private String of40;

    @SerializedName("of45")
    @Expose
    private String of45;

    @SerializedName("sur20")
    @Expose
    private String sur20;

    @SerializedName("sur40")
    @Expose
    private String sur40;

    @SerializedName("sur45")
    @Expose
    private String sur45;

    @SerializedName("total_freight")
    @Expose
    private String totalFreight;

    @SerializedName("carrier")
    @Expose
    private String carrier;

    @SerializedName("schedule")
    @Expose
    private String schedule;

    @SerializedName("transit_time")
    @Expose
    private String transitTime;

    @SerializedName("free_time")
    @Expose
    private String freeTime;

    @SerializedName("valid")
    @Expose
    private String valid;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("month")
    @Expose
    private String month;

    @SerializedName("continent")
    @Expose
    private String continent;

    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public Import(String stt, String pol, String pod, String of20, String of40, String of45,
                  String sur20, String sur40, String sur45, String totalFreight, String carrier,
                  String schedule, String transitTime, String freeTime, String valid, String note,
                  String type, String month, String continent, String createdDate) {
        this.stt = stt;
        this.pol = pol;
        this.pod = pod;
        this.of20 = of20;
        this.of40 = of40;
        this.of45 = of45;
        this.sur20 = sur20;
        this.sur40 = sur40;
        this.sur45 = sur45;
        this.totalFreight = totalFreight;
        this.carrier = carrier;
        this.schedule = schedule;
        this.transitTime = transitTime;
        this.freeTime = freeTime;
        this.valid = valid;
        this.note = note;
        this.type = type;
        this.month = month;
        this.continent = continent;
        this.createdDate = createdDate;
    }

    public Import(String pol, String pod, String of20, String of40, String of45, String sur20,
                  String sur40, String sur45, String totalFreight, String carrier, String schedule,
                  String transitTime, String freeTime, String valid, String note, String type,
                  String month, String continent, String createdDate) {
        this.pol = pol;
        this.pod = pod;
        this.of20 = of20;
        this.of40 = of40;
        this.of45 = of45;
        this.sur20 = sur20;
        this.sur40 = sur40;
        this.sur45 = sur45;
        this.totalFreight = totalFreight;
        this.carrier = carrier;
        this.schedule = schedule;
        this.transitTime = transitTime;
        this.freeTime = freeTime;
        this.valid = valid;
        this.note = note;
        this.type = type;
        this.month = month;
        this.continent = continent;
        this.createdDate = createdDate;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getOf20() {
        return of20;
    }

    public void setOf20(String of20) {
        this.of20 = of20;
    }

    public String getOf40() {
        return of40;
    }

    public void setOf40(String of40) {
        this.of40 = of40;
    }

    public String getOf45() {
        return of45;
    }

    public void setOf45(String of45) {
        this.of45 = of45;
    }

    public String getSur20() {
        return sur20;
    }

    public void setSur20(String sur20) {
        this.sur20 = sur20;
    }

    public String getSur40() {
        return sur40;
    }

    public void setSur40(String sur40) {
        this.sur40 = sur40;
    }

    public String getSur45() {
        return sur45;
    }

    public void setSur45(String sur45) {
        this.sur45 = sur45;
    }

    public String getTotalFreight() {
        return totalFreight;
    }

    public void setTotalFreight(String totalFreight) {
        this.totalFreight = totalFreight;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(String transitTime) {
        this.transitTime = transitTime;
    }

    public String getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
