package com.example.demoapp.view.driver.UIWellcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.R;



public class UIWelcome_first extends AppCompatActivity {

    private Button btn_Next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiwelcome_first);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        btn_Next = findViewById(R.id.btn_Next_WelcomeFirst);
//        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.animate_diagonal_right_exit);
//        btn_Next.startAnimation(animation);
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentWelcome = new Intent(UIWelcome_first.this, com.example.demoapp.view.driver.UIWellcome.UIWelcome_second.class);
                startActivity(intentWelcome);
                //finish();
            }
        });
    }
}
