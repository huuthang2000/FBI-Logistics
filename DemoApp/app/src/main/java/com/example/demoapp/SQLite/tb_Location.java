package com.example.demoapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objLocation;
import com.example.demoapp.Models.objectFirebase.account.fb_Location;

import java.util.ArrayList;
import java.util.List;


public class tb_Location {
    public static final String TAG = tb_Location.class.getSimpleName();
    private final Context context;

    private com.example.demoapp.SQLite.db_life24h mDatabase;

    private static final String TABLE_LOCATION = "tb_location";
    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_UID = "KEY_UID";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_TIME_UPDATE = "time_update";

    private static tb_Location instance;

    public static tb_Location getInstance(Context context){
        if(instance == null)
            instance = new tb_Location(context);
        return instance;
    }

    public tb_Location(Context context) {
        this.context = context;
        this.mDatabase = com.example.demoapp.SQLite.db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_LOCATION))
            createTable();
    }

    private void createTable() {
        String create_table = String.format("CREATE TABLE %s ( STT INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s REAL , %s REAL , %s TEXT )",
                TABLE_LOCATION,
                KEY_ID,
                KEY_UID,
                KEY_LATITUDE,
                KEY_LONGITUDE,
                KEY_TIME_UPDATE);

        mDatabase.getWritableDatabase().execSQL(create_table);
    }

    public void addLocation(String id, String UID , fb_Location location)
    {

        if(!checkItemExistLocation(id, UID))
        {
            ContentValues values = new ContentValues();
            values.put(KEY_ID,id); //random
            values.put(KEY_UID,UID);
            values.put(KEY_LATITUDE, location.getLatitude());
            values.put(KEY_LONGITUDE, location.getLongitude());
            values.put(KEY_TIME_UPDATE, location.getTimeUpdate());
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            db.insert(TABLE_LOCATION, null, values);

        }
//        else
//            updateLocation(id,UID,location);
    }

    private boolean updateLocation(String id, String UID, fb_Location location) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(KEY_LONGITUDE, location.getLongitude());
        values.put(KEY_TIME_UPDATE, location.getTimeUpdate());
        int result = db.update(TABLE_LOCATION, values, KEY_ID + " = ? ", new String[] { String.valueOf(id)});

        return result > 0;
    }

    /**
     * @param id ordinal numbers in the list
     * @param UID id of family
     * @return exist or not exist
     */
    private boolean checkItemExistLocation (String id, String UID){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'", TABLE_LOCATION, KEY_ID, id, KEY_UID, UID);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public fb_Location getLocations (String UID, String id)
    {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'", TABLE_LOCATION, KEY_ID, id, KEY_UID, UID);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null){
            cursor.moveToFirst();
            fb_Location location = new fb_Location(cursor.getDouble(3),
                    cursor.getDouble(4),
                    Long.parseLong(cursor.getString(5)));

            cursor.close();
            return location;
        }
        return null;
    }

    public List<objLocation> getLocationsListByUID (String UID) {
        List<objLocation>  locations = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_LOCATION, KEY_UID, UID);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            objLocation objLocation = new objLocation();
            objLocation.setKeyID(cursor.getString(1));
            fb_Location area = new fb_Location(
                    cursor.getDouble(3),
                    cursor.getDouble(4),
                    Long.parseLong(cursor.getString(5)));
            objLocation.setFb_location(area);
            locations.add(objLocation);
            cursor.moveToNext();
        }
        cursor.close();

        return locations;
    }

    /**
     * Delete account
     * @param keyID ordinal numbers in the list
     * @return delete status
     */
    public void deleteLocationByKeyID(String keyID){
        Log.d("locationList", "delete Location with "+ keyID);
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        db.delete(TABLE_LOCATION, KEY_ID + " = ?", new String[] { String.valueOf(keyID)});
    }
}
