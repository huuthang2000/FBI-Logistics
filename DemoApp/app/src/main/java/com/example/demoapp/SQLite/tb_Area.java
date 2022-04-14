package com.example.demoapp.SQLite;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objArea;
import com.example.demoapp.Models.objectFirebase.family.fb_area;

import java.util.ArrayList;
import java.util.List;

public class tb_Area {
    public static final String TAG = tb_Area.class.getSimpleName();

    private final Context context;

    private com.example.demoapp.SQLite.db_life24h mDatabase;

    private static final String TABLE_AREA = "tb_area";
    private static final String KEY_ID_FAMILY = "id_family";
    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_RADIUS = "radius";
    private static final String KEY_REGION_NAME = "regionName";

    private static tb_Area instance;

    public static tb_Area getInstance(Context context){
        if(instance == null)
            instance = new tb_Area(context);
        return instance;
    }

    public tb_Area(Context context) {
        this.context = context;
        this.mDatabase = com.example.demoapp.SQLite.db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_AREA))
            createTable();
    }

    private void createTable() {
        String create_area_list_table = String.format("CREATE TABLE %s ( STT INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT ,%s TEXT , %s REAL , %s REAL , %s INTEGER , %s TEXT )",
                TABLE_AREA,
                KEY_ID,
                KEY_ID_FAMILY,
                KEY_LATITUDE,
                KEY_LONGITUDE,
                KEY_RADIUS,
                KEY_REGION_NAME);

        mDatabase.getWritableDatabase().execSQL(create_area_list_table);
    }

    public boolean addArea(String id, String idFamily, fb_area area){
        if(!checkItemExist(id, idFamily)){
            ContentValues values = new ContentValues();
            values.put(KEY_ID,id);
            values.put(KEY_ID_FAMILY,idFamily);
            values.put(KEY_LATITUDE, area.getLatitude());
            values.put(KEY_LONGITUDE, area.getLongitude());
            values.put(KEY_RADIUS, area.getRadius());
            values.put(KEY_REGION_NAME, area.getRegionName());
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long rs = db.insert(TABLE_AREA, null, values);


            return rs > 0;
        }
        return false;
    }

    public void addOrUpdateArea(String id, String idFamily , fb_area area) {
        if(!checkItemExist(id, idFamily))
            addArea(id,idFamily,area);
        else
            updateArea(id,idFamily,area);

    }

    public boolean updateArea(String id, String idFamily, fb_area area) {

        if(checkItemExist(id, idFamily)) {
            ContentValues values = new ContentValues();

            values.put(KEY_LATITUDE, area.getLatitude());
            values.put(KEY_LONGITUDE, area.getLongitude());
            values.put(KEY_RADIUS, area.getRadius());
            values.put(KEY_REGION_NAME, area.getRegionName());
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            int result = db.update(TABLE_AREA, values, KEY_ID + " = ? AND " + KEY_ID_FAMILY + " = ? ", new String[]{String.valueOf(id), idFamily});


            return result > 0;
        }
        return false;
    }

    /**
     *
     * @param id ordinal numbers in the list
     * @param idFamily id of family
     * @return exist or not exist
     */
    private boolean checkItemExist(String id, String idFamily){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'", TABLE_AREA, KEY_ID, id, KEY_ID_FAMILY, idFamily);
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
     * @param idFamily id of family
     * @param id id
     * @return object area
     */
    public fb_area getArea(String idFamily, String id){
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'", TABLE_AREA, KEY_ID, id, KEY_ID_FAMILY, idFamily);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null){
            if(!cursor.isClosed()){
                if(cursor.moveToFirst()){
                    fb_area area = new fb_area(cursor.getDouble(2),
                            cursor.getDouble(3),
                            cursor.getInt(4),
                            cursor.getString(5));

                    cursor.close();
                    return area;
                }
            }
        }
        return null;
    }

    /**
     * get area list
     * @param idFamily id of family
     * @return object area list
     */
    public List<objArea> getAreaListByIDFamily(String idFamily) {
        List<objArea>  areaList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_AREA, KEY_ID_FAMILY, idFamily);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            objArea area = new objArea(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getDouble(4),
                    cursor.getInt(5),
                    cursor.getString(6));
            areaList.add(area);
            cursor.moveToNext();
        }
        cursor.close();

        return areaList;
    }

    /**
     * Delete Area
     * @param idFamily id of family
     * @param id ordinal numbers in the list
     * @return delete status
     */
    public boolean deleteArea(String idFamily, String id){
        Log.d(TAG, "Delete area");
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TABLE_AREA, KEY_ID + " = ? AND " + KEY_ID_FAMILY + " = ? ", new String[] { String.valueOf(id),idFamily }) > 0;
    }
}
