package com.example.demoapp.Models.objectFirebase.chat;

import com.example.demoapp.Models.objApplication.objAccount;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class fb_Chat implements Serializable {

    @SerializedName("auth")
    @Expose
    private String auth;
    @SerializedName("notificationID")
    @Expose
    private long notificationID;
    @SerializedName("chatName")
    @Expose
    private String chatName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("membersList")
    @Expose
    private List<String> membersList = null;
    @SerializedName("timeCreate")
    @Expose
    private long timeCreate;
    @SerializedName("timeUpdate")
    @Expose
    private long timeUpdate;
    @SerializedName("totalMessage")
    @Expose
    private long totalMessage;


    public fb_Chat() {
    }

    public fb_Chat(long notificationID, String auth, String chatName, List<String> membersList, long timeCreate, long timeUpdate, String type, long totalMessage) {
        this.auth = auth;
        this.notificationID = notificationID;
        this.chatName = chatName;
        this.membersList = membersList;
        this.timeCreate = timeCreate;
        this.type = type;
        this.totalMessage = totalMessage;
        this.timeUpdate = timeUpdate;
    }

    public fb_Chat(String auth, String chatName, List<String> membersList, String type) {
        this.notificationID = Math.abs(new Random().nextInt());
        this.auth = auth;
        this.chatName = chatName;
        this.membersList = membersList;
        this.timeCreate = Calendar.getInstance().getTimeInMillis();
        this.timeUpdate = Calendar.getInstance().getTimeInMillis();
        this.type = type;
        this.totalMessage = 0;
    }


    public fb_Chat(String chatName, List<String> membersList, String type) {
        this.notificationID = Math.abs(new Random().nextInt());
        this.auth = objAccount.getCurrentUser().getUid();
        this.chatName = chatName;
        this.membersList = membersList;
        this.timeCreate = Calendar.getInstance().getTimeInMillis();
        this.timeUpdate = Calendar.getInstance().getTimeInMillis();
        this.type = type;
        this.totalMessage = 0;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(long notificationID) {
        this.notificationID = notificationID;
    }

    public long getTotalMessage() {
        return totalMessage;
    }

    public void setTotalMessage(long totalMessage) {
        this.totalMessage = totalMessage;
    }

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(long timeUpdate) {
        this.timeUpdate = timeUpdate;
    }
}
