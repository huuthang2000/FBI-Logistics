package com.example.demoapp.view.driver.UIWellcome;

import static com.example.demoapp.Utils.keyUtils.REQUEST_PERMISSION;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.R;

import java.util.Objects;


public class UIWelcome_second extends AppCompatActivity {

    private Button btn_Next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiwelcome_second);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        initPermissionStart();
        btn_Next = findViewById(R.id.btn_Next_WelcomeSecond);
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogAllowAllPermission();
            }
        });

    }

    /**
     * showDialogAllowAllPermission() This is the method that helps display the allow all the permission.
     */
    private void showDialogAllowAllPermission() {
        try {
                Dialog dialogMember = new Dialog(UIWelcome_second.this);
                dialogMember.setContentView(R.layout.custom_dialog_allow_all_location);
                dialogMember.setCancelable(false);
                Objects.requireNonNull(dialogMember.getWindow()).setLayout(getResources().getDisplayMetrics().widthPixels,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                Button btn_Allow_Permission = dialogMember.findViewById(R.id.btn_All_The_Permission);

            btn_Allow_Permission.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogMember.dismiss();
                        initPermission();
                    }
                });
                dialogMember.show();
        }catch (Exception e)
        {
            Log.e("showDialogMemberList",e.getMessage());

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animate_slide_in_left,R.anim.animate_slide_out_right);
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
            }
        }

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
            }
        }
        else {
            Intent intent = new Intent(UIWelcome_second.this, UIWelcome_third.class);
            startActivity(intent);
            finish();
        }
    }

    private void initPermissionStart() {
        boolean checkStartInent = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
               // requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
            }
            else {
                checkStartInent = true;
            }
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
            }
            else {
                checkStartInent = true;
           }
        }else {

            checkStartInent = true;
        }

        if(checkStartInent)
        {
            Intent intent = new Intent(UIWelcome_second.this, UIWelcome_third.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 3 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
                else {
//                    Intent intent = new Intent(UIWelcome_third.this, UIUpgradePremium.class);
                    Intent intent = new Intent(UIWelcome_second.this, UIWelcome_third.class);
                    startActivity(intent);
                    finish();

                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

        else
        {
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 2 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
                else {
//                    Intent intent = new Intent(UIWelcome_third.this, UIUpgradePremium.class);
                    Intent intent = new Intent(UIWelcome_second.this, UIWelcome_third.class);
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
}
