package com.example.demoapp.view.activity.sale.rail;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.adapter.viewPager.ViewPagerAdapterRail;
import com.example.demoapp.databinding.ActivityRailSaleBinding;
import com.example.demoapp.transformer.ZoomOutPageTransformer;
import com.google.android.material.navigation.NavigationBarView;

public class RailSaleActivity extends AppCompatActivity {

    private ActivityRailSaleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRailSaleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setSupportActionBar(binding.toolbar);
        bottomMenu();
        setupViewPager();
        setContentView(view);
    }
    private void setupViewPager() {
        ViewPagerAdapterRail viewPagerAdapterRail = new ViewPagerAdapterRail(this);
        binding.viewPagerRail.setAdapter(viewPagerAdapterRail);
    }

    private void bottomMenu() {
        binding.botomNavRailTransport.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_cy_cy_dom:
                        binding.viewPagerRail.setCurrentItem(0);
                        Toast.makeText(getApplicationContext(), "Cy_Cy", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_door_to_door_dom:
                        binding.viewPagerRail.setCurrentItem(1);
                        Toast.makeText(getApplicationContext(), "Door_To_Door", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        binding.viewPagerRail.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){

                    case 0:
                        binding.botomNavRailTransport.getMenu().findItem(R.id.tab_cy_cy_dom).setChecked(true);
                        binding.viewPagerRail.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                    case 1:
                        binding.botomNavRailTransport.getMenu().findItem(R.id.tab_door_to_door_dom).setChecked(true);
                        binding.viewPagerRail.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                }
            }
        });
    }
}