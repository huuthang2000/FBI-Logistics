package com.example.demoapp.view.driver.DetectActivitys;

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

        // Lấy danh sách các hoạt động có thể xảy ra liên quan đến trạng thái hiện tại của
        // thiết bị. Mỗi hoạt động được liên kết với một mức độ tin cậy, mức này nằm ở giữa
        // 0 và 100
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
