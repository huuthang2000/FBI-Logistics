package com.example.demoapp.view.driver.autoStarService;



import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.demoapp.view.driver.UISafety.SpeedService;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        com.example.demoapp.view.driver.autoStarService.mAlarm.startAlarm(context);

        if(!isMyServiceRunning(context, GPSService.class)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context,GPSService.class));
            } else {
                context.startService(new Intent(context,GPSService.class));
            }
        }

        if(!isMyServiceRunning(context, SpeedService.class)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context,SpeedService.class));
            } else {
                context.startService(new Intent(context,SpeedService.class));
            }
        }

    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
