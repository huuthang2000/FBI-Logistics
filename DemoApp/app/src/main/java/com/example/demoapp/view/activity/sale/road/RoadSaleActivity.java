package com.example.demoapp.view.activity.sale.road;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.adapter.viewPager.ViewPagerAdapterRoad;
import com.example.demoapp.databinding.ActivityRoadSaleBinding;
import com.example.demoapp.transformer.ZoomOutPageTransformer;
import com.google.android.material.navigation.NavigationBarView;

public class RoadSaleActivity extends AppCompatActivity {

    private ActivityRoadSaleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoadSaleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setSupportActionBar(binding.toolbar);
        bottomMenu();
        setupViewPager();
        setContentView(view);
    }
    private void setupViewPager() {
        ViewPagerAdapterRoad viewPagerAdapterRoad = new ViewPagerAdapterRoad(this);
        binding.viewPagerRoad.setAdapter(viewPagerAdapterRoad);
    }

    private void bottomMenu() {
        binding.botomNavRoad.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_dom_cold:
                        binding.viewPagerRoad.setCurrentItem(0);
                        Toast.makeText(getApplicationContext(), "Xe tải lạnh", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_dom_dry:
                        binding.viewPagerRoad.setCurrentItem(1);
                        Toast.makeText(getApplicationContext(), "Xe tải khô", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_container_import:
                        binding.viewPagerRoad.setCurrentItem(2);
                        Toast.makeText(getApplicationContext(), "Nhập khẩu", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_container_export:
                        binding.viewPagerRoad.setCurrentItem(3);
                        Toast.makeText(getApplicationContext(), "Xuất khẩu", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });
        binding.viewPagerRoad.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){

                    case 0:
                        binding.botomNavRoad.getMenu().findItem(R.id.tab_dom_cold).setChecked(true);
                        binding.viewPagerRoad.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                    case 1:
                        binding.botomNavRoad.getMenu().findItem(R.id.tab_dom_dry).setChecked(true);
                        binding.viewPagerRoad.setPageTransformer(new ZoomOutPageTransformer());

                        break;
                    case 2:
                        binding.botomNavRoad.getMenu().findItem(R.id.tab_container_import).setChecked(true);
                        binding.viewPagerRoad.setPageTransformer(new ZoomOutPageTransformer());
                        break;
                    case 3:
                        binding.botomNavRoad.getMenu().findItem(R.id.tab_container_export).setChecked(true);
                        binding.viewPagerRoad.setPageTransformer(new ZoomOutPageTransformer());

                }
            }
        });
    }
}