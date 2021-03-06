package com.example.demoapp.view.activity.fcl;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demoapp.R;
import com.example.demoapp.adapter.viewPager.ViewPagerAdapterFcl;
import com.example.demoapp.databinding.ActivityFclBinding;
import com.example.demoapp.transformer.ZoomOutPageTransformer;

public class FclActivity extends AppCompatActivity {

    private ActivityFclBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFclBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        bottomMenu();
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapterFcl viewPagerAdapterFcl = new ViewPagerAdapterFcl(this);
        binding.viewPagerFcl.setAdapter(viewPagerAdapterFcl);
    }

    @SuppressLint("NonConstantResourceId")
    private void bottomMenu() {
        binding.bottomNavFcl.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    binding.viewPagerFcl.setCurrentItem(0);
                    break;
                case R.id.navigation_fcl:
                    binding.viewPagerFcl.setCurrentItem(1);
                    break;
            }
            return true;
        });
        binding.viewPagerFcl.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){

                    case 0:
                        binding.bottomNavFcl.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        binding.viewPagerFcl.setPageTransformer(new ZoomOutPageTransformer());
                        break;

                    case 1:
                        binding.bottomNavFcl.getMenu().findItem(R.id.navigation_fcl).setChecked(true);
                        binding.viewPagerFcl.setPageTransformer(new ZoomOutPageTransformer());
                        break;
                }
            }
        });
    }


}