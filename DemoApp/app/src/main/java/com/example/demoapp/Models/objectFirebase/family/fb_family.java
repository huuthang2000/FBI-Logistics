package com.example.demoapp.Models.objectFirebase.family;

import android.content.Context;

import com.example.demoapp.Models.objApplication.objAccount;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class fb_family {

    @SerializedName("areaList")
    @Expose
    private List<fb_area> areaList = null;
    @SerializedName("commonName")
    @Expose
    private String commonName;
    @SerializedName("inviteCode")
    @Expose
    private String inviteCode;
    @SerializedName("membersList")
    @Expose
    private List<String> membersList = null;
    @SerializedName("timeCreate")
    @Expose
    private long timeCreate;

    public fb_family() {
    }

    public fb_family(List<fb_area> areaList, String commonName, String inviteCode, List<String> membersList, long timeCreate) {
        this.areaList = areaList;
        this.commonName = commonName;
        this.inviteCode = inviteCode;
        this.membersList = membersList;
        this.timeCreate = timeCreate;
    }

    public fb_family(Context context, String inviteCode){
        this.inviteCode = inviteCode;
        String username = objAccount.getAccountFromSQLite(context,objAccount.getCurrentUser().getUid()).getEmail();
        String familyName = username.contains("@") ? username.substring(0,username.indexOf("@")) : username;
        this.commonName = familyName +"'s family";
        this.membersList = new ArrayList<>();
        this.membersList.add(objAccount.getCurrentUser().getUid());
        this.timeCreate = Calendar.getInstance().getTimeInMillis();
        this.areaList = null;
    }

    public fb_family(Context context, String familyName, String inviteCode){
        this.inviteCode = inviteCode;
        this.commonName = familyName;
        this.membersList = new ArrayList<>();
        this.membersList.add(objAccount.getCurrentUser().getUid());
        this.timeCreate = Calendar.getInstance().getTimeInMillis();
        this.areaList = null;
    }

    public List<fb_area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<fb_area> areaList) {
        this.areaList = areaList;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public List<String> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<String> membersList) {
        this.membersList = membersList;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }
}
