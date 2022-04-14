package com.example.demoapp.Presenters.Chat;


import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objApplication.objDetailImage;
import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;

import java.util.ArrayList;

public interface view_Chat {

    void resultListChat(ArrayList<objChat> chatList);

    void resultOfAction(boolean isSuccess, String message, String type);

    void resultChatDetail(fb_Chat chats);

    void resultMessage(objMessage newMessage);

    void resultAllImage(ArrayList<objDetailImage> detailImageList);

}
