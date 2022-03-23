package com.example.demoapp.model;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.demoapp.R;
import com.example.demoapp.Utils.keyUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;


public class objAccount extends fb_Account implements Serializable {

    private String id;
    private String localAvatar;

    public objAccount(String id, String avatar, String localAvatar, String email, fb_Gps fbGps, String keyToken, fb_Location fbLocation, String name, String phone, String pin, String gender, String status,String network, long timeCreate, boolean isBought, String type) {
        super(avatar, email, fbGps, keyToken, fbLocation, name, phone, pin, gender, status,network, timeCreate, isBought, type);
        this.id = id;
        this.localAvatar = localAvatar;
    }

    public objAccount(String id, fb_Account fbAccount) {
        super(fbAccount.getAvatar(),
                fbAccount.getEmail(),
                fbAccount.getGps(),
                fbAccount.getKeyToken(),
                fbAccount.getLocation(),
                fbAccount.getName(),
                fbAccount.getPhone(),
                fbAccount.getBatteryPercent(),
                fbAccount.getGender(),
                fbAccount.getStatus(),
                fbAccount.getNetwork(),
                fbAccount.getTimeCreate(),
                fbAccount.isBought(),
                fbAccount.getType());
        this.id = id;
        this.localAvatar = "";
    }

    public String getLocalAvatar() {
        if(localAvatar == null )
            return "";
        return localAvatar;
    }

    public void setLocalAvatar(String localAvatar) {
        this.localAvatar = localAvatar;
    }

    public objAccount() {
        super();
    }

    public fb_Account getFireBaseAccount(){
        return new fb_Account(getAvatar(),getEmail(),getGps(),getKeyToken(),getLocation(),getName(),getPhone(), getBatteryPercent(),getGender(),getStatus(),getNetwork(),getTimeCreate(), isBought() ,getType());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAvatar() {
        return super.getAvatar();
    }

    @Override
    public void setAvatar(String avatar) {
        super.setAvatar(avatar);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public fb_Gps getGps() {
        return super.getGps();
    }

    @Override
    public void setGps(fb_Gps fbGps) {
        super.setGps(fbGps);
    }

    @Override
    public String getKeyToken() {
        return super.getKeyToken();
    }

    @Override
    public void setKeyToken(String keyToken) {
        super.setKeyToken(keyToken);
    }

    @Override
    public fb_Location getLocation() {
        return super.getLocation();
    }

    @Override
    public void setLocation(fb_Location fbLocation) {
        super.setLocation(fbLocation);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getPhone() {
        return super.getPhone();
    }

    @Override
    public void setPhone(String phone) {
        super.setPhone(phone);
    }

    @Override
    public String getBatteryPercent() {
        return super.getBatteryPercent();
    }

    @Override
    public void setBatteryPercent(String batteryPercent) {
        super.setBatteryPercent(batteryPercent);
    }

    @Override
    public String getGender() {
        return super.getGender();
    }

    @Override
    public void setGender(String gender) {
        super.setGender(gender);
    }

    @Override
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(String status) {
        super.setStatus(status);
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public void setNetwork(String network) {
        super.setNetwork(network);
    }
    @Override
    public String getNetwork() {
        return super.getNetwork();
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    public int getIntStatusGPS(){
        return getGps().getStatus() ? 1 : 0;
    }




    /**
     * get current user
     * @return Current user
     */
    public static FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Get information of Account not realtime
     * @param uID user id of account
     * @param iInformation interface information account
     */
    public static void getAccountInformationForSingleListener(String uID, final information iInformation){
        DatabaseReference mRefAccount = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(keyUtils.accountList)
                .child(uID);

        mRefAccount.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null && iInformation != null){
                    iInformation.accountInformation(new objAccount(dataSnapshot.getKey(),
                            dataSnapshot.getValue(fb_Account.class)), "Success");
                }
                //Get information fail
                else{
                    if(iInformation != null){
                        iInformation.accountInformation(null, "Fail");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(iInformation != null){
                    iInformation.accountInformation(null, databaseError.getMessage() );
                }
                Log.e("CheckApp",databaseError.getMessage());
            }
        });
    }

    /**
     * Get information of Account realtime
     * @param uID user id
     * @param iInformation interface result
     */
    public static void getAccountInformationListener(String uID, final information iInformation){
        DatabaseReference mRefAccount = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(keyUtils.accountList)
                .child(uID);

        mRefAccount.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null && iInformation != null){
                    iInformation.accountInformation(new objAccount(dataSnapshot.getKey(),
                            dataSnapshot.getValue(fb_Account.class)), "Success");
                }
                //Get information fail
                else{
                    if(iInformation != null){
                        iInformation.accountInformation(null, "Fail");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(iInformation != null){
                    iInformation.accountInformation(null, databaseError.getMessage() );
                }
                Log.e("CheckApp",databaseError.getMessage());
            }
        });
    }

    public interface information{
        void accountInformation(objAccount accountDetail, String message);
    }

    /**
     *
     * @param context context
     * @param percentBattery percent battery (100%)
     * @return icon battery
     */
    public static Drawable getBatteryIcon(Context context, String percentBattery, int color){

        Drawable battery20 = context.getResources().getDrawable(R.drawable.ic_battery_20_black_20dp);
        Drawable battery30 = context.getResources().getDrawable(R.drawable.ic_battery_20_black_20dp);
        Drawable battery50 = context.getResources().getDrawable(R.drawable.ic_battery_20_black_20dp);
        Drawable battery60 = context.getResources().getDrawable(R.drawable.ic_battery_20_black_20dp);
        Drawable battery80 = context.getResources().getDrawable(R.drawable.ic_battery_20_black_20dp);
        Drawable battery90 = context.getResources().getDrawable(R.drawable.ic_battery_20_black_20dp);
        Drawable batteryFull = context.getResources().getDrawable(R.drawable.ic_battery_20_black_20dp);
        Drawable batteryAlert = context.getResources().getDrawable(R.drawable.ic_battery_20_black_20dp);

        //Set color drawable
        if(color != -1){
            battery20.setTint(context.getResources().getColor(color));
            battery30.setTint(context.getResources().getColor(color));
            battery50.setTint(context.getResources().getColor(color));
            battery60.setTint(context.getResources().getColor(color));
            battery80.setTint(context.getResources().getColor(color));
            battery90.setTint(context.getResources().getColor(color));
            batteryFull.setTint(context.getResources().getColor(color));
            batteryAlert.setTint(context.getResources().getColor(color));
        }else{
            int defaultColor = R.color.colorThemeIconToolbar;
            battery20.setTint(context.getResources().getColor(defaultColor));
            battery30.setTint(context.getResources().getColor(defaultColor));
            battery50.setTint(context.getResources().getColor(defaultColor));
            battery60.setTint(context.getResources().getColor(defaultColor));
            battery80.setTint(context.getResources().getColor(defaultColor));
            battery90.setTint(context.getResources().getColor(defaultColor));
            batteryFull.setTint(context.getResources().getColor(defaultColor));
            batteryAlert.setTint(context.getResources().getColor(defaultColor));
        }

        try{
            int percent = Integer.parseInt(percentBattery.substring(0,percentBattery.length()-1));

            if(percent < 30)
                return battery20;

            else if(percent < 40)
                return battery30;

            else if(percent < 60)
                return battery50;

            else if(percent < 70)
                return battery60;

            else if(percent < 90)
                return battery80;

            else if(percent < 100)
                return battery90;

            else
                return batteryFull;

        }catch (Exception e){
            return batteryAlert;
        }
    }


}
