package com.example.demoapp.Models.objApplication;


import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;

import java.util.List;

public class objChat extends fb_Chat{

    private String id;

    public objChat(String id, fb_Chat chat) {
        super(chat.getNotificationID(),chat.getAuth(), chat.getChatName(), chat.getMembersList(), chat.getTimeCreate(),chat.getTimeUpdate(), chat.getType(), chat.getTotalMessage());
        this.id = id;
    }

    public objChat(String id, int notificationID, String auth, String chatName, List<String> membersList, long timeCreate, long timeUpdate, String type, long totalMessage) {
        super(notificationID, auth, chatName, membersList, timeCreate,timeUpdate, type,totalMessage);
        this.id = id;
    }

    public objChat(String auth, String chatName, List<String> membersList, String type, String id) {
        super(auth, chatName, membersList, type);
        this.id = id;
    }

    public objChat(String chatName, List<String> membersList, String type, String id) {
        super(chatName, membersList, type);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public fb_Chat getFireBaseChat(){
        return new fb_Chat(getNotificationID(),getAuth(),getChatName(),getMembersList(),getTimeCreate(),getTimeUpdate(),getType(), getTotalMessage());
    }

    @Override
    public String getChatName() {
        return super.getChatName();
    }

    @Override
    public void setChatName(String chatName) {
        super.setChatName(chatName);
    }

    @Override
    public String getAuth() {
        return super.getAuth();
    }

    @Override
    public void setAuth(String auth) {
        super.setAuth(auth);
    }

    @Override
    public List<String> getMembersList() {
        return super.getMembersList();
    }

    @Override
    public void setMembersList(List<String> membersList) {
        super.setMembersList(membersList);
    }

    @Override
    public long getTimeCreate() {
        return super.getTimeCreate();
    }

    @Override
    public void setTimeCreate(long timeCreate) {
        super.setTimeCreate(timeCreate);
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    @Override
    public void setNotificationID(long notificationID) {
        super.setNotificationID(notificationID);
    }

    @Override
    public long getNotificationID() {
        return super.getNotificationID();
    }

    @Override
    public long getTotalMessage() {
        return super.getTotalMessage();
    }

    @Override
    public void setTotalMessage(long totalMessage) {
        super.setTotalMessage(totalMessage);
    }

    @Override
    public long getTimeUpdate() {
        return super.getTimeUpdate();
    }

    @Override
    public void setTimeUpdate(long timeUpdate) {
        super.setTimeUpdate(timeUpdate);
    }
}
