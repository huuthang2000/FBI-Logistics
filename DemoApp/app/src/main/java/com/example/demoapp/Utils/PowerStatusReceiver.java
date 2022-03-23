package com.example.demoapp.Utils;

import static com.example.demoapp.Utils.keyUtils.NETWORK;
import static com.example.demoapp.Utils.keyUtils.accountList;
import static com.example.demoapp.Utils.keyUtils.batteryPercent;
import static com.example.demoapp.model.objAccount.getCurrentUser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class PowerStatusReceiver  extends BroadcastReceiver {
    private static final String TAG = "PowerStatusReceiver";
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(getCurrentUser() != null)
        {


                //Global.mLog = Log4jHelper.getLogger("PowerStatusReceiver");
                mFirebaseInstance = FirebaseDatabase.getInstance();
                Log.d(TAG, "ACTION_BATTERY_DEVICE");

                try {

                    // Send percentage of current battery to FireBase server
                    final DatabaseReference mRefAccount = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(accountList)
                            .child(getCurrentUser().getUid())
                            .child(batteryPercent);
                    mRefAccount.setValue(batteryLevel(context)+"%");

                    mFirebaseDatabase = mFirebaseInstance.getReference(accountList).child(getCurrentUser().getUid());
                    mFirebaseDatabase.child(NETWORK).setValue(System.currentTimeMillis()+"");
                }catch (Exception e)
                {
                    e.getMessage();
                }

                //Toast.makeText(context, " ACTION batteryLevel = "+batteryLevel(context), Toast.LENGTH_SHORT).show();
                if(batteryLevel(context)>15)
                {

                    Log.d("battery"," ACTION_BATTERY_OKAY ");
                }
                else
                {

                    Log.d("battery"," ACTION_BATTERY_LOW secondclone");
                }
            }
        }

    public static int batteryLevel(Context context)
    {
        Intent intent  = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int    level   = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int    scale   = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        int    percent = (level*100)/scale;
        return percent;
    }
}
