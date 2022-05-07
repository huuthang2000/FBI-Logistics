package com.example.demoapp.view.activity.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.adapter.chat.MyViewPagerAdapter;
import com.example.demoapp.databinding.ActivityDashboardBinding;
import com.example.demoapp.view.activity.login_register.SignInActivity;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    private ActivityDashboardBinding binding;
    private ActionBar actionBar;
    private FirebaseAuth mAuth;
    private String mUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();
        actionBar.setTitle("Home");

        mAuth = FirebaseAuth.getInstance();


        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.viewPager.setAdapter(myViewPagerAdapter);

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        actionBar.setTitle("Home");
                        binding.viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_profile:
                        actionBar.setTitle("Profile");
                        binding.viewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_users:
                        actionBar.setTitle("Users");
                        binding.viewPager.setCurrentItem(2);
                        break;
                    case R.id.nav_chat:
                        actionBar.setTitle("Chats");
                        binding.viewPager.setCurrentItem(3);
                        break;

                }
                return true;
            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                    case 2:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_users).setChecked(true);
                        break;
                    case 3:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_chat).setChecked(true);
                        break;
                }
            }
        });

        checkUserStatus();

    }


    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    private void  checkUserStatus(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            mUID = user.getUid();
            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", mUID);
            editor.apply();
        }else{
            startActivity(new Intent(DashboardActivity.this, SignInActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        // check on start of app
        checkUserStatus();
        super.onStart();
    }
}