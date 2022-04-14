package com.example.demoapp.SQLite;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class tb_IDFamily {
    public static final String TAG = tb_IDFamily.class.getSimpleName();
    private final Context context;

    private db_life24h mDatabase;

    private static final String TABLE_ID_FAMILY = "tb_IDFamily";
    private static final String KEY_ID = "id";
    private static final String KEY_uID = "uID";
    private static final String KEY_PATH_ID_FAMILY = "pathIDFamily";

    private static tb_IDFamily instance;

    public static tb_IDFamily getInstance(Context context){
        if(instance == null)
            instance = new tb_IDFamily(context);
        return instance;
    }

    private tb_IDFamily(Context context) {
        this.context = context;
        this.mDatabase = db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_ID_FAMILY))
            createTable();
    }

    private void createTable() {
        String create_table = String.format("CREATE TABLE %s ( STT INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER ,%s TEXT , %s TEXT )",
                TABLE_ID_FAMILY,
                KEY_ID,
                KEY_uID,
                KEY_PATH_ID_FAMILY);

        mDatabase.getWritableDatabase().execSQL(create_table);
    }

    public void addOrUpdateFamilyID(int id, String Uid, String pathIDFamily) {

        if(!checkItemExist(Uid,id)){
            addFamilyID(id,Uid, pathIDFamily);
        }
        else
            updateFamily(id,Uid,pathIDFamily);

    }

    private boolean addFamilyID(int id, String Uid, String pathIDFamily) {
        if(!checkItemExist(Uid,id)){
            ContentValues values = new ContentValues();
            values.put(KEY_ID,id);
            values.put(KEY_uID,Uid);
            values.put(KEY_PATH_ID_FAMILY,pathIDFamily);

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.insert(TABLE_ID_FAMILY, null, values);


            Log.d(TAG, "Add familyID to SQLite");

            return result > 0;
        }else
            return false;
    }

    public boolean updateFamily(int index, String uID, String idFamily) {
        if(checkItemExist(uID,index)){
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_PATH_ID_FAMILY,idFamily);

            int result = db.update(TABLE_ID_FAMILY, values, KEY_ID + " = ? AND " + KEY_uID + " = ? ", new String[] { String.valueOf(index), uID });

            Log.d(TAG, "Update familyID to SQLite");
            return result > 0;
        }else
            return false;
    }

    /**
     *
     * @param id sequence numbers in the list
     * @param uID user id
     * @return exist or not exist
     */
    private boolean checkItemExist(String uID, int id){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = %s AND %s = '%s'", TABLE_ID_FAMILY, KEY_ID, id, KEY_uID, uID);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     *
     * @param uID user id
     * @param id sequence numbers in the list
     * @return path id of family
     */
    public String getIDFamily(String uID, int id){
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = %s", TABLE_ID_FAMILY, KEY_uID, uID, KEY_ID, id);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null){
            if(cursor.moveToFirst()){
                String pathIDFamily = cursor.getString(3);
                cursor.close();
                return pathIDFamily;
            }
        }
        return "";
    }

    /**
     *
     * @param uID user KEY_ID
     * @return id family list of user
     */
    public ArrayList<String> getAllIDFamilyByUserID(String uID){
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_ID_FAMILY, KEY_uID, uID);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> IDFamilyList = new ArrayList<>();

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            IDFamilyList.add(cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();
        return IDFamilyList;
    }

    /**
     * Delete family
     * @param uID user id
     * @param index sequence numbers in the list
     * @return delete status
     */
    public boolean deleteFamily(String uID, int index){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_ID_FAMILY, KEY_ID + " = ? AND " + KEY_uID + " = ? ", new String[] { String.valueOf(index), uID }) > 0;
    }
}
