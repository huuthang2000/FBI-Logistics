package com.example.demoapp.view.driver.UISafety;


import static com.example.demoapp.Models.objApplication.objAccount.getCurrentUser;
import static com.example.demoapp.Utils.keyUtils.LOWEST_SPEED_TO_WARN;
import static com.example.demoapp.Utils.keyUtils.MIN_SPEED_AVERAGE;
import static com.example.demoapp.Utils.keyUtils.NETWORK;
import static com.example.demoapp.Utils.keyUtils.THE_FASTEST_SPEED_TO_WARN;
import static com.example.demoapp.Utils.keyUtils.accountList;
import static com.example.demoapp.Utils.notificationUtils.buildNotificationService;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.demoapp.Models.objApplication.objDrivingDetail;
import com.example.demoapp.Models.objApplication.objSpeed;
import com.example.demoapp.Presenters.Safety.pre_Safety;
import com.example.demoapp.SQLite.tb_AverageSpeed;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.view.driver.DetectActivitys.DetectedActivitiesIntentService;
import com.example.demoapp.view.driver.autoStarService.mAlarm;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;

public class SpeedService extends Service implements IBaseGpsListener {
    private final Context context = this;
    private final String TAG = SpeedService.class.getSimpleName();

    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    private ActivityRecognitionClient mActivityRecognitionClient;

    public static boolean isDriving;
    private boolean isWarning;

    private int ONE_SECONDS = 1000;
    private int ONE_MINUTES = 1000;

    private CountDownTimer countDownTimer;


    private BroadcastReceiver mActivitiesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Bundle b = intent.getBundleExtra(keyUtils.data);
            assert b != null;
            DetectedActivity detectedActivity = b.getParcelable(keyUtils.activity);
            handleUserActivity(detectedActivity.getType());
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        isDriving = false;
        isWarning = false;

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext()));
        localBroadcastManager.registerReceiver(mActivitiesReceiver,new IntentFilter(keyUtils.BROADCAST_DETECTED_ACTIVITY));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if(mAlarm.checkDevice()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(3, buildNotificationService(context));
            }
        }

        countDownTimer = new CountDownTimer(ONE_MINUTES,ONE_SECONDS) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(getCurrentUser()!=null){
                    final DatabaseReference mRefAccount = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(accountList)
                            .child(getCurrentUser().getUid())
                            .child(NETWORK);
                    mRefAccount.setValue(System.currentTimeMillis()+"");
                }

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                assert locationManager != null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, SpeedService.this);
                updateSpeed(null);

                //Restart
                countDownTimer.start();
            }
        };

        mActivityRecognitionClient = new ActivityRecognitionClient(this);
        mIntentService = new Intent(this, DetectedActivitiesIntentService.class);
        mPendingIntent = PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
        requestActivityUpdatesButtonHandler();

        return START_STICKY;
    }

    public void requestActivityUpdatesButtonHandler() {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                keyUtils.DETECTION_INTERVAL_IN_MILLISECONDS,
                mPendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG,"Successfully requested activity updates");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Requesting activity updates failed to start: " + e.getMessage());
            }
        });
    }

    public void removeActivityUpdatesButtonHandler() {
        Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(
                mPendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.getMessage());
            }
        });
    }

    private void handleUserActivity(int type) {

        if(type == DetectedActivity.IN_VEHICLE || type == DetectedActivity.ON_BICYCLE){
            isDriving = true;
            countDownTimer.start();

        }

        else{

            if(isDriving){

                isDriving = false;

                isWarning = true;

                double averageSpeed = tb_AverageSpeed.getInstance(context).getAverageSpeed();

                pre_Safety.setDrivingDetails(getCurrentSpeed(context), averageSpeed, new pre_Safety.onResultDrivingDetails() {
                    @Override
                    public void onResult(boolean isSuccess, objDrivingDetail drivingDetail, String message) {
                        //Reset
                        tb_AverageSpeed.getInstance(context).deleteAllSpeed();
                        saveTopSpeed(context,0);
                    }
                });

                countDownTimer.cancel();
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeActivityUpdatesButtonHandler();
        isDriving = false;
    }

    private void updateSpeed(MySpeed location) {

        float nCurrentSpeed = 0;

        if(location != null)
        {
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);

        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        double currentSpeed = objSpeed.round(Double.parseDouble(strCurrentSpeed),2);

        final objSpeed latestSpeed = tb_AverageSpeed.getInstance(context).getLatestSpeed();

        //Notification of abnormal speed when driving
        if(tb_AverageSpeed.getInstance(context).getLatestSpeed() != null){
            if(currentSpeed <= LOWEST_SPEED_TO_WARN &&
                    latestSpeed.getSpeed() > THE_FASTEST_SPEED_TO_WARN &&
                    Calendar.getInstance().getTimeInMillis() - latestSpeed.getTime() <= 1000){

                final DatabaseReference mRef = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("test")
                        .push();
                mRef.child("time").setValue(Calendar.getInstance().getTimeInMillis());
                mRef.child("time").setValue(currentSpeed);

              if(isWarning){

                  isWarning = false;

                }

            }
        }

        //If the speed is greater than 10, start saving
        if(currentSpeed > MIN_SPEED_AVERAGE )
            tb_AverageSpeed.getInstance(context).addAverageSpeed(new objSpeed(Double.parseDouble(strCurrentSpeed)));

        saveTopSpeed(context,currentSpeed);

    }

    public static void saveTopSpeed(Context context, double currentSpeed){
        double oldSpeed = getCurrentSpeed(context);
        if(oldSpeed != 0){
            if(currentSpeed > oldSpeed){
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(keyUtils.topSpeed, String.valueOf(currentSpeed));
                editor.apply();
            }

        }else{
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(keyUtils.topSpeed, String.valueOf(currentSpeed));
            editor.apply();
        }
    }



    /**
     * getCurrentFamilyID
     * @param context context
     * @return current id
     */
    public static double getCurrentSpeed(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String rs = sharedPreferences.getString(keyUtils.topSpeed,"");
        if(rs.matches(""))
            return 0;
        else{
            try{
                return Double.parseDouble(rs);
            }catch (Exception e){
                return 0;
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if(location != null && isDriving)
        {
            MySpeed myLocation = new MySpeed(location);
            updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG,"onTaskRemoved");
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);
        //mAlarm.startAlarm(this);
    }
}