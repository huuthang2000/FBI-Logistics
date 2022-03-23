package com.example.demoapp.view.activity.sale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.demoapp.R;
import com.example.demoapp.databinding.ActivitySaleBinding;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.utilities.PreferenceManager;
import com.example.demoapp.view.activity.login_register.SignInActivity;
import com.example.demoapp.view.activity.message.MainMessageActivity;
import com.example.demoapp.view.fragment.sales.AirlinesSaleFragment;
import com.example.demoapp.view.fragment.sales.HomeSaleFragment;
import com.example.demoapp.view.fragment.sales.InlandFragment;
import com.example.demoapp.view.fragment.sales.RoadFragment;
import com.example.demoapp.view.fragment.sales.SeawayFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SaleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_SEAWAY = 1;
    private static final int FRAGMENT_AIRLINES = 2;
    private static final int FRAGMENT_ROAD = 3;
    private static final int FRAGMENT_INLAND = 4;
    private static final  int FRAGMENT_OVERSEA = 5;
    private static final int FRAGMENT_LUUKHO = 6;
    private static final int FRAGMENT_HAIQUAN = 7;
    private static final int FRAGMENT_BAOHIEM = 8;
    private static final int FRAGMENT_NVOCC = 9;
    private static final  int FRAGMENT_DEPOT = 10;
    private static final int ACTIVITY_MESSAGE = 11;
    private static final  int LOGOUT = 12;
    private int mCurrentFragmet = FRAGMENT_HOME;

    DrawerLayout mDrawerLayout;
    TextView tvName, tvEmail;
    private PreferenceManager preferenceManager;
    Toolbar toolbar;
    private ActivitySaleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        anhxa();
        Actionbar();
    }


    private void anhxa() {
        toolbar = findViewById(R.id.tb_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void Actionbar() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar
                , R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        // xử lí mặc định vào trang home
        replaceFragment(new HomeSaleFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setCheckable(true);
        initHearderView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (mCurrentFragmet != FRAGMENT_HOME) {
                replaceFragment(new HomeSaleFragment());
                mCurrentFragmet = FRAGMENT_HOME;
            }
            finish();
        } else if (id == R.id.nav_duongbien) {
            if (mCurrentFragmet != FRAGMENT_SEAWAY) {
                replaceFragment(new SeawayFragment());
                mCurrentFragmet = FRAGMENT_SEAWAY;
            }
            finish();
        } else if (id == R.id.nav_hangkhong) {
            if (mCurrentFragmet != FRAGMENT_AIRLINES) {
                replaceFragment(new AirlinesSaleFragment());
                mCurrentFragmet = FRAGMENT_AIRLINES;
            }
            finish();
        } else if (id == R.id.nav_duongbo) {
            if (mCurrentFragmet != FRAGMENT_ROAD) {
                replaceFragment(new RoadFragment());
                mCurrentFragmet = FRAGMENT_ROAD;
            }
            finish();
        } else if (id == R.id.nav_noidia) {
            if (mCurrentFragmet != FRAGMENT_INLAND) {
                replaceFragment(new InlandFragment());
                mCurrentFragmet = FRAGMENT_INLAND;
            }
            finish();
        }else  if(id == R.id.nav_haiquan){
            if(mCurrentFragmet != FRAGMENT_HAIQUAN){
                Intent intent = new Intent(this, LogActivity.class);
                startActivity(intent);
            }
            finish();
        }else if(id == R.id.nav_message){
            if(mCurrentFragmet != ACTIVITY_MESSAGE){
                Intent intent = new Intent(this, MainMessageActivity.class);
                startActivity(intent);
            }
            finish();
        }else if(id == R.id.log_out){
            if(mCurrentFragmet != LOGOUT){
                signOut();
            }
            finish();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // sử lý fragement
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private void signOut() {
        showToast("Sign out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> update = new HashMap<>();
        update.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(update)
                .addOnSuccessListener(undates -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initHearderView(){
        View header = binding.navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.tv_name_header_sale);
        ImageView imageView = header.findViewById(R.id.image_header_sale);
        tvName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }

}