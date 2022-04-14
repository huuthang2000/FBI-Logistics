package com.example.demoapp.Models.objApplication;



import static com.example.demoapp.Utils.patternFormatDateTime.hh_mm_a;

import com.example.demoapp.Models.objectFirebase.chat.fb_Message;
import com.example.demoapp.Utils.timeUtils;

import java.io.Serializable;
import java.util.List;

public class objMessage extends fb_Message implements Serializable {

    private String id;
    private String idChat;

    public objMessage(String content, String type, String id, String idChat) {
        super(content, type);
        this.id = id;
        this.idChat = idChat;
    }

    public objMessage(String id, String idChat, String auth, String content, List<String> membersListReceived, List<String> membersListSeen, long time, String type) {
        super(auth, content, membersListReceived, membersListSeen, time, type);
        this.id = id;
        this.idChat = idChat;
    }

    public objMessage(String id, String idChat, fb_Message message) {
        super(message.getAuth(), message.getContent(), message.getMembersListReceived(), message.getMembersListSeen(), message.getTime(), message.getType());
        this.id = id;
        this.idChat = idChat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public fb_Message getFireBaseMessage(){
        return new fb_Message(getAuth(), getContent(),getMembersListReceived(),getMembersListSeen(),getTime(),getType());
    }

    @Override
    public String getContent() {
        return super.getContent();
    }

    @Override
    public void setContent(String content) {
        super.setContent(content);
    }

    @Override
    public List<String> getMembersListReceived() {
        return super.getMembersListReceived();
    }

    @Override
    public void setMembersListReceived(List<String> membersListReceived) {
        super.setMembersListReceived(membersListReceived);
    }

    @Override
    public List<String> getMembersListSeen() {
        return super.getMembersListSeen();
    }

    @Override
    public void setMembersListSeen(List<String> membersListSeen) {
        super.setMembersListSeen(membersListSeen);
    }

    @Override
    public long getTime() {
        return super.getTime();
    }

    @Override
    public void setTime(long time) {
        super.setTime(time);
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    public String getStringTime(){
        return timeUtils.convertMillisecondToStringFormat(getTime(),hh_mm_a);
    }
}
