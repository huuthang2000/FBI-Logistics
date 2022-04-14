package com.example.demoapp.SQLite;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objArea;
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.Models.objectFirebase.family.fb_area;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class tb_Family {
    public static final String TAG = tb_Family.class.getSimpleName();
    private final Context context;

    private db_life24h mDatabase;

    private static final String TABLE_FAMILY = "tb_Family";
    private static final String KEY_ID = "id";
    private static final String KEY_COMMON_NAME = "commonName";
    private static final String KEY_INVITE_CODE = "inviteCode";
    private static final String KEY_MEMBERS_LIST = "membersList";
    private static final String KEY_TIME_CREATE = "timeCreate";

    private static tb_Family instance;

    public static tb_Family getInstance(Context context){
        if(instance == null)
            instance = new tb_Family(context);
        return instance;
    }

    private tb_Family(Context context) {
        this.context = context;
        this.mDatabase = db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_FAMILY))
            createTable();
    }

    private void createTable() {
        String create_family_table = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT , %s TEXT , %s TEXT , %s INTEGER )",
                TABLE_FAMILY,
                KEY_ID,
                KEY_COMMON_NAME,
                KEY_INVITE_CODE,
                KEY_MEMBERS_LIST,
                KEY_TIME_CREATE);

        mDatabase.getWritableDatabase().execSQL(create_family_table);
    }

    public void addOrUpdateFamily(objFamily family) {
        if(!checkItemExist(family.getId()))
            addFamily(family);
        else
            updateFamily(family);

    }

    private boolean addFamily(objFamily family) {
        if(!checkItemExist(family.getId())){
            ContentValues values = new ContentValues();

            values.put(KEY_ID,family.getId());
            values.put(KEY_COMMON_NAME,family.getCommonName());
            values.put(KEY_INVITE_CODE, family.getInviteCode());
            values.put(KEY_MEMBERS_LIST, new Gson().toJson(family.getMembersList()));
            values.put(KEY_TIME_CREATE, family.getTimeCreate());

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.insert(TABLE_FAMILY, null, values);

            return result > 0;
        }else
            return false;
    }

    public boolean updateFamily(objFamily family) {
        ContentValues values = new ContentValues();
        values.put(KEY_COMMON_NAME,family.getCommonName());
        values.put(KEY_INVITE_CODE, family.getInviteCode());
        values.put(KEY_MEMBERS_LIST, new Gson().toJson(family.getMembersList()));
        values.put(KEY_TIME_CREATE, family.getTimeCreate());

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        int result = db.update(TABLE_FAMILY, values, KEY_ID + " = ? ", new String[] { family.getId() });

        Log.d(TAG, "Update family to SQLite");
        return result > 0;
    }

    public boolean updateFamilyName(String familyID, String familyName) {
        ContentValues values = new ContentValues();
        values.put(KEY_COMMON_NAME,familyName);

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        int result = db.update(TABLE_FAMILY, values, KEY_ID + " = ? ", new String[] { familyID });

        Log.d(TAG, "Update family name to SQLite");
        return result > 0;
    }

    /**
     *
     * @param idFamily id of family
     * @return exist or not exist
     */
    private boolean checkItemExist(String idFamily){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_FAMILY, KEY_ID, idFamily);
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
     * @param Uid user id
     * @return All family of user id
     */
    public ArrayList<objFamily> getAllFamilyByUid(String Uid){
        //Get all family
        ArrayList<objFamily> allFamilies = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", TABLE_FAMILY);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            String familyID = cursor.getString(0);
            String commonName = cursor.getString(1);
            String inviteCode = cursor.getString(2);
            long timeCreate = cursor.getLong(4);
            String strMemberList = cursor.getString(3);

            List<fb_area> areaList = new ArrayList<>();
            List<objArea> temp = tb_Area.getInstance(context).getAreaListByIDFamily(familyID);
            final int sizeArea = temp.size();

            for(int i = 0; i< sizeArea; i++){
                for(objArea area : temp){
                    if(area.getId().equals(temp.get(i).getId()))
                        areaList.add(area);
                }
            }

            ArrayList<String> membersList = new Gson().fromJson(strMemberList,new TypeToken<List<String>>(){}.getType());

            objFamily family = new objFamily(familyID,
                    areaList,
                    commonName,
                    inviteCode,
                    membersList,
                    timeCreate);

            allFamilies.add(family);

            cursor.moveToNext();
        }
        cursor.close();
        //Filter all users' families
        ArrayList<objFamily> familiesOfUid = new ArrayList<>();
        for(objFamily family : allFamilies){
            if(family.getMembersList().contains(Uid))
                familiesOfUid.add(family);
        }

        return familiesOfUid;
    }

    public objFamily getFamilyByID(String idFamily){

        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_FAMILY, KEY_ID, idFamily);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null){
            if(cursor.moveToFirst()){
                List<fb_area> areaList = new ArrayList<>();
                List<objArea> temp = tb_Area.getInstance(context).getAreaListByIDFamily(idFamily);
                final int sizeArea = temp.size();

                for(int i = 0; i< sizeArea; i++){
                    for(objArea area : temp){
                        if(area.getId().equals(temp.get(i).getId()))
                            areaList.add(area);
                    }
                }

                ArrayList<String> membersList = new Gson().fromJson(cursor.getString(3),new TypeToken<List<String>>(){}.getType());

                objFamily family = new objFamily(cursor.getString(0),
                        areaList,
                        cursor.getString(1),
                        cursor.getString(2),
                        membersList,
                        cursor.getLong(4));

                cursor.close();
                return family;
            }
        }
        return null;
    }

    public ArrayList<objAccount> getAllUserInFamilyByID(String familyID){
        ArrayList<String> memberList = (ArrayList<String>) getFamilyByID(familyID).getMembersList();
        ArrayList<objAccount> accounts = new ArrayList<>();

        for(String Uid : memberList){
            objAccount acc = tb_Account.getInstance(context).getAccountByID(Uid);
            if(acc != null)
                accounts.add(acc);
        }

        return accounts;
    }

    /**
     * Delete family
     * @param idFamily id of family
     * @return delete status
     */
    public boolean deleteFamily(String idFamily){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_FAMILY, KEY_ID + " = ? ", new String[] { idFamily }) > 0;
    }


}
