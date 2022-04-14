package com.example.demoapp.view.driver;

import static com.example.demoapp.Models.objApplication.objAccount.getCurrentUser;
import static com.example.demoapp.Utils.keyUtils.STATUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.timeUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class UIUserInternetReceiver extends BroadcastReceiver {

    //    public  static  boolean  checkConnect = true;
    //    public  static boolean checkOffline = false;
    private static final String TAG = "UIUserInternetReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // create Log4j

        if(objAccount.getCurrentUser() != null)
        {
            if(objAccount.getAccountFromSQLite(context, objAccount.getCurrentUser().getUid()) != null){
                Log.d(TAG, "ACTION_UNLOCK_Internet");

                try {
                    if(timeUtils.isNetworkAvailable(context))
                    {
                        // khi có sự kiện mới và wifi tắt, hãy lưu giá trị = true
                        // để đợi khi người dùng tắt màn hình,
                        // bật wifi lên và gửi trong 30s rồi tắt wifi như cũ.
                        // khi bật wifi, chuyển sự kiện thành false
                        // newEvent_wifi_is_Off = false;
                        Log.d("ckinetrnet","online Internet");
                        final DatabaseReference mRefAccount = FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child(keyUtils.accountList)
                                .child(getCurrentUser().getUid())
                                .child(STATUS);
                        mRefAccount.setValue("Online");

                        //checkConnect = true;


                    }else {
                        //checkConnect = false;
                        // checkOffline = true;
                        final DatabaseReference mRefAccount = FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child(keyUtils.accountList)
                                .child(getCurrentUser().getUid())
                                .child(STATUS);
                        mRefAccount.setValue("Offline");
                        Log.d("ckinetrnet","Offline Internet");


                    }

                }catch (Exception e)
                {
                    Log.d("error", e.getMessage());
                }

            }
        }


    }

}
