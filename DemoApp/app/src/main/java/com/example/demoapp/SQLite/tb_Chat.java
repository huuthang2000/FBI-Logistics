package com.example.demoapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Utils.keyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class tb_Chat {
    public static final String TAG = tb_Chat.class.getSimpleName();
    private final Context context;

    private com.example.demoapp.SQLite.db_life24h mDatabase;

    private static final String TABLE_CHAT = "tb_chat";
    private static final String KEY_ID_CHAT = "id_chat";
    private static final String KEY_AUTH = "auth";
    private static final String KEY_CHAT_NAME = "chatName";
    private static final String KEY_NOTIFICATION_ID = "notificationID";
    private static final String KEY_MEMBERS_LIST = "membersList";
    private static final String KEY_TIME_CREATE = "timeCreate";
    private static final String KEY_TIME_UPDATE = "timeUpdate";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TOTAL_MESSAGE = "totalMessage";

    private static tb_Chat instance;

    public static tb_Chat getInstance(Context context){
        if(instance == null)
            instance = new tb_Chat(context);
        return instance;
    }

    public tb_Chat(Context context) {
        this.context = context;
        this.mDatabase = com.example.demoapp.SQLite.db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_CHAT))
            createTable();
    }

    private void createTable() {
        String create_table = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY , %s TEXT , %s TEXT , %s INTEGER , %s TEXT , %s INTEGER , %s INTEGER , %s TEXT, %s INTEGER)",
                TABLE_CHAT,
                KEY_ID_CHAT,
                KEY_AUTH,
                KEY_CHAT_NAME,
                KEY_NOTIFICATION_ID,
                KEY_MEMBERS_LIST,
                KEY_TIME_CREATE,
                KEY_TIME_UPDATE,
                KEY_TYPE,
                KEY_TOTAL_MESSAGE);

        mDatabase.getWritableDatabase().execSQL(create_table);
    }

    public boolean addChat(String idChat, fb_Chat chat){
        if(!checkItemExist(idChat)){
            ContentValues values = new ContentValues();
            values.put(KEY_ID_CHAT, idChat);
            values.put(KEY_AUTH, chat.getAuth());
            values.put(KEY_CHAT_NAME, chat.getChatName());
            values.put(KEY_NOTIFICATION_ID, chat.getNotificationID());
            values.put(KEY_MEMBERS_LIST, new Gson().toJson(chat.getMembersList()));
            values.put(KEY_TIME_CREATE, chat.getTimeCreate());
            values.put(KEY_TIME_UPDATE, chat.getTimeUpdate());
            values.put(KEY_TYPE, chat.getType());
            values.put(KEY_TOTAL_MESSAGE, chat.getTotalMessage());
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.insert(TABLE_CHAT, null, values);

            Log.d(TAG, "Add chat to SQLite");

            //Add interface
            if(result > 0 && mListenerAdd != null){
                mListenerAdd.onAddChat(new objChat(idChat,chat));
            }

            return result > 0;
        }
        return false;
    }

    public void addChatAndUpdate(String idChat, fb_Chat chat) {
        if(!checkItemExist(idChat))
            addChat(idChat,chat);
        else
            updateChat(idChat, chat);

    }

    private boolean updateChat(String idChat, fb_Chat chat) {
        if(checkItemExist(idChat)) {

            ContentValues values = new ContentValues();

            values.put(KEY_AUTH, chat.getAuth());
            values.put(KEY_CHAT_NAME, chat.getChatName());
            values.put(KEY_MEMBERS_LIST, new Gson().toJson(chat.getMembersList()));
            values.put(KEY_TIME_UPDATE, chat.getTimeUpdate());
            values.put(KEY_TYPE, chat.getType());
            values.put(KEY_TOTAL_MESSAGE, chat.getTotalMessage());


            SQLiteDatabase db = mDatabase.getWritableDatabase();
            int result = db.update(TABLE_CHAT, values, KEY_ID_CHAT + " = ? ", new String[]{idChat});

            Log.d(TAG, "Update chat to SQLite");
            return result > 0;
        }
        return false;
    }

    /**
     *
     * @param idChat id of chat
     * @return exist or not exist
     */
    public boolean checkItemExist(String idChat){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'", KEY_ID_CHAT ,TABLE_CHAT, KEY_ID_CHAT, idChat);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public ArrayList<String> getMembersList(String idChat){
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s' ", KEY_MEMBERS_LIST ,TABLE_CHAT, KEY_ID_CHAT, idChat);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> membersList = new ArrayList<>();
        if (cursor != null){
            if(cursor.moveToFirst()){
                membersList = new Gson().fromJson(cursor.getString(0), new TypeToken<ArrayList<String>>(){}.getType());
                cursor.close();
            }
        }
        return membersList;
    }

    /**
     *
     * @return chat list
     */
    public List<objChat> getAllChat() {
        String query = String.format("SELECT * FROM %s", TABLE_CHAT);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        ArrayList<objChat> chatListAll = new ArrayList<>();
        while(!cursor.isAfterLast())
        {
            List<String> membersList = new Gson().fromJson(cursor.getString(4), new TypeToken<List<String>>(){}.getType());

            objChat chat = new objChat(cursor.getString(0),
                    cursor.getInt(3),
                    cursor.getString(1),
                    cursor.getString(2),
                    membersList,
                    cursor.getLong(5),
                    cursor.getLong(6),
                    cursor.getString(7),
                    cursor.getLong(8));

            chatListAll.add(chat);
            cursor.moveToNext();
        }
        cursor.close();

        return chatListAll;
    }

    /**
     *
     * @param Uid user id
     * @return chat list
     */
    public List<objChat> getAllChatByUid(String Uid) {

        ArrayList<objChat> chatListAll = (ArrayList<objChat>) getAllChat();
        ArrayList<objChat> chatListOfUid = new ArrayList<>();
        for(objChat chat : chatListAll){
            if(chat.getMembersList().contains(Uid) &&
                    chat.getId().contains(tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID().replace("/"+ keyUtils.familyList,"")) &&
                    com.example.demoapp.SQLite.tb_Message.getInstance(context).getAllMessageByChatID(chat.getId()).size() > 0)
                chatListOfUid.add(chat);
        }

        return chatListOfUid;
    }



    /**
     * Delete chat
     * @param id id of chat
     * @return delete status
     */
    public boolean deleteChat(String id){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_CHAT, KEY_ID_CHAT + " = ? " , new String[] { id }) > 0;
    }


    //Interface listener
    private onAddChatListener mListenerAdd;

    public void setOnAddChatListener(onAddChatListener l) {
        this.mListenerAdd = l;
    }

    public interface onAddChatListener {
        void onAddChat(objChat chat);
    }
}
