package com.example.demoapp.view.activity.imp;

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
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.demoapp.R;
import com.example.demoapp.databinding.ActivityImportPageBinding;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.utilities.PreferenceManager;
import com.example.demoapp.view.activity.login_register.SignInActivity;
import com.example.demoapp.view.activity.message.MainMessageActivity;
import com.example.demoapp.view.fragment.home.HomeFragment;
import com.example.demoapp.view.fragment.imp.ImportFragment;
import com.example.demoapp.view.fragment.imp.ImportLclFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ImportPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityImportPageBinding binding;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_IMPORT = 1;
    private static final int FRAGMENT_LCL = 2;
    private static final int ACTIVITY_MESSAGE = 3;
    private static final int LOG_OUT = 4;
    private PreferenceManager preferenceManager;

    private int mCurrentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImportPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        binding.navigationView.getMenu().findItem(R.id.tab_home_import).setChecked(true);
        preferenceManager = new PreferenceManager(this);
        initHearderView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tab_home_import:
                if (mCurrentFragment != FRAGMENT_HOME) {
                    replaceFragment(new HomeFragment());
                    mCurrentFragment = FRAGMENT_HOME;
                    binding.toolbar.setTitle("Home");
                }
                break;
            case R.id.tab_import:
                if (mCurrentFragment != FRAGMENT_IMPORT ){
                    replaceFragment(new ImportFragment());
                    mCurrentFragment = FRAGMENT_IMPORT;
                    binding.toolbar.setTitle("Department Import");
                }
                break;
            case R.id.tab_import_lcl:
                if (mCurrentFragment != FRAGMENT_LCL ){
                    replaceFragment(new ImportLclFragment());
                    mCurrentFragment = FRAGMENT_LCL;
                    binding.toolbar.setTitle("Department LCL");
                }
                break;
            case R.id.tab_chat_import:
                if (mCurrentFragment != ACTIVITY_MESSAGE) {
                    Intent intent = new Intent(this, MainMessageActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tab_logout_import:
                if (mCurrentFragment != LOG_OUT) {
                    signOut();
                }
                finish();
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
        TextView tvName = header.findViewById(R.id.tv_name_header);
        ImageView imageView = header.findViewById(R.id.image_header);
        tvName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }
}