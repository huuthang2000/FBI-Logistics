package com.example.demoapp.view.activity.dom;

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
import com.example.demoapp.databinding.ActivityDomBinding;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.utilities.PreferenceManager;
import com.example.demoapp.view.fragment.dom.DomColdFragment;
import com.example.demoapp.view.fragment.dom.DomCyFragment;
import com.example.demoapp.view.fragment.dom.DomCySeaFragment;
import com.example.demoapp.view.fragment.dom.DomDoorFragment;
import com.example.demoapp.view.fragment.dom.DomDoorSeaFragment;
import com.example.demoapp.view.fragment.dom.DomDryFragment;
import com.example.demoapp.view.fragment.dom.DomExportFragment;
import com.example.demoapp.view.fragment.dom.DomImportFragment;
import com.example.demoapp.view.fragment.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;


public class DomActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityDomBinding binding;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_DOM_EXPORT = 1;
    private static final int FRAGMENT_DOM_IMPORT = 2;
    private static final int FRAGMENT_DOM_DRY = 3;
    private static final int FRAGMENT_DOM_COLD = 4;
    private static final int FRAGMENT_DOM_CY= 5;
    private static final int FRAGMENT_DOM_CY_SEA = 6;
    private static final int FRAGMENT_DOM_DOOR= 7;
    private static final int FRAGMENT_DOM_DOOR_SEA = 8;
    private static final int ACTIVITY_MESSAGE = 9;
    private static final int LOG_OUT = 10;
    private PreferenceManager preferenceManager;

    private int mCurrentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        binding.navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        preferenceManager = new PreferenceManager(this);
        initHearderView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                if (mCurrentFragment != FRAGMENT_HOME) {
                    replaceFragment(new HomeFragment());
                    mCurrentFragment = FRAGMENT_HOME;
                    binding.toolbar.setTitle("Home");
                }
                break;
            case R.id.nav_dom_export:
                if (mCurrentFragment != FRAGMENT_DOM_EXPORT ){
                    replaceFragment(new DomExportFragment());
                    mCurrentFragment = FRAGMENT_DOM_EXPORT;
                    binding.toolbar.setTitle("Dom Export");
                }
                break;
            case R.id.nav_dom_import:
                if (mCurrentFragment != FRAGMENT_DOM_IMPORT ){
                    replaceFragment(new DomImportFragment());
                    mCurrentFragment = FRAGMENT_DOM_IMPORT;
                    binding.toolbar.setTitle("Dom Import");
                }
                break;
            case R.id.nav_dom_dry:
                if (mCurrentFragment != FRAGMENT_DOM_DRY ){
                    replaceFragment(new DomDryFragment());
                    mCurrentFragment = FRAGMENT_DOM_DRY;
                    binding.toolbar.setTitle("Dom Dry");
                }
                break;
            case R.id.nav_dom_cold:
                if (mCurrentFragment != FRAGMENT_DOM_COLD ){
                    replaceFragment(new DomColdFragment());
                    mCurrentFragment = FRAGMENT_DOM_COLD;
                    binding.toolbar.setTitle("Dom Cold");
                }
                break;
            case R.id.nav_dom_cy:
                if (mCurrentFragment != FRAGMENT_DOM_CY ){
                    replaceFragment(new DomCyFragment());
                    mCurrentFragment = FRAGMENT_DOM_CY;
                    binding.toolbar.setTitle("Dom Cy");
                }
                break;
            case R.id.nav_dom_door:
                if (mCurrentFragment != FRAGMENT_DOM_DOOR ){
                    replaceFragment(new DomDoorFragment());
                    mCurrentFragment = FRAGMENT_DOM_DOOR;
                    binding.toolbar.setTitle("Dom Door");
                }
                break;
            case R.id.nav_dom_cy_sea:
                if (mCurrentFragment != FRAGMENT_DOM_CY_SEA ){
                    replaceFragment(new DomCySeaFragment());
                    mCurrentFragment = FRAGMENT_DOM_CY_SEA;
                    binding.toolbar.setTitle("Dom Cy Sea");
                }
                break;
            case R.id.nav_dom_door_sea:
                if (mCurrentFragment != FRAGMENT_DOM_DOOR_SEA){
                    replaceFragment(new DomDoorSeaFragment());
                    mCurrentFragment = FRAGMENT_DOM_DOOR_SEA;
                    binding.toolbar.setTitle("Dom Door Sea");
                }
                break;
            case R.id.nav_message:
                if (mCurrentFragment != ACTIVITY_MESSAGE) {
//                    Intent intent = new Intent(this, MainMessageActivity.class);
//                    startActivity(intent);
                }
                break;
            case R.id.nav_logout:
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