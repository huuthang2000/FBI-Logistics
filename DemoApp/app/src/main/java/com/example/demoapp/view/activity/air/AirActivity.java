package com.example.demoapp.view.activity.air;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.adapter.viewPager.ViewPagerAdapterAir;
import com.example.demoapp.databinding.ActivityAirBinding;
import com.example.demoapp.transformer.ZoomOutPageTransformer;
import com.google.android.material.navigation.NavigationBarView;

public class AirActivity extends AppCompatActivity {

    private ActivityAirBinding mAirBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAirBinding = ActivityAirBinding.inflate(getLayoutInflater());
        View view = mAirBinding.getRoot();
        setContentView(view);
        bottomMenu();
        setupViewPager();

    }

    private void setupViewPager() {
        ViewPagerAdapterAir viewPagerAdapterAir = new ViewPagerAdapterAir(this);
        mAirBinding.viewPagerAir.setAdapter(viewPagerAdapterAir);
    }

    private void bottomMenu() {
        mAirBinding.botomNavAir.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_home_air:
                        mAirBinding.viewPagerAir.setCurrentItem(0);
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_import_air:
                        mAirBinding.viewPagerAir.setCurrentItem(1);
                        Toast.makeText(getApplicationContext(), "Nhập khẩu", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_export_air:
                        mAirBinding.viewPagerAir.setCurrentItem(2);
                        Toast.makeText(getApplicationContext(), "Xuất khẩu", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_retail_goods_air:
                        mAirBinding.viewPagerAir.setCurrentItem(3);
                        Toast.makeText(getApplicationContext(), "Hàng lẻ xuất khẩu", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });
        mAirBinding.viewPagerAir.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){

                    case 0:
                        mAirBinding.botomNavAir.getMenu().findItem(R.id.tab_home_air).setChecked(true);
                        mAirBinding.viewPagerAir.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                    case 1:
                        mAirBinding.botomNavAir.getMenu().findItem(R.id.tab_import_air).setChecked(true);
                        mAirBinding.viewPagerAir.setPageTransformer(new ZoomOutPageTransformer());

                        break;
                    case 2:
                        mAirBinding.botomNavAir.getMenu().findItem(R.id.tab_export_air).setChecked(true);
                        mAirBinding.viewPagerAir.setPageTransformer(new ZoomOutPageTransformer());
                        break;
                    case 3:
                        mAirBinding.botomNavAir.getMenu().findItem(R.id.tab_retail_goods_air).setChecked(true);
                        mAirBinding.viewPagerAir.setPageTransformer(new ZoomOutPageTransformer());

                }
            }
        });
    }


}