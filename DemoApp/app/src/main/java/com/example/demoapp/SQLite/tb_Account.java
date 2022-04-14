package com.example.demoapp.SQLite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objectFirebase.account.fb_Gps;
import com.example.demoapp.Models.objectFirebase.account.fb_Location;

import java.util.ArrayList;
import java.util.List;

public class tb_Account {

    public static final String TAG = tb_Account.class.getSimpleName();

    private final Context context;

    private com.example.demoapp.SQLite.db_life24h mDatabase;

    private static final String TABLE_ACCOUNT = "tb_account";

    //TEXT
    private static final String KEY_ID = "id";
    //TEXT
    private static final String KEY_AVATAR = "avatar";
    //TEXT
    private static final String KEY_LOCAL_AVATAR = "localAvatar";
    //TEXT
    private static final String KEY_EMAIL = "email";
    //INTEGER
    private static final String KEY_STATUS_GPS = "statusGps";
    //INTEGER
    private static final String KEY_TIME_UPDATE_GPS = "timeUpdateGps";
    //TEXT
    private static final String KEY_TOKEN = "keyToken";
    //REAL
    private static final String KEY_LOCATION_LONGITUDE = "longitude";
    //REAL
    private static final String KEY_LOCATION_LATITUDE = "latitude";
    //INTEGER
    private static final String KEY_LOCATION_TIME_UPDATE = "timeUpdateLocation";
    //TEXT
    private static final String KEY_NAME = "name";
    //TEXT
    private static final String KEY_PHONE = "phone";
    //TEXT
    private static final String KEY_BATTERY_PERCENT = "batteryPercent";
    //TEXT
    private static final String KEY_GENDER = "gender";
    //TEXT
    private static final String KEY_STATUS = "status";
    //TEXT
    private static final String KEY_NETWORK = "network";

    private static final String KEY_BOUGHT = "bought";

    private static final String KEY_TIME_CREATE = "timeCreate";

    //TEXT
    private static final String KEY_TYPE = "type";

    private static tb_Account instance;

    public static tb_Account getInstance(Context context){
        if(instance == null)
            instance = new tb_Account(context);
        return instance;
    }

