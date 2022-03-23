package com.example.demoapp.model;


/**
 * Author: Lucaswalker@jexpa.com
 * Class: Distributing
 * History: 2/12/2020
 * Project: SecondClone
 */

/*
 *  Date created: 02/12/2020
 *  Last updated: 02/12/2020
 *  Name project: life24h
 *  Description:
 *  Auth: trong.le@1byte.com
 */

public class objLocation {
    private String keyID;
    private fb_Location fb_location;

    public objLocation(String keyID, fb_Location fb_location) {
        this.keyID = keyID;
        this.fb_location = fb_location;
    }

    public objLocation() {
    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public fb_Location getFb_location() {
        return fb_location;
    }

    public void setFb_location(fb_Location fb_location) {
        this.fb_location = fb_location;
    }
}
