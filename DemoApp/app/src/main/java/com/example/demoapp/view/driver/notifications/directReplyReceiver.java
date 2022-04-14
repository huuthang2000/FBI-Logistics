package com.example.demoapp.view.driver.notifications;

import static com.example.demoapp.Utils.keyUtils.KEY_TEXT_REPLY;
import static com.example.demoapp.Utils.keyUtils.TAG_NOTIFICATION;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.RemoteInput;

import com.example.demoapp.Presenters.Chat.pre_Chat;
import com.example.demoapp.R;
import com.example.demoapp.Utils.keyUtils;




public class directReplyReceiver extends BroadcastReceiver {


    private String dsdssd = "dsđssđs";
    @Override
    public void onReceive(Context context, Intent intent) {
        processInlineReply(context,intent);
    }

    private void processInlineReply(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        Bundle bundle = intent.getBundleExtra(keyUtils.data);
        Log.d("ssds",dsdssd );
        if (remoteInput != null) {

            String idChat = bundle.getString(keyUtils.dataIDChat);

            String message = remoteInput.getCharSequence(KEY_TEXT_REPLY).toString();
            
            if(idChat!=null){

                pre_Chat.postMessageFromNotification(idChat, message, new pre_Chat.onResultPostMessageNotification() {
                    @Override
                    public void onResult(boolean isSuccess, String message) {
                        if(isSuccess){
                            NotificationManager mNotificationManager =
                                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                            int id = intent.getIntExtra(keyUtils.notificationId, -1);
                            // mNotificationManager.cancelAll();
                            mNotificationManager.cancel(TAG_NOTIFICATION,id);

                            Toast.makeText(context, R.string.Successful_message_reply, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, R.string.Reply_message_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            
        }
    }
}
