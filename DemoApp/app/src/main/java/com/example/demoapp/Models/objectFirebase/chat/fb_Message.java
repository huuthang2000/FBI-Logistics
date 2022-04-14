package com.example.demoapp.Models.objectFirebase.chat;



import com.example.demoapp.Models.objApplication.objAccount;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class fb_Message implements Serializable {

    public fb_Message(String content, String type) {
        this.content = content;
        this.type = type;
        this.time = Calendar.getInstance().getTimeInMillis();

        this.membersListReceived = new ArrayList<>();

        this.membersListSeen = new ArrayList<>();

        this.auth = objAccount.getCurrentUser().getUid();
        this.membersListReceived.add(objAccount.getCurrentUser().getUid());
        this.membersListSeen.add(objAccount.getCurrentUser().getUid());
    }

    public fb_Message(String auth, String content, List<String> membersListReceived, List<String> membersListSeen, long time, String type) {
        this.auth = auth;
        this.content = content;
        this.membersListReceived = membersListReceived;
        this.membersListSeen = membersListSeen;
        this.time = time;
        this.type = type;
    }

    public fb_Message() {
    }

    @SerializedName("auth")
    @Expose
    private String auth;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("membersListReceived")
    @Expose
    private List<String> membersListReceived = null;
    @SerializedName("membersListSeen")
    @Expose
    private List<String> membersListSeen = null;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("type")
    @Expose
    private String type;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMembersListReceived() {
        return membersListReceived;
    }

    public void setMembersListReceived(List<String> membersListReceived) {
        this.membersListReceived = membersListReceived;
    }

    public List<String> getMembersListSeen() {
        return membersListSeen;
    }

    public void setMembersListSeen(List<String> membersListSeen) {
        this.membersListSeen = membersListSeen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
