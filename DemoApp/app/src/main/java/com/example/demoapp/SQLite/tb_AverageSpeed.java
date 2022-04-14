package com.example.demoapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objSpeed;

import java.util.ArrayList;
import java.util.List;


public class tb_AverageSpeed {
    public static final String TAG = tb_AverageSpeed.class.getSimpleName();
    private final Context context;

    private com.example.demoapp.SQLite.db_life24h mDatabase;

    private static final String TABLE_AVERAGE_SPEED = "tb_AverageSpeed";
    private static final String KEY_ID = "id";
    private static final String KEY_AVERAGE_SPEED = "averageSpeed";
    private static final String KEY_TIME = "time";

    private static tb_AverageSpeed instance;

    public static tb_AverageSpeed getInstance(Context context){
        if(instance == null)
            instance = new tb_AverageSpeed(context);
        return instance;
    }

    public tb_AverageSpeed(Context context) {
        this.context = context;
        this.mDatabase = com.example.demoapp.SQLite.db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_AVERAGE_SPEED))
            createTable();
    }

    private void createTable() {
        String create_drivingDetail_table = String.format("CREATE TABLE %s ( STT INTEGER PRIMARY KEY AUTOINCREMENT , %s INTEGER , %s INTEGER , %s INTEGER )",
                TABLE_AVERAGE_SPEED,
                KEY_ID,
                KEY_AVERAGE_SPEED,
                KEY_TIME);

        mDatabase.getWritableDatabase().execSQL(create_drivingDetail_table);
    }

    public boolean addAverageSpeed(objSpeed averageSpeed){
        ContentValues values = new ContentValues();

        values.put(KEY_AVERAGE_SPEED, averageSpeed.getSpeed());
        values.put(KEY_ID, averageSpeed.getId());
        values.put(KEY_TIME, averageSpeed.getTime());

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        long result = db.insert(TABLE_AVERAGE_SPEED, null, values);

        Log.d(TAG, "addAverageSpeed");
        return result > 0;
    }

    /**
     *
     * @return latest speed
     */
    public objSpeed getLatestSpeed(){
        String query = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT 1 ", TABLE_AVERAGE_SPEED, KEY_ID);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        objSpeed speed = null;

        while(!cursor.isAfterLast())
        {
            int idDrivingDetail = cursor.getInt(1);
            double averageSpeed = cursor.getDouble(2);
            long time = cursor.getLong(3);
            speed = new objSpeed(idDrivingDetail,averageSpeed, time);
            cursor.moveToNext();
        }
        cursor.close();
        return speed;
    }

    /**
     *
     * @return list object speed
     */
    public List<objSpeed> getAllSpeed() {
        List<objSpeed>  averageSpeedList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s ", TABLE_AVERAGE_SPEED);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            int idDrivingDetail = cursor.getInt(1);
            double averageSpeed = cursor.getDouble(2);
            long time = cursor.getLong(3);
            averageSpeedList.add(new objSpeed(idDrivingDetail,averageSpeed, time));
            cursor.moveToNext();
        }
        cursor.close();
        return averageSpeedList;
    }


    public double getAverageSpeed(){
        List<objSpeed> allDriving = getAllSpeed();
        final int allSpeedSize = allDriving.size();
        double total = 0;
        for(objSpeed speed : allDriving){
            total += speed.getSpeed();
        }
        return objSpeed.round(total/allSpeedSize,2);
    }


    public void deleteAllSpeed(){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_AVERAGE_SPEED);

    }
}
