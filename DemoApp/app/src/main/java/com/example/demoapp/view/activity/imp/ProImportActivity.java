package com.example.demoapp.view.activity.imp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.adapter.viewPager.ViewPagerAdapterImport;
import com.example.demoapp.databinding.ActivityProImportBinding;
import com.example.demoapp.transformer.ZoomOutPageTransformer;

public class ProImportActivity extends AppCompatActivity {

    private ActivityProImportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProImportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        bottomMenu();
        setupViewPager();

    }

    private void setupViewPager() {
        ViewPagerAdapterImport viewPagerAdapterImport = new ViewPagerAdapterImport(this);
        binding.viewPagerImport.setAdapter(viewPagerAdapterImport);
    }

    @SuppressLint("NonConstantResourceId")
    private void bottomMenu() {
        binding.bottomNavImport.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    binding.viewPagerImport.setCurrentItem(0);
                    break;
                case R.id.navigation_import:
                    binding.viewPagerImport.setCurrentItem(1);
                    break;
                case R.id.navigation_import_lcl:
                    binding.viewPagerImport.setCurrentItem(2);
                    break;
            }
            return true;
        });
        binding.viewPagerImport.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {

                    case 0:
                        binding.bottomNavImport.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        binding.viewPagerImport.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                    case 1:
                        binding.bottomNavImport.getMenu().findItem(R.id.navigation_import).setChecked(true);
                        binding.viewPagerImport.setPageTransformer(new ZoomOutPageTransformer());
                        break;
                    case 2:
                        binding.bottomNavImport.getMenu().findItem(R.id.navigation_import_lcl).setChecked(true);
                        binding.viewPagerImport.setPageTransformer(new ZoomOutPageTransformer());
                        break;
                }
            }
        });
    }

}