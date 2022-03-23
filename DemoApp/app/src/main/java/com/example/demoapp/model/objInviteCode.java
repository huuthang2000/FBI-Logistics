package com.example.demoapp.model;



public class objInviteCode {
    private String inviteCode;
    private String idFamily;

    public objInviteCode(String inviteCode, String idFamily) {
        this.inviteCode = inviteCode;
        this.idFamily = idFamily;
    }

    public objInviteCode() {
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getIdFamily() {
        return idFamily;
    }

    public void setIdFamily(String idFamily) {
        this.idFamily = idFamily;
    }
}
