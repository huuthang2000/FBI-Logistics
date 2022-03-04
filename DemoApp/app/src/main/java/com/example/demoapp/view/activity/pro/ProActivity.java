package com.example.demoapp.view.activity.pro;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.demoapp.R;

import com.example.demoapp.view.activity.dom.DomActivity;

import com.example.demoapp.adapter.PriceListAIRAdapter;

import com.example.demoapp.view.activity.imp.ProImportActivity;
import com.example.demoapp.view.fragment.air.AirExportFragment;
import com.example.demoapp.view.fragment.fcl.FCLFragment;
import com.example.demoapp.view.fragment.home.HomeFragment;
import com.example.demoapp.view.fragment.log.LogFragment;
import com.google.android.material.navigation.NavigationView;

public class ProActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static int FRAGMENT_HOME = 0;
    private final static int FRAGMENT_FCL = 1;
    private final static int FRAGMENT_LCL = 2;
    private final static int FRAGMENT_IMPORT = 3;
    private final static int FRAGMENT_DOM = 4;
    private final static int FRAGMENT_LOG = 5;

    // check currently fragment
    private int mCurrentFragment = FRAGMENT_HOME;

    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    private SearchView searchView;
    private PriceListAIRAdapter mAirAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_main);
        toolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // vao app la open fragment home
        replaceFragment(new HomeFragment());
        toolbar.setTitle("FBI Logistics");

        // checked fragment home
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "Pro Activity Resume", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "Pro Activity Pause", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "Pro Activity Stop", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_home){
            if(mCurrentFragment != FRAGMENT_HOME){
                replaceFragment(new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
                toolbar.setTitle("FBI Logistics");
            }
        }else if(id == R.id.nav_fcl){
            if(mCurrentFragment != FRAGMENT_FCL){
                replaceFragment(new FCLFragment());
                mCurrentFragment = FRAGMENT_FCL;
                toolbar.setTitle("FCL PAGE");

            }

        }else if (id == R.id.nav_lcl) {
            if (mCurrentFragment != FRAGMENT_LCL) {
                replaceFragment(new AirExportFragment());
                mCurrentFragment = FRAGMENT_LCL;
                toolbar.setTitle("AIR PAGE");
            }
        }
        else if (id == R.id.nav_import){
            if(mCurrentFragment != FRAGMENT_IMPORT){
                Intent intent = new Intent(this, ProImportActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else if (id == R.id.nav_dom){
            if(mCurrentFragment != FRAGMENT_DOM){
                Intent intent = new Intent(this, DomActivity.class);
                startActivity(intent);
                finish();
            }
        }else if( id == R.id.nav_log) {
            if (mCurrentFragment != FRAGMENT_LOG) {
                replaceFragment(new LogFragment());
                mCurrentFragment = FRAGMENT_LOG;
                toolbar.setTitle("LOG PAGE");
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // khi nhan back khong thoat app
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();
    }


}