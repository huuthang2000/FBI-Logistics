package com.example.demoapp.view.driver;

import static com.example.demoapp.Models.objApplication.objAccount.getCurrentUser;
import static com.example.demoapp.Utils.keyUtils.GPS;
import static com.example.demoapp.Utils.keyUtils.STATUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Utils.keyUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class gpsSwitchStateReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if(objAccount.getCurrentUser() != null) {
            if (objAccount.getAccountFromSQLite(context, objAccount.getCurrentUser().getUid()) != null) {
                if (intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {

                    //Log.d("ZXZ"," GPS turned on");
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    try {
                        if (isGpsEnabled || isNetworkEnabled) {
                            // Handle Location turned ON
                            Log.d("ZXZ"," GPS turned on");
                            final DatabaseReference mRefAccount = FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child(keyUtils.accountList)
                                    .child(getCurrentUser().getUid())
                                    .child(GPS)
                                    .child(STATUS);
                            mRefAccount.setValue(true);
                        } else {
                            // Handle Location turned OFF
                            Log.d("ZXZ"," GPS turned off");
                            final DatabaseReference mRefAccount = FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child(keyUtils.accountList)
                                    .child(getCurrentUser().getUid())
                                    .child(GPS)
                                    .child(STATUS);
                            mRefAccount.setValue(false);
                        }

                    }
                    catch (Exception e)
                    {
                        Log.d("error",e.getMessage());
                    }
                }
            }
        }
    }


}



