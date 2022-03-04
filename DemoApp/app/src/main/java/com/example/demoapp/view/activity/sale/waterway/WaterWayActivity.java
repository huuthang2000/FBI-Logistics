package com.example.demoapp.view.activity.sale.waterway;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.adapter.viewPager.ViewPagerAdapterWaterWay;
import com.example.demoapp.databinding.ActivityWaterWayBinding;
import com.example.demoapp.transformer.ZoomOutPageTransformer;
import com.google.android.material.navigation.NavigationBarView;

public class WaterWayActivity extends AppCompatActivity {
    private ActivityWaterWayBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaterWayBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setSupportActionBar(binding.toolbar);
        bottomMenu();
        setupViewPager();
        setContentView(view);
    }
    private void setupViewPager() {
        ViewPagerAdapterWaterWay viewPagerAdapterWaterWay = new ViewPagerAdapterWaterWay(this);
        binding.viewPagerWaterway.setAdapter(viewPagerAdapterWaterWay);
    }

    private void bottomMenu() {
        binding.botomNavWaterway.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_cy_waterway:
                        binding.viewPagerWaterway.setCurrentItem(0);
                        break;
                    case R.id.tab_door_waterway:
                        binding.viewPagerWaterway.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });
        binding.viewPagerWaterway.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){

                    case 0:
                        binding.botomNavWaterway.getMenu().findItem(R.id.tab_cy_waterway).setChecked(true);
                        binding.viewPagerWaterway.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                    case 1:
                        binding.botomNavWaterway.getMenu().findItem(R.id.tab_door_waterway).setChecked(true);
                        binding.viewPagerWaterway.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                }
            }
        });
    }

}