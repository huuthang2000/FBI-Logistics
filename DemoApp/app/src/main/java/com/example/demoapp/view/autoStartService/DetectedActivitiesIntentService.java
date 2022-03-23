package com.example.demoapp.view.autoStartService;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.demoapp.Utils.keyUtils;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;


public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = DetectedActivitiesIntentService.class.getSimpleName();

    public DetectedActivitiesIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        for (DetectedActivity activity : detectedActivities) {
            if(activity.getConfidence() > keyUtils.CONFIDENCE)
                broadcastActivity(activity);
        }
    }


    private void broadcastActivity(DetectedActivity activity) {
        Intent intent = new Intent(keyUtils.BROADCAST_DETECTED_ACTIVITY);
        // You can also include some extra data.
        Bundle bundle = new Bundle();
        bundle.putParcelable(keyUtils.activity, activity);
        intent.putExtra(keyUtils.data, bundle);
        LocalBroadcastManager.getInstance(DetectedActivitiesIntentService.this).sendBroadcast(intent);
    }


}
