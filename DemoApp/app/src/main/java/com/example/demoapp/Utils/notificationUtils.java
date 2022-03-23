package com.example.demoapp.Utils;



import static com.example.demoapp.Utils.keyUtils.CHANNEL_ID;
import static com.example.demoapp.Utils.keyUtils.KEY_TEXT_REPLY;
import static com.example.demoapp.Utils.keyUtils.TAG_NOTIFICATION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

//import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.R;
//import com.example.demoapp.Views.UIChat.UIChat;
//import com.example.demoapp.Views.UIChat.UIListChat;
import com.example.demoapp.view.driver.DriverActivity;
//import com.example.demoapp.Views.notifications.directReplyReceiver;

import java.util.Objects;

public class notificationUtils {


    // GPSNotification
    public static void buildNotificationGPS(Context context, int notificationID, String title, String message) {

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Create GPSNotification builder
        NotificationCompat.Builder mBuilder;

        //Initialise ContentIntent
        Intent ContentIntent = new Intent(context, DriverActivity.class);
        ContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent ContentPendingIntent = PendingIntent.getActivity(context,
                0,
                ContentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.taubien)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(context.getResources().getColor(R.color.black))
                .setAutoCancel(true)
                .setContentIntent(ContentPendingIntent)
               // .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.quite_impressed))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                    "Notification GPS",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setDescription("Notification of GPS");

            mBuilder.setChannelId(CHANNEL_ID);
            Objects.requireNonNull(mNotificationManager).createNotificationChannel(mChannel);
        }

        Objects.requireNonNull(mNotificationManager).notify(TAG_NOTIFICATION,notificationID, mBuilder.build());
    }



    // messageNotification
//    public static void buildNotificationMessage(Context context, int notificationID, String title, String message, String idChat, fb_Chat chatDetail) {
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        //Create messageNotification builder
//        NotificationCompat.Builder mBuilder;
//
//
//        //Initialise RemoteInput
//        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("Reply").build();
//        Bundle bundle = new Bundle();
//
////        bundle.putString(com.example.demoapp.Utils.keyUtils.dataIDChat,idChat);
////        bundle.putSerializable(com.example.demoapp.Utils.keyUtils.dataChat,chatDetail);
////        bundle.putSerializable(com.example.demoapp.Utils.keyUtils.dataType, UIListChat.TAG);
////
////        Intent replyIntent = new Intent(context, directReplyReceiver.class);
////        replyIntent.putExtra(com.family.life24h.Utils.keyUtils.data,bundle);
////        replyIntent.putExtra(com.family.life24h.Utils.keyUtils.notificationId, notificationID);
//        replyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(context,
//                0,
//                replyIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(R.drawable.ic_chat_black_24dp, "Reply", replyPendingIntent)
//                .addRemoteInput(remoteInput)
//                .setShowsUserInterface(true)
//                .build();
//
//
//        //Initialise ContentIntent
//        Intent ContentIntent = new Intent(context, UIChat.class);
//        ContentIntent.putExtra(com.family.life24h.Utils.keyUtils.data,bundle);
//        ContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent ContentPendingIntent = PendingIntent.getActivity(context,
//                0,
//                ContentIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        mBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.icon_notification)
//                .setContentTitle(title)
//                .setContentText(message)
//                .addAction(replyAction)
//                .setColor(context.getResources().getColor(R.color.colorThemePurple))
//                .setAutoCancel(true)
//                .setContentIntent(ContentPendingIntent)
//                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.quite_impressed))
//                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
//                    "Notification message",
//                    NotificationManager.IMPORTANCE_HIGH);
//            mChannel.enableLights(true);
//            mChannel.enableVibration(true);
//            mChannel.setDescription("Notification");
//
//            mBuilder.setChannelId(CHANNEL_ID);
//            Objects.requireNonNull(mNotificationManager).createNotificationChannel(mChannel);
//        }
//
//        Objects.requireNonNull(mNotificationManager).notify(TAG_NOTIFICATION,notificationID, mBuilder.build());
//    }
//
//    public static Notification buildNotificationService(Context context){
//        Notification notification = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            final String CHANNEL_ID = "Notification_Life24h";
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
//                    "Notification_Life24h",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
//                    .build();
//        }
//
//        return notification;
//    }
}
