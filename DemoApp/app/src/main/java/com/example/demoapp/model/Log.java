package com.example.demoapp.model;

import java.io.Serializable;

public class Log implements Serializable {
    private String stt;
    private String tenhang;
    private String hscode;
    private String congdung;
    private String hinhanh;
    private String cangdi;
    private String cangden;
    private String loaihang;
    private String soluongcuthe;
    private String yeucaudacbiet;
    private String price;
    private String valid;
    private String month;
    private String importorexport;
    private String type;
    private String date_created;

    public Log(String stt, String tenhang, String hscode, String congdung, String hinhanh,
               String cangdi, String cangden, String loaihang, String soluongcuthe,
               String yeucaudacbiet, String price, String valid, String month, String importorexport,
               String type, String date_created) {
        this.stt = stt;
        this.tenhang = tenhang;
        this.hscode = hscode;
        this.congdung = congdung;
        this.hinhanh = hinhanh;
        this.cangdi = cangdi;
        this.cangden = cangden;
        this.loaihang = loaihang;
        this.soluongcuthe = soluongcuthe;
        this.yeucaudacbiet = yeucaudacbiet;
        this.price = price;
        this.valid = valid;
        this.month = month;
        this.importorexport = importorexport;
        this.type = type;
        this.date_created = date_created;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
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

    public String getTenhang() {
        return tenhang;
    }

    public void setTenhang(String tenhang) {
        this.tenhang = tenhang;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getCongdung() {
        return congdung;
    }

    public void setCongdung(String congdung) {
        this.congdung = congdung;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getCangdi() {
        return cangdi;
    }

    public void setCangdi(String cangdi) {
        this.cangdi = cangdi;
    }

    public String getCangden() {
        return cangden;
    }

    public void setCangden(String cangden) {
        this.cangden = cangden;
    }

    public String getLoaihang() {
        return loaihang;
    }

    public void setLoaihang(String loaihang) {
        this.loaihang = loaihang;
    }

    public String getSoluongcuthe() {
        return soluongcuthe;
    }

    public void setSoluongcuthe(String soluongcuthe) {
        this.soluongcuthe = soluongcuthe;
    }

    public String getYeucaudacbiet() {
        return yeucaudacbiet;
    }

    public void setYeucaudacbiet(String yeucaudacbiet) {
        this.yeucaudacbiet = yeucaudacbiet;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getImportorexport() {
        return importorexport;
    }

    public void setImportorexport(String importorexport) {
        this.importorexport = importorexport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
