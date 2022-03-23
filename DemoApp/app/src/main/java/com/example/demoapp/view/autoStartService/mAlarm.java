package com.example.demoapp.view.autoStartService;

/*
 *  Date created: 02/11/2020
 *  Last updated: 02/11/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class mAlarm {
    public static final String TAG = mAlarm.class.getSimpleName();

    public static long ALARM_TIME = 60000 * 5;

    public static boolean checkDevice() {
        String manufacturer = android.os.Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        } else if ("oppo".equalsIgnoreCase(manufacturer)) {
            return true;
        } else if ("vivo".equalsIgnoreCase(manufacturer)) {
            return true;
        } else if ("Letv".equalsIgnoreCase(manufacturer)) {
            return true;
        } else return "Honor".equalsIgnoreCase(manufacturer);
    }


//    public static void startAlarm(Context context) {
//        if (checkDevice()) {
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            Intent intent = new Intent(context, AlarmReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                    1111, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ALARM_TIME, pendingIntent);
//        }
//    }
}
