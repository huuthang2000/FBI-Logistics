package com.example.demoapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class tb_CurrentFamilyID {
    public static final String TAG = tb_CurrentFamilyID.class.getSimpleName();

    private final Context context;

    private com.example.demoapp.SQLite.db_life24h mDatabase;

    private static final String TABLE_CURRENT_FAMILY_ID = "tb_currentFamilyID";

    //TEXT
    private static final String KEY_ID = "id";


    private static tb_CurrentFamilyID instance;

    public static tb_CurrentFamilyID getInstance(Context context){
        if(instance == null)
            instance = new tb_CurrentFamilyID(context);
        return instance;
    }

    private tb_CurrentFamilyID(Context context) {
        this.context = context;
        this.mDatabase = com.example.demoapp.SQLite.db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_CURRENT_FAMILY_ID))
            createTable();
    }

    private void createTable(){
        final String create_table = String.format("CREATE TABLE %s ( STT INTEGER PRIMARY KEY AUTOINCREMENT , %s TEXT )",
                TABLE_CURRENT_FAMILY_ID,
                KEY_ID);
        mDatabase.getWritableDatabase().execSQL(create_table);
    }

    public void addOrUpdateID(String familyID) {
        deleteAllData();

        if(!checkItemExistByID(familyID)){
            addID(familyID);
        }
        else{
            updateID(familyID);
        }
    }

    public boolean addID(String familyID){

        if(!checkItemExistByID(familyID)){
            ContentValues values = new ContentValues();
            values.put(KEY_ID, familyID);

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.insert(TABLE_CURRENT_FAMILY_ID, null, values);

            Log.d(TAG, "Add FamilyID to SQLite: " + familyID);

            return result > 0;
        }else
            return false;

    }

    public boolean updateID(String familyID){
        if(checkItemExistByID(familyID)){
            ContentValues values = new ContentValues();
            values.put(KEY_ID, familyID);

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.update(TABLE_CURRENT_FAMILY_ID, values, KEY_ID + " = ?", new String[] { String.valueOf(familyID) });

            Log.d(TAG, "Update FamilyID to SQLite: " + familyID);

            return result > 0;
        }else
            return false;

    }

    /**
     * Delete id
     * @param familyID id of family
     * @return delete status
     */
    public boolean deleteID(String familyID){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_CURRENT_FAMILY_ID, KEY_ID + "= ?", new String[]{familyID}) > 0;
    }

    /**
     * Delete all id
     */
    public void deleteAllData(){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_CURRENT_FAMILY_ID);
    }

    /**
     * Check if an item exists by id
     * @param familyID id account
     * @return exist or not exist
     */
    private boolean checkItemExistByID(String familyID){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_CURRENT_FAMILY_ID, KEY_ID , familyID);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public String getCurrentFamilyID() {
        String familyID = "";
        String query = String.format("SELECT * FROM %s ORDER BY %s LIMIT 10", TABLE_CURRENT_FAMILY_ID, KEY_ID);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if(cursor.moveToFirst()){
                familyID = cursor.getString(1);
                cursor.close();
            }
        }

        Log.d(TAG, "FamilyID from SQLite: " + familyID);
        return familyID;
    }

}
