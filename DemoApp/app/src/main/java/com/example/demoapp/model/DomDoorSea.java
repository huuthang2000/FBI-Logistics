package com.example.demoapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DomDoorSea implements Serializable {
    @SerializedName("stt")
    @Expose
    private String stt;

    @SerializedName("port_go")
    @Expose
    private String portGo;

    @SerializedName("port_come")
    @Expose
    private String portCome;

    @SerializedName("address_receive")
    @Expose
    private String addressReceive;

    @SerializedName("address_delivery")
    @Expose
    private String addressDelivery;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("etd")
    @Expose
    private String etd;

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

    public DomDoorSea(String stt, String portGo, String portCome, String addressReceive, String addressDelivery, String name, String weight, String quantity, String etd, String type, String month, String continent, String createdDate) {
        this.stt = stt;
        this.portGo = portGo;
        this.portCome = portCome;
        this.addressReceive = addressReceive;
        this.addressDelivery = addressDelivery;
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
        this.etd = etd;
        this.type = type;
        this.month = month;
        this.continent = continent;
        this.createdDate = createdDate;
    }

    public DomDoorSea(String portGo, String portCome, String addressReceive, String addressDelivery, String name, String weight, String quantity, String etd, String type, String month, String continent, String createdDate) {
        this.portGo = portGo;
        this.portCome = portCome;
        this.addressReceive = addressReceive;
        this.addressDelivery = addressDelivery;
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
        this.etd = etd;
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

    public String getPortGo() {
        return portGo;
    }

    public void setPortGo(String portGo) {
        this.portGo = portGo;
    }

    public String getPortCome() {
        return portCome;
    }

    public void setPortCome(String portCome) {
        this.portCome = portCome;
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

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
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
