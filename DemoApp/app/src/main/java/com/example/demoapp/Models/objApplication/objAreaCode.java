package com.example.demoapp.Models.objApplication;


public class objAreaCode {

    public String id;
    public String countriesName;

    public objAreaCode() {
    }

    public objAreaCode(String id, String countriesName) {
        this.id = id;
        this.countriesName = countriesName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountriesName() {
        return countriesName;
    }

    public void setCountriesName(String countriesName) {
        this.countriesName = countriesName;
    }
}