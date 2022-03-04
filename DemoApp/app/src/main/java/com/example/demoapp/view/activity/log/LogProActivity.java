package com.example.demoapp.view.activity.log;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.adapter.viewPager.ViewPagerAdapterLog;
import com.example.demoapp.databinding.ActivityLogProBinding;
import com.example.demoapp.transformer.ZoomOutPageTransformer;
import com.google.android.material.navigation.NavigationBarView;

public class LogProActivity extends AppCompatActivity {

    private ActivityLogProBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogProBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        bottomMenu();
        setupViewPager();

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_log)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_log_pro);
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void setupViewPager() {
        ViewPagerAdapterLog viewPagerAdapterLog = new ViewPagerAdapterLog(this);
        binding.viewPagerLog.setAdapter(viewPagerAdapterLog);
    }

    private void bottomMenu() {
        binding.botomNavLog.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        binding.viewPagerLog.setCurrentItem(0);
                        break;
                    case R.id.navigation_log:
                        binding.viewPagerLog.setCurrentItem(1);
                        break;

                }
                return true;
            }
        });
        binding.viewPagerLog.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){

                    case 0:
                        binding.botomNavLog.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        binding.viewPagerLog.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                    case 1:
                        binding.botomNavLog.getMenu().findItem(R.id.navigation_log).setChecked(true);
                        binding.viewPagerLog.setPageTransformer(new ZoomOutPageTransformer());

                        break;

                }
            }
        });
    }


}