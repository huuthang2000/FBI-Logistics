package com.example.demoapp.Presenters.Chat;


import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.example.demoapp.Models.objectFirebase.chat.fb_Message;

import java.util.ArrayList;

public interface imp_Chat {

    /**
     *
     * @param idChat id chat
     */
    void getChatDetail(String idChat);

    /**
     *
     * @param idChat id chat
     */
    void getMessageList(String idChat);


    /**
     *
     * @param idChat id chat
     * @param content content
     * @param bmImage bitmap image
     * @param type type of action
     */
    void postMessage(String idChat, String content, Bitmap bmImage, String type);

    /**
     * Upload the image to the server and return the results
     * @param position position
     * @param pathImage image path
     * @param result interface onResult
     */
    void uploadImageToFolderMessage(final int position, final String pathImage, final com.example.demoapp.Presenters.Chat.pre_Chat.onResultUploadImage result);

    /**
     *
     * @param idChat id chat
     */
    void getAllImageFromMessage(String idChat);

    void getAllListChatOfUid();

    /**
     *
     * @param idChat id chat
     * @param members members
     * @param chatName chat name
     */
    void editDetailChat(String idChat, ArrayList<String> members, String chatName);

    /**
     *
     * @param idChat id chat
     * @param idMember id member
     * @param type type of remove (remove or leave group)
     */
    void removeMember(String idChat, String idMember,String type);


    /**
     *
     * @param Uid User id
     * @param result Interface result
     */
    void findChatIDByUid(final String Uid, @NonNull final com.example.demoapp.Presenters.Chat.pre_Chat.onResultFindChatIDByUid result);

    /**
     *
     * @param idChat id of chat
     * @param message object message fireBase
     * @param idMessage id of message
     */
    void setSeenTheMessage(String idChat, String idMessage,  fb_Message message);

    /**
     *
     * @param idChat id of chat
     * @param message object message fireBase
     * @param idMessage id of message
     */
    void setReceivedTheMessage(String idChat, String idMessage,  fb_Message message);
}
