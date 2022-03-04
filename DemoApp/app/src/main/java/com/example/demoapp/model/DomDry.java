package com.example.demoapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DomDry implements Serializable {
    @SerializedName("stt")
    @Expose
    private String stt;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("quantity_pallet")
    @Expose
    private String quantityPallet;

    @SerializedName("quantity_carton")
    @Expose
    private String quantityCarton;

    @SerializedName("address_receive")
    @Expose
    private String addressReceive;

    @SerializedName("address_delivery")
    @Expose
    private String addressDelivery;

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

    public DomDry(String stt, String name, String weight, String quantityPallet, String quantityCarton, String addressReceive, String addressDelivery, String length, String height, String width, String type, String month, String continent, String createdDate) {
        this.stt = stt;
        this.name = name;
        this.weight = weight;
        this.quantityPallet = quantityPallet;
        this.quantityCarton = quantityCarton;
        this.addressReceive = addressReceive;
        this.addressDelivery = addressDelivery;
        this.length = length;
        this.height = height;
        this.width = width;
        this.type = type;
        this.month = month;
        this.continent = continent;
        this.createdDate = createdDate;
    }

    public DomDry(String name, String weight, String quantityPallet, String quantityCarton, String addressReceive, String addressDelivery, String length, String height, String width, String type, String month, String continent, String createdDate) {
        this.name = name;
        this.weight = weight;
        this.quantityPallet = quantityPallet;
        this.quantityCarton = quantityCarton;
        this.addressReceive = addressReceive;
        this.addressDelivery = addressDelivery;
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

    public String getQuantityPallet() {
        return quantityPallet;
    }

    public void setQuantityPallet(String quantityPallet) {
        this.quantityPallet = quantityPallet;
    }

    public String getQuantityCarton() {
        return quantityCarton;
    }

    public void setQuantityCarton(String quantityCarton) {
        this.quantityCarton = quantityCarton;
    }

    public String getAddressReceive() {
        return addressReceive;
    }

    public void setAddressReceive(String addressReceive) {
        this.addressReceive = addressReceive;
    }

    public String getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(String addressDelivery) {
        this.addressDelivery = addressDelivery;
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
