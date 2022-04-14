package com.example.demoapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objInviteCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class tb_InviteCode {
    public static final String TAG = tb_InviteCode.class.getSimpleName();
    private final Context context;

    private db_life24h mDatabase;

    private static final String TABLE_INVITE_CODE = "tb_inviteCode";
    private static final String KEY_INVITE_CODE = "inviteCode";
    private static final String KEY_FAMILY_ID = "familyID";

    private static tb_InviteCode instance;

    public static tb_InviteCode getInstance(Context context){
        if(instance == null)
            instance = new tb_InviteCode(context);
        return instance;
    }

    public tb_InviteCode(Context context) {
        this.context = context;
        this.mDatabase = db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_INVITE_CODE))
            createTable();
    }

    private void createTable() {
        String create_inviteCode_table = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY , %s TEXT )",
                TABLE_INVITE_CODE,
                KEY_INVITE_CODE,
                KEY_FAMILY_ID);

        mDatabase.getWritableDatabase().execSQL(create_inviteCode_table);
    }


    public String createAndAddInviteCode(String familyID){

        //Create invite code
        String inviteCode;
        do{
            inviteCode = randomInviteCode();
        }while (!checkItemExist(inviteCode));

        ContentValues values = new ContentValues();

        values.put(KEY_INVITE_CODE, inviteCode);
        values.put(KEY_FAMILY_ID, familyID);

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        db.insert(TABLE_INVITE_CODE, null, values);

        return inviteCode;
    }

    public static String randomInviteCode() {
        String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
        int sizeOfRandomString = 6;
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString().toUpperCase();
    }

    public boolean addInviteCode(String inviteCode, String familyID){
        if(!checkItemExist(inviteCode)) {
            ContentValues values = new ContentValues();

            values.put(KEY_INVITE_CODE, inviteCode);
            values.put(KEY_FAMILY_ID, familyID);

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.insert(TABLE_INVITE_CODE, null, values);

            Log.d(TAG, "addInviteCode: " + inviteCode);
            return result > 0;
        }else
            return false;
    }

    public void addOrUpdateInviteCode(String inviteCode, String familyID) {

        if(!checkItemExist(inviteCode))
            addInviteCode(inviteCode,familyID);
        else
            updateInviteCode(inviteCode, familyID);

    }

    private boolean updateInviteCode(String inviteCode, String familyID) {
        if(checkItemExist(inviteCode)){
            ContentValues values = new ContentValues();

            values.put(KEY_FAMILY_ID, familyID);

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            int result = db.update(TABLE_INVITE_CODE, values, KEY_INVITE_CODE + " = ? ", new String[] { inviteCode });

            Log.d(TAG, "updateInviteCode: " + inviteCode);

            return result > 0;
        }
        else
            return false;

    }

    /**
     *
     * @param inviteCode invite code
     * @return exist or not exist
     */
    private boolean checkItemExist(String inviteCode){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s' ", KEY_FAMILY_ID ,TABLE_INVITE_CODE, KEY_INVITE_CODE, inviteCode);
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
     * @return objInviteCode list
     */
    public List<objInviteCode> getAllInviteCode() {
        List<objInviteCode>  inviteCodeList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s ", TABLE_INVITE_CODE);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            inviteCodeList.add(new objInviteCode(cursor.getString(0),cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return inviteCodeList;
    }

    /**
     * Delete account
     * @param inviteCode invite code
     * @return delete status
     */
    public boolean deleteInviteCode(String inviteCode){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_INVITE_CODE, KEY_INVITE_CODE + " = ? ", new String[] { inviteCode }) > 0;
    }
}
