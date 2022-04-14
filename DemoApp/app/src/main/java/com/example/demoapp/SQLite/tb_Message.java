package com.example.demoapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.Models.objectFirebase.chat.fb_Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class tb_Message {
    public static final String TAG = tb_Message.class.getSimpleName();
    private final Context context;

    private com.example.demoapp.SQLite.db_life24h mDatabase;

    private static final String TABLE_MESSAGE = "tb_message";
    private static final String KEY_ID_CHAT = "id_chat";
    private static final String KEY_ID = "id";
    private static final String KEY_AUTH = "auth";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_MEMBERS_RECEIVED = "membersReceived";
    private static final String KEY_MEMBERS_SEEN = "membersSeen";
    private static final String KEY_TIME = "time";
    private static final String KEY_TYPE = "type";

    private static tb_Message instance;

    public static tb_Message getInstance(Context context){
        if(instance == null)
            instance = new tb_Message(context);
        return instance;
    }

    public tb_Message(Context context) {
        this.context = context;
        this.mDatabase = com.example.demoapp.SQLite.db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_MESSAGE))
            createTable();
    }

    private void createTable() {
        String create_table = String.format("CREATE TABLE %s ( STT INTEGER PRIMARY KEY AUTOINCREMENT ,  %s INTEGER , %s TEXT , %s TEXT ,%s TEXT , %s TEXT , %s TEXT , %s INTEGER , %s TEXT )",
                TABLE_MESSAGE,
                KEY_ID,
                KEY_ID_CHAT,
                KEY_AUTH,
                KEY_CONTENT,
                KEY_MEMBERS_RECEIVED,
                KEY_MEMBERS_SEEN,
                KEY_TIME,
                KEY_TYPE);

        mDatabase.getWritableDatabase().execSQL(create_table);
    }

    public boolean addMessage(int id, String idChat, fb_Message message){
        if(!checkItemExist(id, idChat)){
            ContentValues values = new ContentValues();

            values.put(KEY_ID, id);
            values.put(KEY_ID_CHAT, idChat);
            values.put(KEY_AUTH, message.getAuth());
            values.put(KEY_TIME, message.getTime());
            values.put(KEY_TYPE, message.getType());
            values.put(KEY_CONTENT, message.getContent());
            values.put(KEY_MEMBERS_RECEIVED, new Gson().toJson(message.getMembersListReceived()));
            values.put(KEY_MEMBERS_SEEN, new Gson().toJson(message.getMembersListSeen()));
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.insert(TABLE_MESSAGE, null, values);

            Log.d(TAG, "Add Message to SQLite");

            if(result > 0 && mListenerAdd != null){
                mListenerAdd.onAddMessage(new objMessage(String.valueOf(id), idChat, message));
            }

            return result > 0;
        }
        else
            return false;
    }

    public void addAndUpdateMessage(int id, String idChat, fb_Message message) {
        if(!checkItemExist(id, idChat))
            addMessage(id,idChat,message);
        else
            updateMessage(id, idChat, message);

    }

    public boolean updateMessage(int id, String idChat, fb_Message message) {
        if(checkItemExist(id, idChat)) {
            ContentValues values = new ContentValues();

            values.put(KEY_ID_CHAT, idChat);
            values.put(KEY_AUTH, message.getAuth());
            values.put(KEY_TIME, message.getTime());
            values.put(KEY_TYPE, message.getType());
            values.put(KEY_CONTENT, message.getContent());
            values.put(KEY_MEMBERS_RECEIVED, new Gson().toJson(message.getMembersListReceived()));
            values.put(KEY_MEMBERS_SEEN, new Gson().toJson(message.getMembersListSeen()));

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            int result = db.update(TABLE_MESSAGE, values, KEY_ID + " = ? AND " + KEY_ID_CHAT + " = ? ", new String[]{String.valueOf(id), idChat});

            Log.d(TAG, "Update Message to SQLite");
            return result > 0;
        }else
            return false;
    }

    /**
     *
     * @param id id of message
     * @param idChat id of chat
     * @return exist or not exist
     */
    public boolean checkItemExist(int id, String idChat){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = %s AND %s = '%s'", KEY_ID ,TABLE_MESSAGE, KEY_ID, id, KEY_ID_CHAT, idChat);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public ArrayList<String> getMembersListSeen(int id, String idChat){
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s' AND %s = %s", KEY_MEMBERS_SEEN ,TABLE_MESSAGE, KEY_ID_CHAT, idChat, KEY_ID, id);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> membersListSeen = new ArrayList<>();
        if (cursor != null){
            if(cursor.moveToFirst()){
                membersListSeen = new Gson().fromJson(cursor.getString(0), new TypeToken<ArrayList<String>>(){}.getType());
                cursor.close();
            }
        }
        return membersListSeen;
    }

    public ArrayList<String> getMembersListReceived(int id, String idChat){
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s' AND %s = %s", KEY_MEMBERS_RECEIVED ,TABLE_MESSAGE, KEY_ID_CHAT, idChat, KEY_ID, id);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> membersListReceived = new ArrayList<>();
        if (cursor != null){
            if(cursor.moveToFirst()) {
                membersListReceived = new Gson().fromJson(cursor.getString(0), new TypeToken<ArrayList<String>>() {
                }.getType());
                cursor.close();
            }
        }
        return membersListReceived;
    }

    /**
     *
     * @param idChat id of chat
     * @return message list
     */
    public List<objMessage> getAllMessageByChatID(String idChat) {
        List<objMessage>  messageList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_MESSAGE, KEY_ID_CHAT, idChat);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            List<String> membersListReceived = new Gson().fromJson(cursor.getString(5), new TypeToken<List<String>>(){}.getType());
            List<String> membersListSeen = new Gson().fromJson(cursor.getString(6), new TypeToken<List<String>>(){}.getType());
            objMessage message = new objMessage(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    membersListReceived,
                    membersListSeen,
                    cursor.getLong(7),
                    cursor.getString(8));
            messageList.add(message);
            cursor.moveToNext();
        }
        cursor.close();
        return messageList;
    }

    public objMessage getMessage(String idChat, int position) {

        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = %s ",
                TABLE_MESSAGE, KEY_ID_CHAT, idChat, KEY_ID, position);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if(cursor.moveToFirst()){
                List<String> membersListReceived = new Gson().fromJson(cursor.getString(5), new TypeToken<List<String>>() {
                }.getType());
                List<String> membersListSeen = new Gson().fromJson(cursor.getString(6), new TypeToken<List<String>>() {
                }.getType());
                objMessage message = new objMessage(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        membersListReceived,
                        membersListSeen,
                        cursor.getLong(7),
                        cursor.getString(8));
                return message;
            }
        }
        return null;
    }

    /**
     * Delete account
     * @param idChat id of chat
     * @return delete status
     */
    public boolean deleteAllMessageByChatID(String idChat){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_MESSAGE, KEY_ID_CHAT + " = ? ", new String[] { idChat }) > 0;
    }

    /**
     * Delete account
     * @param idChat id of chat
     * @param id id of message
     * @return delete status
     */
    public boolean deleteMessage(String id, String idChat){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_MESSAGE, KEY_ID + " = ? AND " + KEY_ID_CHAT + " = ? ", new String[] { id,idChat }) > 0;
    }


    //Interface listener
    private onAddMessageListener mListenerAdd;

    public void setOnAddMessageListener(onAddMessageListener l) {
        this.mListenerAdd = l;
    }

    public interface onAddMessageListener {
        void onAddMessage(objMessage message);
    }
}
