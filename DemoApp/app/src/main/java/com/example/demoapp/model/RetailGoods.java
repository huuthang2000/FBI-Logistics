package com.example.demoapp.model;

import java.io.Serializable;

public class RetailGoods implements Serializable {
    private String stt, pol, pod, dim, grossweight, typeofcargo, oceanfreight,
            localcharge, carrier, schedule, transittime, valid, note, month, continent, date_created;

    public RetailGoods(String stt, String pol, String pod, String dim, String grossweight,
                       String typeofcargo, String oceanfreight, String localcharge, String carrier,
                       String schedule, String transittime, String valid, String note, String month,
                       String continent, String date_created) {
        this.stt = stt;
        this.pol = pol;
        this.pod = pod;
        this.dim = dim;
        this.grossweight = grossweight;
        this.typeofcargo = typeofcargo;
        this.oceanfreight = oceanfreight;
        this.localcharge = localcharge;
        this.carrier = carrier;
        this.schedule = schedule;
        this.transittime = transittime;
        this.valid = valid;
        this.note = note;
        this.month = month;
        this.continent = continent;
        this.date_created = date_created;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
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

    public String getDim() {
        return dim;
    }

    public void setDim(String dim) {
        this.dim = dim;
    }

    public String getGrossweight() {
        return grossweight;
    }

    public void setGrossweight(String grossweight) {
        this.grossweight = grossweight;
    }

    public String getTypeofcargo() {
        return typeofcargo;
    }

    public void setTypeofcargo(String typeofcargo) {
        this.typeofcargo = typeofcargo;
    }

    public String getOceanfreight() {
        return oceanfreight;
    }

    public void setOceanfreight(String oceanfreight) {
        this.oceanfreight = oceanfreight;
    }

    public String getLocalcharge() {
        return localcharge;
    }

    public void setLocalcharge(String localcharge) {
        this.localcharge = localcharge;
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

    public String getTransittime() {
        return transittime;
    }

    public void setTransittime(String transittime) {
        this.transittime = transittime;
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
}
