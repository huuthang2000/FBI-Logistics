package com.example.demoapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImportLcl implements Serializable {
    @SerializedName("stt")
    @Expose
    private String stt;

    @SerializedName("term")
    @Expose
    private String term;

    @SerializedName("pol")
    @Expose
    private String pol;

    @SerializedName("pod")
    @Expose
    private String pod;

    @SerializedName("cargo")
    @Expose
    private String cargo;

    @SerializedName("of")
    @Expose
    private String of;

    @SerializedName("local_pol")
    @Expose
    private String localPol;

    @SerializedName("local_pod")
    @Expose
    private String localPod;

    @SerializedName("carrier")
    @Expose
    private String carrier;

    @SerializedName("schedule")
    @Expose
    private String schedule;

    @SerializedName("transit_time")
    @Expose
    private String transitTime;

    @SerializedName("valid")
    @Expose
    private String valid;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("month")
    @Expose
    private String month;

    @SerializedName("continent")
    @Expose
    private String continent;

    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public ImportLcl(String stt, String term, String pol, String pod, String cargo, String of, String localPol,
                     String localPod, String carrier, String schedule, String transitTime, String valid, String note, String month, String continent, String createdDate) {
        this.stt = stt;
        this.term = term;
        this.pol = pol;
        this.pod = pod;
        this.cargo = cargo;
        this.of = of;
        this.localPol = localPol;
        this.localPod = localPod;
        this.carrier = carrier;
        this.schedule = schedule;
        this.transitTime = transitTime;
        this.valid = valid;
        this.note = note;
        this.month = month;
        this.continent = continent;
        this.createdDate = createdDate;
    }

    public ImportLcl(String term, String pol, String pod, String cargo, String of, String localPol,
                     String localPod, String carrier, String schedule, String transitTime, String valid, String note, String month, String continent, String createdDate) {
        this.term = term;
        this.pol = pol;
        this.pod = pod;
        this.cargo = cargo;
        this.of = of;
        this.localPol = localPol;
        this.localPod = localPod;
        this.carrier = carrier;
        this.schedule = schedule;
        this.transitTime = transitTime;
        this.valid = valid;
        this.note = note;
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

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getOf() {
        return of;
    }

    public void setOf(String of) {
        this.of = of;
    }

    public String getLocalPol() {
        return localPol;
    }

    public void setLocalPol(String localPol) {
        this.localPol = localPol;
    }

    public String getLocalPod() {
        return localPod;
    }

    public void setLocalPod(String localPod) {
        this.localPod = localPod;
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
