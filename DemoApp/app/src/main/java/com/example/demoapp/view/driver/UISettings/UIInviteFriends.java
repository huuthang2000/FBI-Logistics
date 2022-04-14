package com.example.demoapp.view.driver.UISettings;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.R;
import com.example.demoapp.Utils.viewUtils;


public class UIInviteFriends extends AppCompatActivity {
    private final Context context = this;

    private TextView tvInviteCode;
    private Button btnSendCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        viewUtils.setupToolbar(context,R.id.toolbar,R.color.black,null,R.color.colorThemeStatusBar);

        initView();

        tvInviteCode.setText(objFamily.getMyFamily(context).getInviteCode());

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        tvInviteCode = findViewById(R.id.tvInviteCode);
        btnSendCode = findViewById(R.id.btnSendCode);

    }
}
