package com.example.demoapp.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class gpsUtils {

    private final Context context;
    private LocationManager manager;
    private Location location;

    private FusedLocationProviderClient mFusedLocationClient;

    private static gpsUtils instance;

    private informationLocation listener;

    public void getInfomationLocation(informationLocation listener){
        this.listener = listener;
    }

    public interface informationLocation{
        void latitudeAndLongitude(double latitude, double longitude);
    }

    public static gpsUtils getInstance(Context context){
        if(instance == null)
            instance = new gpsUtils(context);
        return instance;
    }

    @SuppressLint("MissingPermission")
    private gpsUtils(Context context) {
        this.context = context;
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public boolean isGPS(){
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(final informationLocation listener){
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                listener.latitudeAndLongitude(location.getLatitude(),location.getLongitude());
            }
        });
    }


}
