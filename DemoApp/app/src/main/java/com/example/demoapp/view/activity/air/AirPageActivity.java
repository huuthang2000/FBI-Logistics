package com.example.demoapp.view.activity.air;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.demoapp.R;
import com.example.demoapp.databinding.ActivityAirPageBinding;
import com.example.demoapp.view.fragment.air.AirExportFragment;
import com.example.demoapp.view.fragment.air.AirImportFragment;
import com.example.demoapp.view.fragment.air.RetailGoodsExportFragment;
import com.example.demoapp.view.fragment.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;


public class AirPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityAirPageBinding binding;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_AIR_IMPORT = 1;
    private static final int FRAGMENT_AIR_EXPORT = 2;
    private static final int FRAGMENT_AIR_RETAIL_GOODS = 3;
    private static final int FRAGMENT_MY_PROFILE = 4;

    private int mCurrentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAirPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        binding.navigationView.getMenu().findItem(R.id.tab_home_air).setChecked(true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.search, menu);
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tab_home_air:
                if (mCurrentFragment != FRAGMENT_HOME) {
                    replaceFragment(new HomeFragment());
                    mCurrentFragment = FRAGMENT_HOME;
                    binding.toolbar.setTitle("Home");
                }
                break;
            case R.id.tab_import_air:
                if (mCurrentFragment != FRAGMENT_AIR_IMPORT) {
                    replaceFragment(new AirImportFragment());
                    mCurrentFragment = FRAGMENT_AIR_IMPORT;
                    binding.toolbar.setTitle("Air Import");
                }
                break;
            case R.id.tab_export_air:
                if (mCurrentFragment != FRAGMENT_AIR_EXPORT) {
                    replaceFragment(new AirExportFragment());
                    mCurrentFragment = FRAGMENT_AIR_EXPORT;
                    binding.toolbar.setTitle("Air Export");
                }
                break;
            case R.id.tab_retail_goods_air:
                if (mCurrentFragment != FRAGMENT_AIR_RETAIL_GOODS) {
                    replaceFragment(new RetailGoodsExportFragment());
                    mCurrentFragment = FRAGMENT_AIR_RETAIL_GOODS;
                    binding.toolbar.setTitle("Air Retail Goods");
                }
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}