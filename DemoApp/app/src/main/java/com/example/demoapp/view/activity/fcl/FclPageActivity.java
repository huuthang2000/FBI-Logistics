package com.example.demoapp.view.activity.fcl;

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
import com.example.demoapp.databinding.ActivityFclPageBinding;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.utilities.PreferenceManager;
import com.example.demoapp.view.fragment.fcl.FCLFragment;
import com.example.demoapp.view.fragment.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

public class FclPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityFclPageBinding binding;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_FCL = 1;
    private static final int ACTIVITY_MESSAGE = 2;
    private static final int LOG_OUT = 3;
    private PreferenceManager preferenceManager;

    private int mCurrentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFclPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        binding.navigationView.getMenu().findItem(R.id.tab_home_fcl).setChecked(true);
        preferenceManager = new PreferenceManager(this);
//        initHearderView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tab_home_fcl:
                if (mCurrentFragment != FRAGMENT_HOME) {
                    replaceFragment(new HomeFragment());
                    mCurrentFragment = FRAGMENT_HOME;
                    binding.toolbar.setTitle("Home");
                }
                break;
            case R.id.tab_fcl:
                if (mCurrentFragment != FRAGMENT_FCL) {
                    replaceFragment(new FCLFragment());
                    mCurrentFragment = FRAGMENT_FCL;
                    binding.toolbar.setTitle("Air Retail Goods");
                }
                break;
            case R.id.tab_chat_fcl:
                if (mCurrentFragment != ACTIVITY_MESSAGE) {
//                    Intent intent = new Intent(this, MainMessageActivity.class);
//                    startActivity(intent);
                }
                break;
            case R.id.tab_logout_fcl:
                if (mCurrentFragment != LOG_OUT) {
//                    signOut();
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
//    private void signOut() {
//        showToast("Sign out...");
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference =
//                database.collection(Constants.KEY_COLLECTION_USERS).document(
//                        preferenceManager.getString(Constants.KEY_USER_ID)
//                );
//        HashMap<String, Object> update = new HashMap<>();
//        update.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
//        documentReference.update(update)
//                .addOnSuccessListener(undates -> {
//                    preferenceManager.clear();
//                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
//                    finish();
//                })
//                .addOnFailureListener(e -> showToast("Unable to sign out"));
//    }
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