    private tb_Account(Context context) {
        this.context = context;
        this.mDatabase = com.example.demoapp.SQLite.db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_ACCOUNT))
            createTable();
    }

    private void createTable(){
        final String create_account_table = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY , %s TEXT , %s TEXT , %s TEXT , %s INTEGER , %s INTEGER , %s TEXT , %s REAL , %s REAL , %s INTEGER , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s INTEGER , %s INTEGER , %s TEXT )",
                TABLE_ACCOUNT,
                KEY_ID,
                KEY_AVATAR,
                KEY_LOCAL_AVATAR,
                KEY_EMAIL,
                KEY_STATUS_GPS,
                KEY_TIME_UPDATE_GPS,
                KEY_TOKEN,
                KEY_LOCATION_LONGITUDE,
                KEY_LOCATION_LATITUDE,
                KEY_LOCATION_TIME_UPDATE,
                KEY_NAME,
                KEY_PHONE,
                KEY_BATTERY_PERCENT,
                KEY_GENDER,
                KEY_STATUS,
                KEY_NETWORK,
                KEY_TIME_CREATE,
                KEY_BOUGHT,
                KEY_TYPE);
        mDatabase.getWritableDatabase().execSQL(create_account_table);
    }

    public void addOrUpdateAccount(objAccount account) {
        if(!checkItemExistByID(account.getId())){
            addAccount(account);
        }
        else{
            updateAccount(account);
        }
    }

    public boolean addAccount(objAccount account){

        if(!checkItemExistByID(account.getId())){
            ContentValues values = new ContentValues();
            values.put(KEY_ID, account.getId());
            values.put(KEY_AVATAR, account.getAvatar());

            values.put(KEY_LOCAL_AVATAR, account.getLocalAvatar());

            values.put(KEY_EMAIL, account.getEmail());
            values.put(KEY_GENDER, account.getGender());
            values.put(KEY_STATUS_GPS, account.getIntStatusGPS());
            values.put(KEY_TIME_UPDATE_GPS, account.getGps().getTimeUpdate());
            values.put(KEY_TOKEN, account.getKeyToken());
            values.put(KEY_LOCATION_LONGITUDE, account.getLocation().getLongitude());
            values.put(KEY_LOCATION_LATITUDE, account.getLocation().getLatitude());
            values.put(KEY_LOCATION_TIME_UPDATE, account.getLocation().getTimeUpdate());
            values.put(KEY_NAME, account.getName());
            values.put(KEY_PHONE, account.getPhone());
            values.put(KEY_BATTERY_PERCENT, account.getBatteryPercent());
            values.put(KEY_STATUS, account.getStatus());
            values.put(KEY_NETWORK, account.getNetwork());
            values.put(KEY_TIME_CREATE, account.getTimeCreate());
            values.put(KEY_BOUGHT, account.isBought() ? 1 : 0);
            values.put(KEY_TYPE, account.getType());

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.insert(TABLE_ACCOUNT, null, values);

            Log.d(TAG, "Add account to SQLite");

            return result > 0;
        }else
            return false;

    }

    public boolean updateAccount(objAccount account){
        if(checkItemExistByID(account.getId())){
            ContentValues values = new ContentValues();

            values.put(KEY_AVATAR, account.getAvatar());
            values.put(KEY_LOCAL_AVATAR, account.getLocalAvatar());
            values.put(KEY_EMAIL, account.getEmail());
            values.put(KEY_GENDER, account.getGender());
            values.put(KEY_STATUS_GPS, account.getIntStatusGPS());
            values.put(KEY_TIME_UPDATE_GPS, account.getGps().getTimeUpdate());
            values.put(KEY_TOKEN, account.getKeyToken());
            values.put(KEY_LOCATION_LONGITUDE, account.getLocation().getLongitude());
            values.put(KEY_LOCATION_LATITUDE, account.getLocation().getLatitude());
            values.put(KEY_LOCATION_TIME_UPDATE, account.getLocation().getTimeUpdate());
            values.put(KEY_NAME, account.getName());
            values.put(KEY_PHONE, account.getPhone());
            values.put(KEY_BATTERY_PERCENT, account.getBatteryPercent());
            values.put(KEY_STATUS, account.getStatus());
            values.put(KEY_NETWORK, account.getNetwork());
            values.put(KEY_TIME_CREATE, account.getTimeCreate());
            values.put(KEY_BOUGHT, account.isBought() ? 1 : 0);
            values.put(KEY_TYPE, account.getType());

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.update(TABLE_ACCOUNT, values, KEY_ID + " = ?", new String[] { String.valueOf(account.getId()) });

            Log.d(TAG, "Update account to SQLite");
            return result > 0;
        }else
            return false;

    }

    public boolean updateAccount(String id, String pathAvatar, String name, String gender, String phone){
        if(checkItemExistByID(id)){
            ContentValues values = new ContentValues();

            if(!pathAvatar.matches(""))
                values.put(KEY_AVATAR, pathAvatar);

            values.put(KEY_GENDER, gender);
            values.put(KEY_NAME, name);
            values.put(KEY_PHONE, phone);

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.update(TABLE_ACCOUNT, values, KEY_ID + " = ?", new String[] { String.valueOf(id) });

            Log.d(TAG, "Update account to SQLite");
            return result > 0;
        }else
            return false;

    }

    public boolean updateLocalAvatar(String id, String localAvatar){
        if(checkItemExistByID(id)){
            ContentValues values = new ContentValues();

            values.put(KEY_LOCAL_AVATAR, localAvatar);

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.update(TABLE_ACCOUNT, values, KEY_ID + " = ? ", new String[] { String.valueOf(id) });

            Log.d(TAG, "Update localAvatar account to SQLite");
            return result > 0;
        }else
            return false;

    }

    /**
     * Delete account
     * @param idAccount id of Account
     * @return delete status
     */
    public boolean deleteAccount(String idAccount){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_ACCOUNT, KEY_ID + "= ?", new String[]{idAccount}) > 0;
    }

    /**
     * Check if an item exists by id
     * @param idAccount id account
     * @return exist or not exist
     */
    private boolean checkItemExistByID(String idAccount){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_ACCOUNT, KEY_ID , idAccount);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public objAccount getAccountByID(String idAccount){
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_ACCOUNT, KEY_ID , idAccount);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null){
            if(cursor.moveToFirst()){
                objAccount account = new objAccount(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        new fb_Gps(cursor.getInt(4) != 0, cursor.getInt(5)),
                        cursor.getString(6),
                        new fb_Location(cursor.getDouble(8),cursor.getDouble(7),Long.parseLong(cursor.getString(9))),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getLong(16),
                        cursor.getInt(17) != 0,
                        cursor.getString(18));

                cursor.close();
                return account;
            }
        }
        return null;
    }



    public List<objAccount> getAllAccount() {
        List<objAccount>  accountList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", TABLE_ACCOUNT);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            objAccount account = new objAccount(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    new fb_Gps(cursor.getInt(4) != 0, cursor.getInt(5)),
                    cursor.getString(6),
                    new fb_Location(cursor.getDouble(8),cursor.getDouble(7),Long.parseLong(cursor.getString(9))),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getString(14),
                    cursor.getString(15),
                    cursor.getLong(16),
                    cursor.getInt(17) != 0,
                    cursor.getString(18));
            accountList.add(account);
            cursor.moveToNext();
        }
        cursor.close();

        return accountList;
    }

}
