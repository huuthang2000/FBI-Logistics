package com.example.demoapp.view.driver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.util.Log;

import com.example.demoapp.Utils.PowerStatusReceiver;



public abstract class UIMonitorApplication extends Application {

    private static final String TAG = "UIAppController";

    private BroadcastReceiver receiver;

    public static final String TAG_APP_WORKER = "task_app_worker";

    private static UIMonitorApplication instance;

    public UIMonitorApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }


    // uncaught exception handler variable
    private Thread.UncaughtExceptionHandler defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

    // handler listenery

    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            try
            {
                defaultUEH.uncaughtException(thread, ex);
            }
            catch(Exception e)
            {
                e.getMessage();
            }

        }
    };


    BroadcastReceiver userOutInternet = new  UIUserInternetReceiver();

    // Broadcast Save battery
    BroadcastReceiver powerStatusReceiver = new PowerStatusReceiver();
    //gpsSwitchStateReceiver
    BroadcastReceiver gpsSwitchStateReceiver = new gpsSwitchStateReceiver();


    @Override
    public void onCreate() {

        super.onCreate();

        try
        {
            try
            {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }
            catch(Exception e)
            {
                Log.d(TAG,e.getMessage()+"");
            }

            // thiết lập trình xử lý cho ngoại lệ không cần thiết và bắt đầu lại ứng dụng nếu có ngoại lệ
            Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);

            IntentFilter checkWifi = new IntentFilter();
            checkWifi.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            checkWifi.setPriority(999);
            this.registerReceiver(userOutInternet, checkWifi);

            IntentFilter saveBattery = new IntentFilter();
            //saveBattery.addAction(Intent.ACTION_BATTERY_LOW);
            saveBattery.addAction(Intent.ACTION_BATTERY_CHANGED);
            //saveBattery.addAction(Intent.ACTION_BATTERY_OKAY);
            saveBattery.setPriority(999);
            this.registerReceiver(powerStatusReceiver, saveBattery);

            IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
            this.registerReceiver(gpsSwitchStateReceiver, filter);

//            final PeriodicWorkRequest periodicWorkRequest
//                    = new PeriodicWorkRequest.Builder(UIAppWorker.class, 30, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
//                    .build();

//            WorkManager.getInstance().enqueueUniquePeriodicWork(TAG_APP_WORKER, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);

        }
        catch(Exception e)
        {
            Log.d(TAG,  e.getMessage()+"");
        }
    }






}
