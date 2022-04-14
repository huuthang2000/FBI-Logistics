package com.example.demoapp.view.driver.UIWellcome;

import static com.example.demoapp.Utils.keyUtils.CHECK_WELCOME;
import static com.example.demoapp.Utils.keyUtils.REQUEST_PERMISSION;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.Presenters.Account.pre_Account;
import com.example.demoapp.Presenters.Account.view_Account;
import com.example.demoapp.Presenters.Group.pre_Family;
import com.example.demoapp.R;
import com.example.demoapp.Utils.directionalController;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.example.demoapp.view.driver.UILogin;

public class UIWelcome_third extends AppCompatActivity implements view_Account {
    private final Context context = this;
    private pre_Account mLogin;
    private Button btn_Allow;
    private SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiwelcome_third);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        viewUtils.setTransparentStatusBar((AppCompatActivity) context);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(keyUtils.KEY_FAMILY_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        btn_Allow = findViewById(R.id.btn_Next_WelcomeAllow);
//        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.animate_slide_out_right);
//        btn_Allow.startAnimation(animation);
        btn_Allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPermission();
            }
        });
    }

    /*private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

                Log.d("packageName", UIWelcome_third.this.getPackageName());
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                myIntent.setData(Uri.parse("package:" + UIWelcome_third.this.getPackageName()));
                startActivity(myIntent);
            }
        }

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

                Log.d("packageName",UIWelcome_third.this.getPackageName());
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                myIntent.setData(Uri.parse("package:" + UIWelcome_third.this.getPackageName()));
                startActivity(myIntent);
            }
        }
        else {
            Intent intent = new Intent(UIWelcome_third.this, UILogin.class);
            startActivity(intent);
            finish();

            editor.putBoolean(CHECK_WELCOME,true);
            editor.apply();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 7 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[3] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[4] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[5] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[6] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
                else {
//                    Intent intent = new Intent(UIWelcome_third.this, UIUpgradePremium.class);
                    Intent intent = new Intent(UIWelcome_third.this, UILogin.class);
                    startActivity(intent);
                    finish();
                    editor.putBoolean(CHECK_WELCOME,true);
                    editor.apply();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

        else
        {
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 5 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[3] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[4] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
                else {
//                    Intent intent = new Intent(UIWelcome_third.this, UIUpgradePremium.class);
                    Intent intent = new Intent(UIWelcome_third.this, UILogin.class);
                    startActivity(intent);
                    finish();
//                    mLogin = new pre_Account(context,this);
//                    mLogin.autoSignIn();
                    editor.putBoolean(CHECK_WELCOME,true);
                    editor.apply();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            }
        }

    }*/

    private void initPermission() {
        boolean checkAllow = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

                Log.d("packageName", UIWelcome_third.this.getPackageName());
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                myIntent.setData(Uri.parse("package:" + UIWelcome_third.this.getPackageName()));
                startActivity(myIntent);
            }
            else {
                checkAllow = true;
            }
        }

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

                Log.d("packageName",UIWelcome_third.this.getPackageName());
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                myIntent.setData(Uri.parse("package:" + UIWelcome_third.this.getPackageName()));
                startActivity(myIntent);
            }
            else {
                checkAllow = true;
            }
        }
        else {
            checkAllow = true;
        }

        if(checkAllow)
        {
            Intent intent = new Intent(UIWelcome_third.this, UILogin.class);
            startActivity(intent);
            finish();

            editor.putBoolean(CHECK_WELCOME,true);
            editor.apply();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       // Log.d("ssaassa", "Build.VERSION_COD grantResults.length = "+grantResults.length);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //Log.d("ssaassa", "Build.VERSION_CODES.Q grantResults.length = "+grantResults.length);
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 4 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[3] != PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.putBoolean(CHECK_WELCOME,true);
                    editor.apply();
//                    Intent intent = new Intent(UIWelcome_third.this, UIUpgradePremium.class);
                    Intent intent = new Intent(UIWelcome_third.this, UILogin.class);
                    startActivity(intent);
                    finish();

                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        else
        {
            //Log.d("ssaassa", "grantResults.length = "+grantResults.length);
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 4) {
                    // ||
                    //                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    //                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    //                        grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                    //                        grantResults[3] != PackageManager.PERMISSION_GRANTED
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.putBoolean(CHECK_WELCOME,true);
                    editor.apply();
//                    Intent intent = new Intent(UIWelcome_third.this, UIUpgradePremium.class);
                    Intent intent = new Intent(UIWelcome_third.this, UILogin.class);
                    startActivity(intent);
                    finish();
//                    mLogin = new pre_Account(context,this);
//                    mLogin.autoSignIn();

                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void errorEmailOrPassword(String errorEmail, String errorPassword) {

    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {

        if(type.matches(keyUtils.ResultAutoLogin))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(isSuccess){
                        pre_Family.checkFamilyID(new pre_Family.onResultCheckFamilyID() {
                            @Override
                            public void onResult(boolean isSuccess, long size, String message) {
                                if(isSuccess && size > 0){
                                    directionalController.goToUILoadingData(context);
                                    finish();
                                }
                                else{
                                    directionalController.goToUISetupProfile(context);
                                    finish();
                                }
                            }
                        });
                    }else{
                        startActivity(new Intent(context, UILogin.class));
                        finish();
                    }
                }
            },200);
        }
    }

    @Override
    public void errorInputRegister(String email, String password, String confirmPassword) {

    }


    @Override
    public void errorInputSettingProfile(String errorUsername, String errorPhoneNumber) {

    }

    @Override
    public void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName) {

    }

    @Override
    public void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm) {

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animate_slide_in_left,R.anim.animate_slide_out_right);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
