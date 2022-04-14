package com.example.demoapp.Models.objApplication;

import com.example.demoapp.Models.objectFirebase.account.fb_Location;




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
