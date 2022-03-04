package com.example.demoapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DomExport implements Serializable {
    @SerializedName("stt")
    @Expose
    private String stt;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("temp")
    @Expose
    private String temp;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("port_export")
    @Expose
    private String portExport;

    @SerializedName("length")
    @Expose
    private String length;

    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("width")
    @Expose
    private String width;

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

    public DomExport(String stt, String name, String weight, String quantity, String temp, String address, String portExport, String length, String height, String width, String type, String month, String continent, String createdDate) {
        this.stt = stt;
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
        this.temp = temp;
        this.address = address;
        this.portExport = portExport;
        this.length = length;
        this.height = height;
        this.width = width;
        this.type = type;
        this.month = month;
        this.continent = continent;
        this.createdDate = createdDate;
    }

    public DomExport(String name, String weight, String quantity, String temp, String address, String portExport, String length, String height, String width, String type, String month, String continent, String createdDate) {
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
        this.temp = temp;
        this.address = address;
        this.portExport = portExport;
        this.length = length;
        this.height = height;
        this.width = width;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPortExport() {
        return portExport;
    }

    public void setPortExport(String portExport) {
        this.portExport = portExport;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
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
