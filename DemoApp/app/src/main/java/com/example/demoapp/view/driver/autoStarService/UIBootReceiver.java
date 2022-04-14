package com.example.demoapp.view.driver.autoStarService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.demoapp.view.driver.UISafety.SpeedService;
import com.example.demoapp.view.driver.UISafety.SpeedService;




public class UIBootReceiver extends BroadcastReceiver {

    @Override
        public void onReceive(Context context, Intent arg1)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context,GPSService.class));
                context.startForegroundService(new Intent(context, SpeedService.class));
            } else {
                context.startService(new Intent(context,GPSService.class));
                context.startService(new Intent(context,SpeedService.class));
            }

        }

}
