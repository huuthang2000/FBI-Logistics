package com.example.demoapp.view.driver.UIWellcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.Presenters.Account.pre_Account;
import com.example.demoapp.Presenters.Account.view_Account;
import com.example.demoapp.R;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.example.demoapp.view.driver.UISetupProfile;
import com.google.android.material.textfield.TextInputLayout;


public class UIRegister extends AppCompatActivity implements view_Account {
    private final Context context = this;

    private pre_Account mRegister;

    private TextView tvLogin;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;

    private Button btnRegister;

    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewUtils.setTransparentStatusBar((AppCompatActivity) context);
        initView();

        setAction();
    }

    private void setAction() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.GONE);
                btnRegister.setEnabled(false);
                mRegister.actionSignUp(tilEmail.getEditText().getText().toString(),
                        tilPassword.getEditText().getText().toString(),
                        tilConfirmPassword.getEditText().getText().toString());
            }
        });
    }

    private void initView() {
        mRegister = new pre_Account(context,this);
        tvLogin = findViewById(R.id.tvLogin);

        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        btnRegister = findViewById(R.id.btnRegister);

        tvError = findViewById(R.id.tvError);
    }


    @Override
    public void errorEmailOrPassword(String ErrorEmail, String ErrorPassword) {

    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {
        if(type.matches(keyUtils.ResultRegister)){


            btnRegister.setEnabled(true);

            if(isSuccess)
            {
                Intent intent = new Intent(context, UISetupProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else{
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(message);
            }
        }
    }


    @Override
    public void errorInputRegister(String errorEmail, String errorPassword, String errorConfirmPassword) {

        btnRegister.setEnabled(true);

        tilEmail.setErrorEnabled(errorEmail != null);
        tilConfirmPassword.setErrorEnabled(errorConfirmPassword != null);
        tilPassword.setErrorEnabled(errorPassword != null);

        tilConfirmPassword.setError(errorConfirmPassword);
        tilEmail.setError(errorEmail);
        tilPassword.setError(errorPassword);
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


}