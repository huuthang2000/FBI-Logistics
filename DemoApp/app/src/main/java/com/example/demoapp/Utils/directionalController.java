package com.example.demoapp.Utils;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Models.objectFirebase.chat.fb_Message;
import com.example.demoapp.R;
import com.example.demoapp.view.driver.PlaceAlertsDetail;
import com.example.demoapp.view.driver.UIChat.UIChat;
import com.example.demoapp.view.driver.UIChat.UINewMessage;
import com.example.demoapp.view.driver.UIChat.UIViewImageMessage;
import com.example.demoapp.view.driver.UILoadingData;
import com.example.demoapp.view.driver.UISafety.UIAllDrivingInsights;
import com.example.demoapp.view.driver.UISafety.UIDrivingDetailOfUser;
import com.example.demoapp.view.driver.UISelectCountries;
import com.example.demoapp.view.driver.UISettings.UIEditProfile;
import com.example.demoapp.view.driver.UISettings.UIInviteFriends;
import com.example.demoapp.view.driver.UISettings.UIJoinOtherFamilies;
import com.example.demoapp.view.driver.UISetupProfile;

public class directionalController {

    /**
     *  @param context context
     * @param idChat id of chat
     * @param type
     * @param chatDetail chat detail
     */
    public static void goToUIChat(Context context, String idChat, String type, fb_Chat chatDetail) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(com.example.demoapp.Utils.keyUtils.dataChat, chatDetail);
        bundle.putString(com.example.demoapp.Utils.keyUtils.dataIDChat, idChat);
        bundle.putString(com.example.demoapp.Utils.keyUtils.dataType, type);

        Intent intent = new Intent(context, UIChat.class);

        intent.putExtra(com.example.demoapp.Utils.keyUtils.data, bundle);

        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param idChat id of chat
     * @param url url of image
     * @param message object message
     */
    public static void goToUIImageViewer(Context context, String idChat, String url, fb_Message message) {
        Bundle bundle = new Bundle();
        bundle.putString(com.example.demoapp.Utils.keyUtils.dataIDChat, idChat);
        bundle.putSerializable(com.example.demoapp.Utils.keyUtils.dataMessage, message);
        bundle.putString(com.example.demoapp.Utils.keyUtils.dataURLImage, url);

        Intent intent = new Intent(context, UIViewImageMessage.class);
        intent.putExtra(com.example.demoapp.Utils.keyUtils.data, bundle);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param phoneNumber phone number
     */
    public static void callThePhoneNumber(Context context, String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("callThePhoneNumber", "Permission not granted");
                return;
            }
        }
        context.startActivity(intent);
    }


    /**
     *
     * @param context context
     * @param type type of action
     */
    public static void goToUINewMessage(Context context, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(com.example.demoapp.Utils.keyUtils.dataType,type);
        Intent intent = new Intent(context, UINewMessage.class);
        intent.putExtra(com.example.demoapp.Utils.keyUtils.data,bundle);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param Uid user id
     */
    public static void goToUIDrivingDetailOfUser(Context context, String Uid) {
        Bundle bundle = new Bundle();
        bundle.putString(com.example.demoapp.Utils.keyUtils.dataUid,Uid);
        Intent intent = new Intent(context, UIDrivingDetailOfUser.class);
        intent.putExtra(com.example.demoapp.Utils.keyUtils.data,bundle);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIAllDrivingInsights(Context context) {
        Intent intent = new Intent(context, UIAllDrivingInsights.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIInviteFriends(Context context) {
        Intent intent = new Intent(context, UIInviteFriends.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIEditProfile(Context context) {
        Intent intent = new Intent(context, UIEditProfile.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIJoinOtherFamilies(Context context) {
        Intent intent = new Intent(context, UIJoinOtherFamilies.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUISetupProfile(Context context) {
        Intent intent = new Intent(context, UISetupProfile.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUILoadingData(Context context) {
        Intent intent = new Intent(context, UILoadingData.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param TAG  TAG activity
     */
    public static void goToUISelectCountry(Context context, String TAG) {
        Bundle bundle = new Bundle();
        bundle.putString(com.example.demoapp.Utils.keyUtils.dataType,TAG);
        Intent intent = new Intent(context, UISelectCountries.class);
        intent.putExtra(com.example.demoapp.Utils.keyUtils.data,bundle);
        context.startActivity(intent);
    }


    /**
     *
     * @param context context
     */
    public static void goToUIPlaceAlertsDetailAdd(Context context) {
        Intent intentAlert = new Intent(context, PlaceAlertsDetail.class);
        intentAlert.putExtra(com.example.demoapp.Utils.keyUtils.Place_Item_Add,true);
        context.startActivity(intentAlert);
    }

    /**
     *
     * @param context context
     */


    /**
     *
     * @param context context
     */



    public static void goToGoogleMap(Context context, double longitude, double latitude){
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=&daddr="+latitude +","+ longitude);

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            context.startActivity(mapIntent);
        }else{
            Toast.makeText(context, R.string.Please_install_the_Google_Map_application, Toast.LENGTH_SHORT).show();
        }

    }
}
